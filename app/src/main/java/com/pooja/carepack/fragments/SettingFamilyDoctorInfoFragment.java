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
import android.widget.CheckBox;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelFamilyDoctorInfo;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.widget.EditText;

import java.util.HashMap;

/**
 * Created by Yudiz on 23/12/15.
 */
public class SettingFamilyDoctorInfoFragment extends BaseFragment implements LibPostListner {

    public int menu_item = 0;
    private View view;
    private Menu menu;
    private MyPrefs prefs;
    private EditText etFirstname, etLastname, etemail, etAddress, etPhone, etEmergency;
    private ModelFamilyDoctorInfo modelFamilyDoctorInfo;
    private CheckBox cbSendGPS;
    private String SendGPSValue;
    private Animation shakeAnim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_setting_family_doctor_info, container, false);
        intiUI();
        onGetDetails();
        setHasOptionsMenu(true);
        return view;
    }

    private void intiUI() {
        prefs = new MyPrefs(getActivity());
        etFirstname = (EditText) view.findViewById(R.id.frg_family_doctor_info_etFirstName);
        etLastname = (EditText) view.findViewById(R.id.frg_family_doctor_info_etLastName);
        etEmergency = (EditText) view.findViewById(R.id.frg_family_doctor_info_etEmergencyCall);
        etemail = (EditText) view.findViewById(R.id.frg_family_doctor_info_etEmail);
        etAddress = (EditText) view.findViewById(R.id.frg_family_doctor_info_etAddress);
        etPhone = (EditText) view.findViewById(R.id.frg_family_doctor_info_etPhoneNumber);
//        etSendGPS = (EditText) view.findViewById(R.id.frg_family_doctor_info_etSendGPS);
        cbSendGPS = (CheckBox) view.findViewById(R.id.frg_family_doctor_info_tbSendGPS);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }

    private void onGetDetails() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        new PostLibResponse(SettingFamilyDoctorInfoFragment.this, new ModelFamilyDoctorInfo(), getActivity(), hashMap, WebServicesConst.FAMILY_DOCTOR_INFO + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.GET_FAMILY_DOCTOR_INFO, true, true);
    }

    public void setData() {
        etFirstname.setText(modelFamilyDoctorInfo.getFamilyDoctor().getFirstname());
        etLastname.setText(modelFamilyDoctorInfo.getFamilyDoctor().getLastname());
        etEmergency.setText(modelFamilyDoctorInfo.getFamilyDoctor().getEmergency());
        etemail.setText(modelFamilyDoctorInfo.getFamilyDoctor().getEmail());
        etAddress.setText(modelFamilyDoctorInfo.getFamilyDoctor().getAddress());
        etPhone.setText(modelFamilyDoctorInfo.getFamilyDoctor().getPhone());
        SendGPSValue = modelFamilyDoctorInfo.getFamilyDoctor().getSendGps();
        if (SendGPSValue.equals("y"))
            cbSendGPS.setChecked(true);
        else
            cbSendGPS.setChecked(false);
    }

    private void onSaveDetails() {
        if (valid()) {
            if (cbSendGPS.isChecked())
                SendGPSValue = "y";
            else
                SendGPSValue = "n";

            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
            hashMap.put("action", "submit_family_doctor");
            hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
            hashMap.put("firstname", etFirstname.getText().toString());
            hashMap.put("lastname", etLastname.getText().toString());
            hashMap.put("email", etemail.getText().toString());
            hashMap.put("phone", etPhone.getText().toString());
            hashMap.put("send_gps", SendGPSValue);
            hashMap.put("emergency", etEmergency.getText().toString());
            hashMap.put("address", etAddress.getText().toString());

            new PostLibResponse(SettingFamilyDoctorInfoFragment.this, new ModelFamilyDoctorInfo(), getActivity(), hashMap, WebServicesConst.FAMILY_DOCTOR_INFO, WebServicesConst.RES.UPDATE_FAMILY_DOCTOR_INFO, true, true);
        }
    }

    private boolean valid() {
        if (etEmergency.getText().length() < 10) {
            etEmergency.startAnimation(shakeAnim);
            etEmergency.requestFocus();
            toast(R.string.toast_invalid_phone);
            return false;
        } else if (etFirstname.getText().length() == 0) {
            etFirstname.startAnimation(shakeAnim);
            etFirstname.requestFocus();
            toast(R.string.toast_invalid_,etFirstname.getHint().toString());
            return false;
        } else if (etLastname.getText().length() == 0) {
            etLastname.startAnimation(shakeAnim);
            etLastname.requestFocus();
            toast(R.string.toast_invalid_,etLastname.getHint().toString());
            return false;
        } else if (etAddress.getText().length() == 0) {
            etAddress.startAnimation(shakeAnim);
            etAddress.requestFocus();
            toast(R.string.toast_invalid_,etAddress.getHint().toString());
            return false;
        } else if (etPhone.getText().length() < 10) {
            etPhone.startAnimation(shakeAnim);
            etPhone.requestFocus();
            toast(R.string.toast_invalid_phone);
            return false;
        } else if (etemail.getText().length() == 0 || !Utility.isEmailValid(etemail.getText().toString())) {
            etemail.startAnimation(shakeAnim);
            etemail.requestFocus();
            toast(R.string.toast_invalid_email);
            return false;
        }
        return true;
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
                onSaveDetails();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelFamilyDoctorInfo = (ModelFamilyDoctorInfo) clsGson;
        if (requestCode == WebServicesConst.RES.UPDATE_FAMILY_DOCTOR_INFO) {
            if (modelFamilyDoctorInfo != null) {
                int Status = modelFamilyDoctorInfo.getStatus();
                if (Status == 200) {
                    toast(modelFamilyDoctorInfo.getMessage().toString());
                    getActivity().onBackPressed();
                } else {
                    toast(modelFamilyDoctorInfo.getMessage().toString());
                }
            }
        } else if (requestCode == WebServicesConst.RES.GET_FAMILY_DOCTOR_INFO) {
            if (modelFamilyDoctorInfo != null) {
                int Status = modelFamilyDoctorInfo.getStatus();
                if (Status == 200) {
//                    toast(modelFamilyDoctorInfo.getMessage().toString());
                    setData();
                    Log.d("get family doctoe info  data   " + modelFamilyDoctorInfo.getFamilyDoctor().getFirstname());
                } else {
                    toast(modelFamilyDoctorInfo.getMessage().toString());
                }
            }
        }

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
