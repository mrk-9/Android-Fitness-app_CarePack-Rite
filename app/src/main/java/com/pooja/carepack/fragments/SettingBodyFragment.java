package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelBodySettings;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class SettingBodyFragment extends BaseFragment implements LibPostListner, View.OnFocusChangeListener {

    public int menu_item = 0;
    private View view;
    private Menu menu;
    private EditText etHeight, etWeight, etBodyFat, etWaist, etHip, etChest, etAbdominal, etNeck, etCalfL, etCalfR, etArmL, etArmR, etThighL, etThighR, etDressSize;
    private MyPrefs prefs;
    private ModelBodySettings modelBodySettings;
    private Animation shakeAnim;
    private ImageView ivBody;
    private int gender = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            gender = bundle.getInt("gender");
            android.util.Log.e("TAG", "GENDER   " + gender);
        }
        if (gender == 0)
            view = inflater.inflate(R.layout.frg_setting_body_female, container, false);
        else
            view = inflater.inflate(R.layout.frg_setting_body_male, container, false);

        intiUI();
//        if (prefs.get(MyPrefs.keys.GENDER).equals("female"))
        if (!prefs.get(MyPrefs.keys.HEIGHT).equals("")) {
            if (etHeight.getText().length() == 0) {
                float height = Float.parseFloat(prefs.get(MyPrefs.keys.HEIGHT)) * 100;
                etHeight.setText(String.valueOf(height));
            }
        }
        if (!prefs.get(MyPrefs.keys.WEIGHT).equals("")) {
            if (etWeight.getText().length() == 0)
                etWeight.setText(prefs.get(MyPrefs.keys.WEIGHT));
        }

        onGetDetails();
        setHasOptionsMenu(true);
        etWaist.setOnFocusChangeListener(this);
        etHeight.setOnFocusChangeListener(this);
        etNeck.setOnFocusChangeListener(this);
        etHip.setOnFocusChangeListener(this);
        return view;
    }


    private void intiUI() {
        prefs = new MyPrefs(getActivity());
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        etHeight = (EditText) view.findViewById(R.id.frg_setting_body_etHeight);
        etWeight = (EditText) view.findViewById(R.id.frg_setting_body_etWeight);
        etBodyFat = (EditText) view.findViewById(R.id.frg_setting_body_etBodyFat);
        etWaist = (EditText) view.findViewById(R.id.frg_setting_body_etWaist);
        etHip = (EditText) view.findViewById(R.id.frg_setting_body_etHip);
        etChest = (EditText) view.findViewById(R.id.frg_setting_body_etChest);
        etAbdominal = (EditText) view.findViewById(R.id.frg_setting_body_etAbdonimal);
        etNeck = (EditText) view.findViewById(R.id.frg_setting_body_etNeck);
        etCalfL = (EditText) view.findViewById(R.id.frg_setting_body_etCalfL);
        etCalfR = (EditText) view.findViewById(R.id.frg_setting_body_etCalfR);
        etArmL = (EditText) view.findViewById(R.id.frg_setting_body_etArmL);
        etArmR = (EditText) view.findViewById(R.id.frg_setting_body_etArmR);
        etThighL = (EditText) view.findViewById(R.id.frg_setting_body_etThighL);
        etThighR = (EditText) view.findViewById(R.id.frg_setting_body_etThighR);
        etDressSize = (EditText) view.findViewById(R.id.frg_setting_body_etDressSize);
        ivBody = (ImageView) view.findViewById(R.id.frg_setting_body_ivBody);

//        FrameLayout fl = (FrameLayout) view.findViewById(R.id.body_setting_scroll);
        LinearLayout fl1 = (LinearLayout) view.findViewById(R.id.body_setting_scroll1);
//        LinearLayout fl2 = (LinearLayout) view.findViewById(R.id.body_setting_scroll2);
//        int height = Utility.getScreenSize(getActivity()).y - getStatusBarHeight();
//        fl.getLayoutParams().height = height;
//        fl1.getLayoutParams().height = height;
        fl1.getLayoutParams().height = (int) (Utility.getScreenSize(getActivity()).x * 1.31);
//        fl2.getLayoutParams().height = height;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        result += (int) styledAttributes.getDimension(0, 0);
        result += 10;
        styledAttributes.recycle();
        return result;
    }

    private void setBodyFat() {
        Log.d("set body fat call======");
        double bodyFat;
        float waist = Float.parseFloat(etWaist.getText().toString());
        float neck = Float.parseFloat(etNeck.getText().toString());
        float height = Float.parseFloat(etHeight.getText().toString());
        float hip = Float.parseFloat(etHip.getText().toString());
        if (waist > 0 && neck > 0 && height > 50) {
            if (gender == 1) {
//            495/(1.0324-0.19077(LOG(waist-neck))+0.15456(LOG(height)))-450  //for male
//                495/(1.0324-0.19077*(Math.log10(waist - neck))+0.15456*(Math.log10(height)))-450;
                bodyFat = 495 / (1.0324 - 0.19077 * (Math.log10(waist - neck)) + 0.15456 * (Math.log10(height))) - 450;//495 / ((1.0324 - (0.19077 * (Math.log10(waist - neck)))) + ((0.15456 * (Math.log10(height)))) - 450);
                android.util.Log.d("TAG", "BODY FAT   " + bodyFat);
                etBodyFat.setText(new DecimalFormat("#.##").format(bodyFat));
            } else if (gender == 0) {
//                495/(1.29579-0.35004(LOG(waist+hip-neck))+0.22100(LOG(height)))-450
//                495/((1.29579-(0.35004(LOG(waist+hip-neck))))+((0.22100(LOG(height))))-450);
//                495/(1.29579-0.35004*(Math.log10(waist+hip-neck))+0.22100*(Math.log10(height)))-450;
                if (hip > 0) {
                    bodyFat = 495 / (1.29579 - 0.35004 * (Math.log10(waist + hip - neck)) + 0.22100 * (Math.log10(height))) - 450;//495 / ((1.29579 - (0.35004 * (Math.log10(waist + hip - neck)))) + ((0.22100 * (Math.log10(height)))) - 450);
                    android.util.Log.d("TAG", "BODY FAT   " + bodyFat);
                    etBodyFat.setText(new DecimalFormat("#.##").format(bodyFat));
                }
            }
        }
    }


    private void onGetDetails() {
        String isOnline = prefs.get(MyPrefs.keys.ISONLINE);
//        if(isOnline.equals("1")) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        new PostLibResponse(SettingBodyFragment.this, new ModelBodySettings(), getActivity(), hashMap, WebServicesConst.BODY_SETTING + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.GET_BODY_SETTING, true, true);
//        }
//        else{
//            onPostResponseComplete(new Gson().fromJson(prefs.get(WebServicesConst.BODY_SETTING + "/" + prefs.get(MyPrefs.keys.ID)), ModelBodySettings.class),WebServicesConst.RES.GET_BODY_SETTING);
//        }
    }

    private void setData() {
        Log.d("get body setting data   " + modelBodySettings.body_settings.height.toString());

        prefs.set(MyPrefs.keys.HEIGHT, "" + modelBodySettings.body_settings.height.toString());
        prefs.set(MyPrefs.keys.WEIGHT, "" + modelBodySettings.body_settings.weight.toString());
        etHeight.setText(modelBodySettings.body_settings.height.toString());
        etWeight.setText(modelBodySettings.body_settings.weight.toString());
        etBodyFat.setText(modelBodySettings.body_settings.body_fat_percent.toString());
        etWaist.setText(modelBodySettings.body_settings.waist.toString());
        etHip.setText(modelBodySettings.body_settings.hip.toString());
        etChest.setText(modelBodySettings.body_settings.chest);
        etAbdominal.setText(modelBodySettings.body_settings.abdominal);
        etNeck.setText(modelBodySettings.body_settings.neck);
        etCalfL.setText(modelBodySettings.body_settings.calf_left);
        etCalfR.setText(modelBodySettings.body_settings.calf_right);
        etArmL.setText(modelBodySettings.body_settings.arm_left);
        etArmR.setText(modelBodySettings.body_settings.arm_right);
        etThighL.setText(modelBodySettings.body_settings.thigh_left);
        etThighR.setText(modelBodySettings.body_settings.thigh_right);
        etDressSize.setText(modelBodySettings.body_settings.dress_size);

    }

    private void onSaveDetails() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "submit_body_settings");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("height", etHeight.getText().toString());
        hashMap.put("weight", etWeight.getText().toString());
        hashMap.put("arm_left", etArmL.getText().toString());
        hashMap.put("arm_right", etArmR.getText().toString());
        hashMap.put("hip", etHip.getText().toString());
        hashMap.put("waist", etWaist.getText().toString());
        hashMap.put("chest", etChest.getText().toString());
        hashMap.put("neck", etNeck.getText().toString());
        hashMap.put("abdominal", etAbdominal.getText().toString());
        hashMap.put("thigh_left", etThighL.getText().toString());
        hashMap.put("thigh_right", etThighR.getText().toString());
        hashMap.put("calf_left", etCalfL.getText().toString());
        hashMap.put("calf_right", etCalfR.getText().toString());
        hashMap.put("dress_size", etDressSize.getText().toString());
        hashMap.put("body_fat_percent", etBodyFat.getText().toString());

        new PostLibResponse(SettingBodyFragment.this, new ModelBodySettings(), getActivity(), hashMap, WebServicesConst.BODY_SETTING, WebServicesConst.RES.UPDATE_BODY_SETTING, true, true);
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
                    onSaveDetails();
                break;
            default:
                break;
        }
        return true;
    }

    private boolean valid() {
        if (etHeight.getText().length() > 0 && etWeight.getText().length() > 0) {
            float height = Float.parseFloat(etHeight.getText().toString());
            float weight = Float.parseFloat(etWeight.getText().toString());
            android.util.Log.d("TAG", "VALID HW   " + height + "   " + weight);
            if (height < 50) {
                etHeight.startAnimation(shakeAnim);
                etHeight.requestFocus();
                toast(R.string.toast_invalid_height, etHeight.getHint().toString());
                return false;
            } else if (weight < 20) {
                etWeight.startAnimation(shakeAnim);
                etWeight.requestFocus();
                toast(R.string.toast_invalid_weight, etWeight.getHint().toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelBodySettings = (ModelBodySettings) clsGson;

        if (requestCode == WebServicesConst.RES.UPDATE_BODY_SETTING) {
            if (modelBodySettings != null) {
                int Status = modelBodySettings.status;
                if (Status == 200) {
                    prefs.set(MyPrefs.keys.HEIGHT, modelBodySettings.body_settings.height);
                    prefs.set(MyPrefs.keys.WEIGHT, modelBodySettings.body_settings.weight);
                    Log.d("get body setting data   " + modelBodySettings.body_settings.height + "  " + modelBodySettings.body_settings.arm_left);
//                    toast(modelBodySettings.message.toString());
                    getActivity().onBackPressed();
                } else {
                    toast(modelBodySettings.message.toString());
                }
            }
        } else if (requestCode == WebServicesConst.RES.GET_BODY_SETTING) {
            if (modelBodySettings != null) {

                int Status = modelBodySettings.status;
                if (Status == 200) {
//                    prefs.set(WebServicesConst.BODY_SETTING + "/" + prefs.get(MyPrefs.keys.ID),new Gson().toJson(modelBodySettings));
                    setData();
                    Log.d("get body setting data   " + modelBodySettings.body_settings.arm_left);
                } else {
                    toast(modelBodySettings.message.toString());
                }
            }
        }

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setBodyFat();
    }
}
