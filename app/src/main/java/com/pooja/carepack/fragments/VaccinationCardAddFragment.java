package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelUserVaccinationList;
import com.pooja.carepack.model.ModelVaccinationCardInfo;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Yudiz on 28/12/15.
 */
public class VaccinationCardAddFragment extends BaseFragment implements LibPostListner, View.OnClickListener, MainActivity.OnDateDialogListener {

    public int menu_item = 0;
    private View view;
    private String vaccinationID, userVaccinationID;
    private ModelVaccinationCardInfo modelVaccinationInfo;
    private EditText etDescription, etName, etVaccineDate, etRemiderMeDate;
    private MyPrefs prefs;
    private String addEditStatus = "0";
    private Menu menu;
    private LinearLayout llDate, llReminderDate;
    private com.rey.material.app.Dialog.Builder builder = null;
    private ModelUserVaccinationList modelUserVaccinationList;
    private int stat;
    private Animation shakeAnim;
    private Calendar vaccinationDate = Calendar.getInstance();
    private Calendar reminderDate = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_vaccination_info_add, container, false);
        intiUI();
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Log.d(" in bundle");
                vaccinationID = bundle.getString("VaccinationID");
                userVaccinationID = bundle.getString("UserVaccinationID");
                addEditStatus = userVaccinationID;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vaccinationID != null)
            getVaccinationInfo();
        if (userVaccinationID != null) {
            Log.d("User vac id add frg  " + userVaccinationID);
            getUserVaccinationInfo();
        }
        setHasOptionsMenu(true);
        return view;
    }

    private void getUserVaccinationInfo() {
        Log.d("call getUserVaccinationInfo()");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(VaccinationCardAddFragment.this, new ModelVaccinationCardInfo(), getActivity(), hashMap, WebServicesConst.USER_VACCINATION_LIST + "/" + prefs.get(MyPrefs.keys.ID) + "/" + userVaccinationID, WebServicesConst.RES.GET_USER_VACCINATION_INFO, true, true);
    }

    private void getVaccinationInfo() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(VaccinationCardAddFragment.this, new ModelVaccinationCardInfo(), getActivity(), hashMap, WebServicesConst.VACCINATION_LIST + "/" + vaccinationID, WebServicesConst.RES.GET_VACCINATION_INFO, true, true);
    }

    private void intiUI() {
        prefs = new MyPrefs(getActivity());
        etDescription = (EditText) view.findViewById(R.id.frg_add_vaccination_info_tvDesc);
        etVaccineDate = (EditText) view.findViewById(R.id.frg_add_vaccination_info_etVaccineDate);
        etName = (EditText) view.findViewById(R.id.frg_add_vaccination_info_etName);
        etRemiderMeDate = (EditText) view.findViewById(R.id.frg_add_vaccination_info_etReminderMeDate);

        llDate = (LinearLayout) view.findViewById(R.id.frg_add_vaccination_info_llDate);
        llReminderDate = (LinearLayout) view.findViewById(R.id.frg_add_vaccination_info_llReminderMeDate);

        llReminderDate.setOnClickListener(this);
        llDate.setOnClickListener(this);

        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }


    private void SaveVaccinationInfo() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Log.d("vac id status  " + addEditStatus);
        hashMap.put("action", "submit_user_vaccination");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", addEditStatus);
        hashMap.put("title", etName.getText().toString());
        hashMap.put("description", etDescription.getText().toString());
        hashMap.put("vaccine_date", etVaccineDate.getText().toString());
        hashMap.put("reminder", etRemiderMeDate.getText().toString());

        new PostLibResponse(VaccinationCardAddFragment.this, new ModelUserVaccinationList(), getActivity(), hashMap, WebServicesConst.USER_VACCINATION_LIST, WebServicesConst.RES.ADD_EDIT_USER_VACCINATION_LIST, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.GET_VACCINATION_INFO || requestCode == WebServicesConst.RES.GET_USER_VACCINATION_INFO) {
            modelVaccinationInfo = (ModelVaccinationCardInfo) clsGson;
            if (modelVaccinationInfo != null) {
                int status = modelVaccinationInfo.status;
                if (status == 200) {
                    setData();
                } else {
                    toast(modelVaccinationInfo.message);
                }
            }
        } else if (requestCode == WebServicesConst.RES.ADD_EDIT_USER_VACCINATION_LIST) {
            modelUserVaccinationList = (ModelUserVaccinationList) clsGson;
            if (modelUserVaccinationList != null) {
                int status = modelUserVaccinationList.getStatus();
                if (status == 200) {
                    toast(modelUserVaccinationList.getMessage());
                    getActivity().onBackPressed();
                } else {
                    toast(modelUserVaccinationList.getMessage());
                }
            }
        }
    }

    private void setData() {
        etDescription.setText(modelVaccinationInfo.info.description);
        etName.setText(modelVaccinationInfo.info.title);
        String vaccine_data = Utility.getFormatedDate(modelVaccinationInfo.info.vaccine_date, "yyyy-MM-dd", "yyyy-MM-dd");
        etVaccineDate.setText(vaccine_data);

        String reminder = Utility.getFormatedDate(modelVaccinationInfo.info.reminder, "yyyy-MM-dd", "yyyy-MM-dd");
        etRemiderMeDate.setText(reminder);
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
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
                if (valid())
                    SaveVaccinationInfo();
                break;
            default:
                break;
        }
        return true;
    }

    private boolean valid() {
        if (etDescription.getText().length() == 0) {
            etDescription.startAnimation(shakeAnim);
            etDescription.requestFocus();
            toast(R.string.toast_invalid_, etDescription.getHint().toString());
            return false;
        } else if (etName.getText().length() == 0) {
            etName.startAnimation(shakeAnim);
            etName.requestFocus();
            toast(R.string.toast_invalid_, etName.getHint().toString());
            return false;
        } else if (etVaccineDate.getText().length() == 0) {
            etVaccineDate.startAnimation(shakeAnim);
            etVaccineDate.requestFocus();
            toast(R.string.toast_invalid_, etVaccineDate.getHint().toString());
            return false;
        } else if (etRemiderMeDate.getText().length() == 0) {
            etRemiderMeDate.startAnimation(shakeAnim);
            etRemiderMeDate.requestFocus();
            toast(R.string.toast_invalid_, etRemiderMeDate.getHint().toString());
            return false;
        } else if (vaccinationDate.after(reminderDate)) {
            etRemiderMeDate.startAnimation(shakeAnim);
            etRemiderMeDate.requestFocus();
            toast(R.string.toast_invalid_reminder_and_vaccination);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_add_vaccination_info_llDate:
                this.stat = 0;
                mainActivity.showDateDialog(0,VaccinationCardAddFragment.this);
                break;
            case R.id.frg_add_vaccination_info_llReminderMeDate:
                this.stat = 1;
                mainActivity.showDateDialog(vaccinationDate.get(Calendar.DAY_OF_MONTH), vaccinationDate.get(Calendar.MONTH), vaccinationDate.get(Calendar.YEAR), this);
                break;
        }
    }


    @Override
    public void onPositiveActionClicked(DatePickerDialog dialog) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = dialog.getFormattedDate(format);

        if (stat == 0) {
            vaccinationDate.set(dialog.getYear(), dialog.getMonth(), dialog.getDay(), 0, 0, 0);
            etVaccineDate.setText(selectedDate);
        } else {
            reminderDate.set(dialog.getYear(), dialog.getMonth(), dialog.getDay(), 0, 0, 0);
            etRemiderMeDate.setText(selectedDate);
        }
    }
}
