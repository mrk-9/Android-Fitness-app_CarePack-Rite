package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.SplashActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelLogin;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener, LibPostListner {

    private View view;
    private ModelLogin modelLogin;
    private ImageView ivBack;
    private EditText etEmail, etPassword, etVerifyPassword;
    private MyPrefs prefs;
    private Animation shakeAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_register, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivBack = (ImageView) view.findViewById(R.id.ivRegisterBack);
        ivBack.setOnClickListener(this);
        (view.findViewById(R.id.register_tv_register)).setOnClickListener(this);
        (view.findViewById(R.id.register_ll_login)).setOnClickListener(this);
        etEmail = (EditText) view.findViewById(R.id.frg_register_etEmail);
        etPassword = (EditText) view.findViewById(R.id.frg_register_etPassword);
        etVerifyPassword = (EditText) view.findViewById(R.id.frg_register_etVerifyPassword);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        prefs = new MyPrefs(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_ll_login:
            case R.id.ivRegisterBack:
                ((SplashActivity) getActivity()).setFragment(new WelcomeFragment());
                break;
            case R.id.register_tv_register:
                if (isValid()) {
                    callRegisterService();
                }
                break;
        }
    }

    private boolean isValid() {
        if (etEmail.getText().toString().length() == 0 || !Utility.isEmailValid(etEmail.getText().toString())) {
            etEmail.startAnimation(shakeAnim);
            etEmail.requestFocus();
            toast(R.string.toast_invalid_email);
            return false;
        } else if (etPassword.getText().toString().length() < 6) {
            etPassword.startAnimation(shakeAnim);
            etPassword.requestFocus();
            toast(R.string.toast_invalid_password_length);
            return false;
        } else if (etVerifyPassword.getText().toString().length() < 6) {
            etVerifyPassword.startAnimation(shakeAnim);
            etVerifyPassword.requestFocus();
            toast(R.string.toast_invalid_password_length);
            return false;
        } else if (!etPassword.getText().toString().equals(etVerifyPassword.getText().toString())) {
            etPassword.startAnimation(shakeAnim);
            etPassword.requestFocus();
            etVerifyPassword.startAnimation(shakeAnim);
            toast("Password Mismatch");
            return false;
        }
        return true;
    }

    private void callRegisterService() {
        String token = prefs.get(MyPrefs.keys.GCMKEY);
        if (token.length() == 0) {
            toast(getActivity(), "Device token not found");
            token = "dummytoken";
        }
        android.util.Log.d("Tag", "login call  " + prefs.get(MyPrefs.keys.GCMKEY) + " " + prefs.get(MyPrefs.keys.LAT) + " " + prefs.get(MyPrefs.keys.LNG));
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("email", etEmail.getText().toString());
        hashMap.put("password", etPassword.getText().toString());
        hashMap.put("device_token", token);
        hashMap.put("device_type", "android");
        hashMap.put("lat", prefs.get(MyPrefs.keys.LAT));
        hashMap.put("lng", prefs.get(MyPrefs.keys.LNG));
        hashMap.put("action", "submit_register");
        new PostLibResponse(RegisterFragment.this, new ModelLogin(), getActivity(), hashMap, WebServicesConst.REGISTER, WebServicesConst.RES.REGISTER, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelLogin = (ModelLogin) clsGson;
        if (modelLogin != null) {
            int statusCode = Integer.parseInt(String.valueOf(modelLogin.status));
            BaseFragment.Log.d(modelLogin.message + "   " + statusCode);
            if (statusCode == 200) {
//                prefs.setUser(modelLogin);
                BaseFragment.Log.d(modelLogin.message + "   " + statusCode+"   if ");
                toast(modelLogin.message);
                getActivity().onBackPressed();
//                startActivity(new Intent(getActivity(), MainActivity.class));
//                getActivity().finish();
            } else {
                BaseFragment.Log.d(modelLogin.message + "   " + statusCode+"   else ");
                toast(modelLogin.message);
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    public void toast(String msg) {
        try {
            Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    public void toast(int msg) {
        try {
            Toast.makeText(getActivity(), "" + getString(msg), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }
}
