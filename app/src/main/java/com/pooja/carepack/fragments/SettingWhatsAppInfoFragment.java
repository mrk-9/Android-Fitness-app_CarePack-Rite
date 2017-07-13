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
import com.pooja.carepack.model.ModelWhatsappInfo;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.widget.EditText;

import java.util.HashMap;

/**
 * Created by Yudiz on 24/12/15.
 */
public class SettingWhatsAppInfoFragment extends BaseFragment implements LibPostListner {
    public int menu_item = 0;
    private EditText etWhatsappNumber;
    private CheckBox cbWhatsappWarning;
    private View view;
    private ModelWhatsappInfo modelWhatsappInfo;
    private String notificationStatus;
    private Menu menu;
    private Animation shakeAnim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_setting_whatsapp_info, container, false);
        intiUI();
        onGetDetails();
        setHasOptionsMenu(true);
        return view;
    }

    private void intiUI() {
        etWhatsappNumber = (EditText) view.findViewById(R.id.frg_whatsapp_info_etWhatsappNumber);
        cbWhatsappWarning = (CheckBox) view.findViewById(R.id.frg_whatsapp_info_switchEmerWarning);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }

    private void onGetDetails() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        new PostLibResponse(SettingWhatsAppInfoFragment.this, new ModelWhatsappInfo(), getActivity(), hashMap, WebServicesConst.WHATSAPP_INFO + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.GET_WHATSAPP_INFO, true, true);
    }

    private void setData() {
        etWhatsappNumber.setText(modelWhatsappInfo.getWhatsapp().getWhatsappNumber());
        notificationStatus = modelWhatsappInfo.getWhatsapp().getNotification();
        if (notificationStatus.equals("y"))
            cbWhatsappWarning.setChecked(true);
        else
            cbWhatsappWarning.setChecked(false);
    }

    private void onSaveDetails() {
        if (valid()) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            if (cbWhatsappWarning.isChecked())
                notificationStatus = "y";
            else
                notificationStatus = "n";
            hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
            hashMap.put("action", "submit_whatsapp_details");
            hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
            hashMap.put("whatsapp_number", etWhatsappNumber.getText().toString());
            hashMap.put("notification", notificationStatus);
            new PostLibResponse(SettingWhatsAppInfoFragment.this, new ModelWhatsappInfo(), getActivity(), hashMap, WebServicesConst.WHATSAPP_INFO, WebServicesConst.RES.UPDATE_WHATSAPP_INFO, true, true);
        }
    }

    private boolean valid() {
        if (etWhatsappNumber.getText().length() < 10) {
            etWhatsappNumber.startAnimation(shakeAnim);
            etWhatsappNumber.requestFocus();
            toast(R.string.toast_invalid_phone);
            return false;
        }
        return true;
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelWhatsappInfo = (ModelWhatsappInfo) clsGson;
        if (requestCode == WebServicesConst.RES.UPDATE_WHATSAPP_INFO) {
            if (modelWhatsappInfo != null) {
                int Status = modelWhatsappInfo.getStatus();
                if (Status == 200) {
                    getActivity().onBackPressed();
                } else {
                    toast(modelWhatsappInfo.getMessage().toString());
                }
            }
        } else if (requestCode == WebServicesConst.RES.GET_WHATSAPP_INFO) {
            if (modelWhatsappInfo != null) {
                int Status = modelWhatsappInfo.getStatus();
                if (Status == 200) {
                    setData();
                } else {
                    toast(modelWhatsappInfo.getMessage().toString());
                }
            }
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

}
