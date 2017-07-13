package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.dialogs.ReminderDialog;
import com.pooja.carepack.model.BasicResponse;
import com.pooja.carepack.model.ModelReminder;
import com.pooja.carepack.model.ModelTreatmentAppointment;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TreatmentAddFragment extends BaseFragment implements LibPostListner, OnClickListener, MainActivity.OnTimeDialogListener, MainActivity.OnDateDialogListener {

    public int menu_item = 0;
    private View view;
    private Menu menu;
    private List<ModelReminder.Reminder> reminder;
    private String treatmentId;
    private EditText etTitle, etLocation, etStart, etEnd, etRepeat, etStartDate, etEndDate, etInvite, etReminder, etNotes;
    private LinearLayout llStart, llEnd, llRepeat, llStartDate, llEndDate, llInvite, llReminder, llNotes;
    private Animation shakeAnim;
    private boolean isStartTime;
    private boolean isStartDate;
    private String selectedReminderMin;
    private ArrayList<CharSequence> invitees;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_treatment_add, container, false);
        etTitle = (EditText) view.findViewById(R.id.frg_treatment_info_etTitle);
        etLocation = (EditText) view.findViewById(R.id.frg_treatment_info_etLocation);

        etStart = (EditText) view.findViewById(R.id.frg_treatment_info_etStart);
        llStart = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llStart);
        llStart.setOnClickListener(this);

        etEnd = (EditText) view.findViewById(R.id.frg_treatment_info_etEnd);
        llEnd = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llEnd);
        llEnd.setOnClickListener(this);

        etRepeat = (EditText) view.findViewById(R.id.frg_treatment_info_etRepeat);
        etRepeat.setText(getResources().getStringArray(R.array.repeat)[0].toString());
        llRepeat = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llRepeat);
        llRepeat.setOnClickListener(this);

        etStartDate = (EditText) view.findViewById(R.id.frg_treatment_info_etStartDate);
        llStartDate = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llStartDate);
        llStartDate.setOnClickListener(this);

        etEndDate = (EditText) view.findViewById(R.id.frg_treatment_info_etEndDate);
        etEndDate.setVisibility(View.GONE);
        llEndDate = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llEndDate);
        llEndDate.setOnClickListener(this);

        etInvite = (EditText) view.findViewById(R.id.frg_treatment_info_etInvitee);
        llInvite = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llInvitee);
        llInvite.setOnClickListener(this);
        invitees = new ArrayList<>();
        etInvite.setText(invitees.size() + "");

        etReminder = (EditText) view.findViewById(R.id.frg_treatment_info_etReminder);
        llReminder = (LinearLayout) view.findViewById(R.id.frg_treatment_info_llReminder);
        llReminder.setOnClickListener(this);

        etNotes = (EditText) view.findViewById(R.id.frg_treatment_info_etNote);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        setHasOptionsMenu(true);


        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                etStartDate.setText(bundle.getString("startdate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Log.d(" in bundle");
                treatmentId = bundle.getString("appointmentId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (treatmentId != null)
            getTreatmentInfo(treatmentId);
        else treatmentId = "0";

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getReminderInfo();

    }

    private void getReminderInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        new PostLibResponse(TreatmentAddFragment.this, new ModelReminder(), getActivity(), hashMap, WebServicesConst.TREATMENT_REMINDER, WebServicesConst.RES.TREATMENT_REMINDER, true, true);
    }

    private void getTreatmentInfo(String treatmentId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userid", "" + prefs.get(MyPrefs.keys.ID));
        hashMap.put("id", treatmentId);
        new PostLibResponse(TreatmentAddFragment.this, new ModelTreatmentAppointment(), getActivity(), hashMap, WebServicesConst.TREATMENT_PLAN, WebServicesConst.RES.TREATMENT_GET, true, true);
    }

    private void setTreatmentInfo() {
        String invitee = invitees.toString();
        invitee = invitee.substring(1, invitee.length() - 1);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("action", "submit_treatment_plan");
        hashMap.put("userid", "" + prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", "" + prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", treatmentId);
        hashMap.put("title", "" + etTitle.getText().toString());
        hashMap.put("notes", "" + etNotes.getText().toString());
        hashMap.put("location", "" + etLocation.getText().toString());
        hashMap.put("starts", "" + etStart.getText().toString());
        hashMap.put("ends", "" + etEnd.getText().toString());
        hashMap.put("repeats", "" + etRepeat.getText().toString());
        hashMap.put("startdate", "" + etStartDate.getText().toString());
        hashMap.put("enddate", "" + etEndDate.getText().toString());
        hashMap.put("invitees", "" + invitee);
        hashMap.put("reminder", "" + selectedReminderMin);
        new PostLibResponse(TreatmentAddFragment.this, new BasicResponse(), getActivity(), hashMap, WebServicesConst.TREATMENT_PLAN, WebServicesConst.RES.TREATMENT_ADD, true, true);
    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.TREATMENT_REMINDER) {
            ModelReminder modelReminder = (ModelReminder) clsGson;
            if (modelReminder != null) {
                if (modelReminder.status == 200) {
                    reminder = modelReminder.reminder;
                    etReminder.setText(reminder.get(0).title);
                    selectedReminderMin = reminder.get(0).minute;
                } else {
                    toast(modelReminder.message);
                }
            }
        } else if (requestCode == WebServicesConst.RES.TREATMENT_GET) {
            ModelTreatmentAppointment treatmentAppointment = (ModelTreatmentAppointment) clsGson;
            if (treatmentAppointment != null) {
                if (treatmentAppointment.status == 200) {
                    loadData(treatmentAppointment.treatment_plans);
                } else {
                    toast(treatmentAppointment.message);
                }
            }
        } else if (requestCode == WebServicesConst.RES.TREATMENT_ADD) {
            BasicResponse basicResponse = (BasicResponse) clsGson;
            toast(basicResponse.message);
            getActivity().onBackPressed();
        }
    }

    private void loadData(ModelTreatmentAppointment.TreatmentPlans treatment_plans) {
        try {
            etTitle.setText(treatment_plans.title);
            etLocation.setText(treatment_plans.location);
            etStart.setText(treatment_plans.starts);
            etEnd.setText(treatment_plans.ends);
            etRepeat.setText(treatment_plans.repeats);
            if(!etRepeat.getText().toString().equalsIgnoreCase("Never"))
                etEndDate.setVisibility(View.VISIBLE);
            etStartDate.setText(treatment_plans.startdate);
            etEndDate.setText(treatment_plans.enddate);
            startDate.set(Integer.parseInt(Utility.getFormatedDate(treatment_plans.startdate, "yyyy-MM-dd", "yyyy")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.startdate, "yyyy-MM-dd", "MM")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.startdate, "yyyy-MM-dd", "dd")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.starts, "hh:mm a", "HH")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.starts, "hh:mm a", "mm")), 0);
            endDate.set(Integer.parseInt(Utility.getFormatedDate(treatment_plans.enddate, "yyyy-MM-dd", "yyyy")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.enddate, "yyyy-MM-dd", "MM")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.enddate, "yyyy-MM-dd", "dd")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.ends, "hh:mm a", "HH")),
                    Integer.parseInt(Utility.getFormatedDate(treatment_plans.ends, "hh:mm a", "mm")), 0);
            try {
                etInvite.setText(treatment_plans.invitees.size() + "");
                invitees.clear();
                invitees.addAll(treatment_plans.invitees);
            } catch (Exception e) {
                e.printStackTrace();
            }
            selectedReminderMin = treatment_plans.reminder;
            etNotes.setText(treatment_plans.notes);
            etReminder.setText(treatment_plans.reminder_title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(menu_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_save:
                if (valid()) {
                    setTreatmentInfo();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean valid() {
        if (etTitle.getText().length() == 0) {
            etTitle.startAnimation(shakeAnim);
            etTitle.requestFocus();
            toast(R.string.toast_invalid_, etTitle.getHint().toString());
            return false;
//        } else if (etLocation.getText().length() == 0) {
//            etLocation.startAnimation(shakeAnim);
//            etLocation.requestFocus();
//            toast(R.string.toast_invalid_, etLocation.getHint().toString());
//            return false;
        } else if (etStart.getText().length() == 0) {
            etStart.startAnimation(shakeAnim);
            etStart.requestFocus();
            toast(R.string.toast_invalid_, etStart.getHint().toString());
            return false;
        } else if (etEnd.getText().length() == 0) {
            etEnd.startAnimation(shakeAnim);
            etEnd.requestFocus();
            toast(R.string.toast_invalid_, etEnd.getHint().toString());
            return false;
        } else if (etRepeat.getText().length() == 0) {
            etRepeat.startAnimation(shakeAnim);
            etRepeat.requestFocus();
            toast(R.string.toast_invalid_, etRepeat.getHint().toString());
            return false;
        } else if (etStartDate.getText().length() == 0) {
            etStartDate.startAnimation(shakeAnim);
            etStartDate.requestFocus();
            toast(R.string.toast_invalid_, etStartDate.getHint().toString());
            return false;
        } else if (etEndDate.getVisibility() == View.VISIBLE && etEndDate.getText().length() == 0) {
            etEndDate.startAnimation(shakeAnim);
            etEndDate.requestFocus();
            toast(R.string.toast_invalid_, etEndDate.getHint().toString());
            return false;
        }
//        else if (startDate.after(endDate)) {
//            if (etStartDate.getText().toString().equals(etEndDate.getText().toString())) {
//                etEndDate.startAnimation(shakeAnim);
//                etEndDate.requestFocus();
//                toast(R.string.toast_invalid_start_end_time);
//            } else {
//                etEnd.startAnimation(shakeAnim);
//                etEnd.requestFocus();
//                toast(R.string.toast_invalid_start_end_date);
//            }
//            return false;
//        }
        else if (etInvite.getText().length() == 0) {
            etInvite.startAnimation(shakeAnim);
            etInvite.requestFocus();
            toast(R.string.toast_invalid_, etInvite.getHint().toString());
            return false;
        } else if (etReminder.getText().length() == 0) {
            etReminder.startAnimation(shakeAnim);
            etReminder.requestFocus();
            toast(R.string.toast_invalid_, etReminder.getHint().toString());
            return false;
//        } else if (etNotes.getText().length() == 0) {
//            etNotes.startAnimation(shakeAnim);
//            etNotes.requestFocus();
//            toast(R.string.toast_invalid_, etNotes.getHint().toString());
//            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_treatment_info_llStart:
                isStartTime = true;
                mainActivity.showTimeDialog(this);
                break;
            case R.id.frg_treatment_info_llEnd:
                isStartTime = false;
                mainActivity.showTimeDialog(this);
                break;
            case R.id.frg_treatment_info_llRepeat:
                showDialog();
                break;
            case R.id.frg_treatment_info_llStartDate:
                isStartDate = true;
                mainActivity.showDateDialog(this);
                break;
            case R.id.frg_treatment_info_llEndDate:
                isStartDate = false;
                mainActivity.showDateDialog(startDate.get(Calendar.DAY_OF_MONTH), startDate.get(Calendar.MONTH), startDate.get(Calendar.YEAR), this);
                break;
            case R.id.frg_treatment_info_llInvitee:
//                TreatmentInviteeEmailDialog treatmentInviteeEmailDialog = new TreatmentInviteeEmailDialog();
//                treatmentInviteeEmailDialog.setTargetFragment(TreatmentAddFragment.this, 200);
//                Bundle bundle = new Bundle();
//                bundle.putCharSequenceArrayList("invitees", invitees);
//                treatmentInviteeEmailDialog.setArguments(bundle);
//                treatmentInviteeEmailDialog.show(getChildFragmentManager().beginTransaction(), "Add Invitee");

                setHasOptionsMenu(false);
                Fragment treatmentFragment = new TreatmentInviteeAddFragment();
                Bundle bundle = new Bundle();
                bundle.putCharSequenceArrayList("invitees", invitees);
                treatmentFragment.setArguments(bundle);
                treatmentFragment.setTargetFragment(TreatmentAddFragment.this, 200);
                mainActivity.addFragment(treatmentFragment, "Add Invitee");
                break;
            case R.id.frg_treatment_info_llReminder:
                ReminderDialog dialogFrag = new ReminderDialog(reminder);




                dialogFrag.show(getChildFragmentManager().beginTransaction(), "countryList");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setHasOptionsMenu(true);
        if (requestCode == 100) {
            if (data != null && data.getExtras() != null) {
                if (data.getExtras().getString("reminderName") != null && data.getExtras().getString("reminderID") != null) {
                    etReminder.setText(data.getExtras().getString("reminderName"));
                    selectedReminderMin = data.getExtras().getString("reminderID");
                }
            }
        } else if (requestCode == 200) {
            if (data != null && data.getExtras() != null) {
                if (data.getExtras().getCharSequenceArrayList("invitees") != null) {
                    invitees.clear();
                    invitees.addAll(data.getExtras().getCharSequenceArrayList("invitees"));
                    etInvite.setText(invitees.size() + "");
                }
            }
        }
        onResume();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gender_bloodgroup_dialog);

        ListView lvData = (ListView) dialog.findViewById(R.id.gender_bloodgroup_dialog_lv);
        ArrayAdapter adtItems = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.repeat));
        if (adtItems != null)
            lvData.setAdapter(adtItems);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etRepeat.setText(getResources().getStringArray(R.array.repeat)[position].toString());
                etEndDate.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onTimeClicked(TimePickerDialog dialog) {
        if (isStartTime) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            String selectedDate = dialog.getFormattedTime(format);
            startDate.set(Calendar.HOUR, dialog.getHour());
            startDate.set(Calendar.MINUTE, dialog.getMinute());
            etStart.setText("" + selectedDate);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            String selectedDate = dialog.getFormattedTime(format);
            endDate.set(Calendar.HOUR, dialog.getHour());
            endDate.set(Calendar.MINUTE, dialog.getMinute());
            etEnd.setText("" + selectedDate);
        }
    }

    @Override
    public void onPositiveActionClicked(DatePickerDialog dialog) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = dialog.getFormattedDate(format);
        if (isStartDate) {
            startDate.set(dialog.getYear(), dialog.getMonth(), dialog.getDay(), 0, 0, 0);
            etStartDate.setText(date.toString());
        } else {
            endDate.set(dialog.getYear(), dialog.getMonth(), dialog.getDay(), 0, 0, 0);
            etEndDate.setText(date.toString());
        }
    }
}