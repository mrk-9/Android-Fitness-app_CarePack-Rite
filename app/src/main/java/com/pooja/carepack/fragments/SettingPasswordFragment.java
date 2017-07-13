package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelLogin;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.widget.EditText;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class SettingPasswordFragment extends BaseFragment implements LibPostListner {

    public int menu_item = 0;
    private View view;
    private EditText etCurrent, etNew, etConfirm;
    private ModelLogin modelLogin;
    private Menu menu;
    private Animation shakeAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_setting_change_password, container, false);
        intiUI(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void intiUI(View view) {
        etCurrent = (EditText) view.findViewById(R.id.frg_setting_current_psw);
        etNew = (EditText) view.findViewById(R.id.frg_setting_new_psw);
        etConfirm = (EditText) view.findViewById(R.id.frg_setting_confirm_psw);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
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
        if (etCurrent.getText().length() < 6) {
            etCurrent.startAnimation(shakeAnim);
            etCurrent.requestFocus();
            toast("Old Password should be 6 character long.");
            return false;
        } else if (etNew.getText().length() < 6) {
            etNew.startAnimation(shakeAnim);
            etNew.requestFocus();
            toast("New Password should be 6 character long.");
            return false;
        } else if (etConfirm.getText().length() < 6) {
            etConfirm.startAnimation(shakeAnim);
            etConfirm.requestFocus();
            toast("Confirm Password should be 6 character long.");
            return false;
        } else if (!etConfirm.getText().toString().equals(etNew.getText().toString())) {
            etConfirm.startAnimation(shakeAnim);
            etConfirm.requestFocus();
            etNew.startAnimation(shakeAnim);
            etNew.requestFocus();
            toast("Password don't match.");
            return false;
        } else if (etCurrent.getText().toString().equals(etNew.getText().toString())) {
            etConfirm.startAnimation(shakeAnim);
            etConfirm.requestFocus();
            etNew.startAnimation(shakeAnim);
            etNew.requestFocus();
            toast("New Password must be different from old one.");
            return false;
        }
        return true;
    }

    private void onSaveDetails() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (etCurrent.getText().toString().length() > 0)
            hashMap.put("opassword", etCurrent.getText().toString());
        hashMap.put("npassword", etNew.getText().toString());
        hashMap.put("action", "submit_change_password");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("device_type", "android");
        new PostLibResponse(SettingPasswordFragment.this, new ModelLogin(), getActivity(), hashMap, WebServicesConst.CHANGE_PASSWORD, WebServicesConst.RES.CHANGE_PASSWORD, true, true);
    }


//    @Override
//    public void onPostResponseComplete(Object clsGson, int requestCode) {
//        modelLogin = (ModelLogin) clsGson;
//        if (requestCode == WebServicesConst.RES.CHANGE_PASSWORD) {
//            if (modelLogin != null) {
//                int Status = modelLogin.status;
//                if (Status == 200) {
//                    toast(modelLogin.message);
//                    getActivity().onBackPressed();
//                } else if (Status == 404) {
//                    toast(modelLogin.message);
//                }
//
//            }
//        }
//    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelLogin = (ModelLogin) clsGson;
        if (requestCode == WebServicesConst.RES.CHANGE_PASSWORD) {
            if (modelLogin != null) {
                int Status = modelLogin.status;
                if (Status == 200) {
                    toast(modelLogin.message);
                    if (prefs.get(MyPrefs.keys.REMEMBER).length()>0) {
                        prefs.set(MyPrefs.keys.PASSWORD, etNew.getText().toString());
                    }
                    getActivity().onBackPressed();
                } else if (Status == 404) {
                    toast(modelLogin.message);
                }

            }
        }
    }


}
