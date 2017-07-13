package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.pooja.carepack.model.ModelTabletRegalationInfo;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TabletRegalationAddFragment extends BaseFragment implements LibPostListner, View.OnClickListener, MainActivity.OnTimeDialogListener {

    public int menu_item = 0;
    private View view;
    private Menu menu;
    private String tabletID;
    private String userTabletID = "0";
    private EditText etDescription, etName, etDose, etNumber, etMG, etMorningTime, etLunchTime, etNightTime;
    private Animation shakeAnim;
    private LinearLayout llMorningTime, llLunchTime, llNightTime;
    private int stat;
    private ModelTabletRegalationInfo modelTabletRegalationInfo;
    private LinearLayout llMG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_tablet_regulation_add, container, false);
        setHasOptionsMenu(true);
        intiUI();
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Log.d(" in bundle");
                tabletID = bundle.getString("tabletID");
                userTabletID = bundle.getString("UserTabletID", "0");
                Log.d(" in bundle " + tabletID + " " + userTabletID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tabletID != null)
            getTabletInfo();
        if (!userTabletID.equals("0")) {
            Log.d("User vac id add frg  " + userTabletID);
            getUserTabletInfo();
        }
        return view;
    }


    private void intiUI() {
        etDescription = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etDesc);
        etName = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etMedicalName);
        etDose = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etDose);
        etNumber = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etNumber);
        etMG = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etMG);
        etMorningTime = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etMorningTime);
        etLunchTime = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etLunchTime);
        etNightTime = (EditText) view.findViewById(R.id.frg_tablet_regulation_add_etNightTime);

        llMorningTime = (LinearLayout) view.findViewById(R.id.frg_tablet_regulation_add_llMorningTime);
        llLunchTime = (LinearLayout) view.findViewById(R.id.frg_tablet_regulation_add_llLunchTime);
        llNightTime = (LinearLayout) view.findViewById(R.id.frg_tablet_regulation_add_llNightTime);
        llMG=(LinearLayout)view.findViewById(R.id.frg_tablet_regulation_add_llMG);

        llMorningTime.setOnClickListener(this);
        llLunchTime.setOnClickListener(this);
        llNightTime.setOnClickListener(this);
        llMG.setOnClickListener(this);

        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }


    private void getUserTabletInfo() {
        String url = "" + WebServicesConst.USER_TABLE_REGULATION + "/" + prefs.get(MyPrefs.keys.ID) + "/" + userTabletID;
        new PostLibResponse(TabletRegalationAddFragment.this, new ModelTabletRegalationInfo(), getActivity(), new HashMap<String, String>(), url, WebServicesConst.RES.GET_USER_TABLE_REGULATION, true, true);
    }

    private void getTabletInfo() {
        String url = "" + WebServicesConst.TABLE_REGULATION + "/" + tabletID;
        Log.d("url " + url);
        new PostLibResponse(TabletRegalationAddFragment.this, new ModelTabletRegalationInfo(), getActivity(), new HashMap<String, String>(), url, WebServicesConst.RES.GET_TABLE_REGULATION, true, true);
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
                if (valid(etDescription, etName, etDose, etNumber, etMG, etMorningTime, etLunchTime, etNightTime))
                    saveTabletInfo();
                break;
            default:
                break;
        }
        return true;
    }

    private boolean valid(EditText... ets) {
        for (EditText et : ets) {
            if (et.getText().length() == 0) {
                et.startAnimation(shakeAnim);
                et.requestFocus();
                toast(R.string.toast_invalid_, et.getHint().toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_tablet_regulation_add_llMorningTime:
                showTimeDialog(0);
                break;
            case R.id.frg_tablet_regulation_add_llLunchTime:
                showTimeDialog(1);
                break;
            case R.id.frg_tablet_regulation_add_llNightTime:
                showTimeDialog(2);
                break;
            case R.id.frg_tablet_regulation_add_llMG:
                showMGDialog();
                break;
        }
    }

    private void showTimeDialog(final int stat) {
        this.stat = stat;
        mainActivity.showTimeDialog(TabletRegalationAddFragment.this);
    }


    @Override
    public void onTimeClicked(TimePickerDialog dialog) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        String selectedDate = dialog.getFormattedTime(format);
        if (stat == 0)
            etMorningTime.setText(selectedDate);
        else if (stat == 1)
            etLunchTime.setText(selectedDate);
        else
            etNightTime.setText(selectedDate);
    }

    private void saveTabletInfo() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("action", "submit_user_tablet");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", userTabletID);
        hashMap.put("title", etName.getText().toString());
        hashMap.put("description", etDescription.getText().toString());
        hashMap.put("dose", etDose.getText().toString());
        hashMap.put("mg", etMG.getText().toString());
        hashMap.put("number", etNumber.getText().toString());

        hashMap.put("morning", etMorningTime.getText().toString());
        hashMap.put("lunch", etLunchTime.getText().toString());
        hashMap.put("night", etNightTime.getText().toString());
        new PostLibResponse(TabletRegalationAddFragment.this, new ModelTabletRegalationInfo(), getActivity(), hashMap, WebServicesConst.USER_TABLE_REGULATION, WebServicesConst.RES.ADD_TABLE_REGULATION, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.GET_TABLE_REGULATION || requestCode == WebServicesConst.RES.GET_USER_TABLE_REGULATION) {
            modelTabletRegalationInfo = (ModelTabletRegalationInfo) clsGson;
            if (modelTabletRegalationInfo != null) {
                int status = modelTabletRegalationInfo.status;
                if (status == 200) {
                    setData();
                } else {
                    toast(modelTabletRegalationInfo.message);
                }
            }
        } else if (requestCode == WebServicesConst.RES.ADD_TABLE_REGULATION) {
            modelTabletRegalationInfo = (ModelTabletRegalationInfo) clsGson;
            if (modelTabletRegalationInfo != null) {
                int status = modelTabletRegalationInfo.status;
                if (status == 200) {
                    toast(modelTabletRegalationInfo.message);
                    if (getFragmentManager().getBackStackEntryCount() == 2)
                        mainActivity.onDoubleBackPressed();
                    else
                        mainActivity.onBackPressed();
                } else {
                    toast(modelTabletRegalationInfo.message);
                }
            }
        }
    }

    private void setData() {

        etDescription.setText(modelTabletRegalationInfo.info.description);
        etName.setText(modelTabletRegalationInfo.info.title);
        etDose.setText(modelTabletRegalationInfo.info.dose);
        etNumber.setText(modelTabletRegalationInfo.info.number);
        etMG.setText(modelTabletRegalationInfo.info.mg);

//        String morningTime = Utility.getFormatedDate(modelTabletRegalationInfo.info.morning, "yyyy-MM-dd", "yyyy-MM-dd");
        etMorningTime.setText(modelTabletRegalationInfo.info.morning);

//        String lunchTime = Utility.getFormatedDate(modelTabletRegalationInfo.info.lunch, "yyyy-MM-dd", "yyyy-MM-dd");
        etLunchTime.setText(modelTabletRegalationInfo.info.lunch);

//        String nightTime = Utility.getFormatedDate(modelTabletRegalationInfo.info.night, "yyyy-MM-dd", "yyyy-MM-dd");
        etNightTime.setText(modelTabletRegalationInfo.info.night);
    }


    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    private void showMGDialog() {
        ArrayAdapter adtItems = null;
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gender_bloodgroup_dialog);

        ListView lvData = (ListView) dialog.findViewById(R.id.gender_bloodgroup_dialog_lv);
        adtItems = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tablet_unit));
        if (adtItems != null)
            lvData.setAdapter(adtItems);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    etMG.setText(getResources().getStringArray(R.array.tablet_unit)[position].toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
