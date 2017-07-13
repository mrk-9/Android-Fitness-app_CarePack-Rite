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

import com.pooja.carepack.R;
import com.pooja.carepack.utils.MyPrefs;
import com.rey.material.widget.EditText;

import java.util.HashMap;

/**
 * Created by Yudiz on 23/12/15.
 */
public class SettingEmergencyFragment extends BaseFragment
{

    private View view;
    private Menu menu;
    private EditText etOraganDonation,etWearAndroidWatch;
    private MyPrefs prefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frg_settings_emergency,container,false);
        intiUI();
        setHasOptionsMenu(true);
        return view;

    }

    private void intiUI() {
        prefs=new MyPrefs(getActivity());
        etOraganDonation=(EditText)view.findViewById(R.id.frg_emer_settings_etOraganDonation);
        etWearAndroidWatch=(EditText)view.findViewById(R.id.frg_emer_settings_etCollapseWearingWatch);
    }

    private void onSaveDetails() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "submit_body_settings");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
//        hashMap.put("height", etHeight.getText().toString());
//        hashMap.put("weight", etWeight.getText().toString());
//        hashMap.put("arm_left", etArmL.getText().toString());
//        hashMap.put("arm_right", etArmR.getText().toString());
//        hashMap.put("hip", etHip.getText().toString());
//        hashMap.put("waist", etWaist.getText().toString());
//        hashMap.put("chest", etChest.getText().toString());
//        hashMap.put("neck", etNeck.getText().toString());
//        hashMap.put("abdominal", etAbdominal.getText().toString());
//        hashMap.put("thigh_left", etThighL.getText().toString());
//        hashMap.put("thigh_right", etThighR.getText().toString());
//        hashMap.put("calf_left", etCalfL.getText().toString());
//        hashMap.put("calf_right", etCalfR.getText().toString());
//        hashMap.put("dress_size", etDressSize.getText().toString());
//        hashMap.put("body_fat_percent", etBodyFat.getText().toString());
//
//        new PostLibResponse(SettingBodyFragment.this, new ModelBodySettings(), getActivity(), hashMap, WebServicesConst.UPDATE_BODY_SETTING, WebServicesConst.RES.UPDATE_BODY_SETTING, true, true);
    }

    public int menu_item = 0;
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