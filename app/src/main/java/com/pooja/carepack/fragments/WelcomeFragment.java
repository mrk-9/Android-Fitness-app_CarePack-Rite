package com.pooja.carepack.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.SplashActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.dialogs.ForgotPasswordDialog;
import com.pooja.carepack.interfaces.OnFbLoginListener;
import com.pooja.carepack.interfaces.iAnimationEndListener;
import com.pooja.carepack.interfaces.iForgotListener;
import com.pooja.carepack.model.BasicResponse;
import com.pooja.carepack.model.ModelLogin;
import com.pooja.carepack.utils.Animations;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

public class WelcomeFragment extends Fragment implements View.OnClickListener, OnFbLoginListener, iAnimationEndListener, LibPostListner {
    private static final int LOGIN = 1;
    private static final int SIGNUP = 2;
    public static int millies = 1500;
    private LinearLayout llButtons;
    private ImageView ivLogo;
    private Animations anim;
    private View view;
    private MyPrefs prefs;
    private LinearLayout signup;
    private TextView forget, login;
    private int isLogin;
    private CheckBox cbRemember, cbTerms;
    private ImageView loginFb;
    private EditText etEmail, etPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_welcome, container, false);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.LinearLayout2);
        rl.getLayoutParams().height = Utility.getScreenSize(getActivity()).y - getStatusBarHeight();
        prefs = new MyPrefs(getActivity());
        return view;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        llButtons = (LinearLayout) view.findViewById(R.id.splash_ll_welcome);
        ivLogo = (ImageView) view.findViewById(R.id.splash_iv_logo);
        anim = new Animations(getActivity(), this);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                millies = 00;
                if (prefs.get(MyPrefs.keys.ID).length() == 0) {
                    if (prefs.get(MyPrefs.keys.REMEMBER).length() > 0) {
                        try {
                            if (prefs.get(MyPrefs.keys.EMAIL).length() > 0) {
                                etEmail.setText(prefs.get(MyPrefs.keys.EMAIL));
//                                Log.e("TAG","EMAIL   "+prefs.get(MyPrefs.keys.EMAIL));
                            }
                            if (prefs.get(MyPrefs.keys.PASSWORD).length() > 0) {
                                etPassword.setText(prefs.get(MyPrefs.keys.PASSWORD));
//                                Log.e("TAG", "PASSWORD   " + prefs.get(MyPrefs.keys.PASSWORD));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cbRemember.setChecked(true);//set Remember checked after logout
                    }
                    ivLogo.startAnimation(anim.getTranslate());
                } else {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            }
        }, millies);

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        signup = (LinearLayout) view.findViewById(R.id.welcome_ll_signup);
        signup.setOnClickListener(this);
        login = (TextView) view.findViewById(R.id.welcome_tv_login);
        login.setOnClickListener(this);
        forget = (TextView) view.findViewById(R.id.tvForget);
        forget.setOnClickListener(this);
        loginFb = (ImageView) view.findViewById(R.id.welcome_iv_sign_fb);
        loginFb.setOnClickListener(this);
        cbRemember = (CheckBox) view.findViewById(R.id.welcome_cb_remember);
        cbTerms = (CheckBox) view.findViewById(R.id.welcome_cb_terms);
    }

    public void showButtons() {
        llButtons.startAnimation(anim.getToMiddle());
        llButtons.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_ll_signup:
                hideButtons(SIGNUP);
                break;
            case R.id.welcome_tv_login:
                onLogin();
                break;
            case R.id.welcome_iv_sign_fb:
                if (Utility.hasConnection(getActivity())) {
                    if (cbTerms.isChecked())
                        ((SplashActivity) getActivity()).loginWithFacebook(this);
                    else
                        BaseFragment.toast(getActivity(), "Please Accept Terms and Condition");
                } else
                    BaseFragment.toast(getActivity(), "No Internet Connection.");

                break;
            case R.id.tvForget:
                showEmailDialog();
                break;
        }
    }

    private void showEmailDialog() {
        ForgotPasswordDialog f = new ForgotPasswordDialog(getActivity());
        f.showDialog();
        f.setOnForgotPasswordListener(new iForgotListener() {

            @Override
            public void onForgotPassword(String email) {
                forgetPost(email);
            }
        });
    }

    public void hideButtons(int isLogin) {
        this.isLogin = isLogin;
        Utility.hideKeyboard(getActivity(), view);
        llButtons.startAnimation(anim.getToBottom());
        ivLogo.startAnimation(anim.getTranslateTop());
    }

    @Override
    public void onTranslateListener() {
        showButtons();
    }

    @Override
    public void onToMiddleListener() {
    }

    @Override
    public void onTranslateTopListener() {
        if (isLogin == LOGIN) {
            prefs.set(MyPrefs.keys.REMEMBER, cbRemember.isChecked() ? "remember" : "");
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            ((SplashActivity) getActivity()).setFragment(new RegisterFragment());
        }
    }

    public void onFbLogin(String id, String name, String email, String image) {

        Log.d("Tag", " FB login call");
        HashMap<String, String> hashMap = new HashMap<String, String>();

        String[] names = name.split(" ");
//        hashMap.put("firstname", names[0]);
//        hashMap.put("lastname", names[1]);
//        hashMap.put("email", email);
//        hashMap.put("fbid", id);
//        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
//        hashMap.put("device_type", "android");
//        hashMap.put("lat", prefs.get(MyPrefs.keys.LAT));
//        hashMap.put("lng", prefs.get(MyPrefs.keys.LNG));
//        hashMap.put("action", "submit_fb_login");
        String token = prefs.get(MyPrefs.keys.GCMKEY);
        prefs.set(MyPrefs.keys.IMAGE,image);
        if (token.length() == 0) {
            BaseFragment.toast(getActivity(), "Device token not found");
            token = "dummytoken";
        }
        PostLibResponse loginpost = new PostLibResponse(WelcomeFragment.this, new ModelLogin(), getActivity(), null, WebServicesConst.LOGIN, WebServicesConst.RES.LOGIN, true, true);
        loginpost.startRequest("firstname", names[0],
                "lastname", names[1],
                "email", email,
                "fbid", id,
                "device_token", token,
                "device_type", "android",
                "lat", prefs.get(MyPrefs.keys.LAT),
                "lng", prefs.get(MyPrefs.keys.LNG),
                "action", "submit_fb_login");
    }

    private void onLogin() {
        if (etEmail.getText().toString() == null || etPassword.getText().toString() == null || !Utility.isEmailValid(etEmail.getText().toString())) {
            Toast.makeText(getActivity(), getString(R.string.toast_invalid_login), Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().toString().length() < 6) {
            Toast.makeText(getActivity(), getString(R.string.toast_invalid_password_length), Toast.LENGTH_SHORT).show();
        } else if (!cbTerms.isChecked()) {
            BaseFragment.toast(getActivity(), "Please Accept Terms and Condition");
        } else {
            String token = prefs.get(MyPrefs.keys.GCMKEY);
            if (token.length() == 0) {
                BaseFragment.toast(getActivity(), "Device token not found");
                token = "dummytoken";
            }
            Log.d("Tag", "login call  " + prefs.get(MyPrefs.keys.GCMKEY) + " " + prefs.get(MyPrefs.keys.LAT) + " " + prefs.get(MyPrefs.keys.LNG));

            if (cbRemember.isChecked()) {
                prefs.set(MyPrefs.keys.EMAIL, etEmail.getText().toString());
                prefs.set(MyPrefs.keys.PASSWORD, etPassword.getText().toString());
            }
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("email", etEmail.getText().toString());
            hashMap.put("password", etPassword.getText().toString());
            hashMap.put("device_token", token);
            hashMap.put("device_type", "android");
            hashMap.put("lat", prefs.get(MyPrefs.keys.LAT));
            hashMap.put("lng", prefs.get(MyPrefs.keys.LNG));
            hashMap.put("action", "submit_login");

            new PostLibResponse(WelcomeFragment.this, new ModelLogin(), getActivity(), hashMap, WebServicesConst.LOGIN, WebServicesConst.RES.LOGIN, true, true);
        }
    }

    public void forgetPost(String email) {
        PostLibResponse mPostLib = new PostLibResponse(WelcomeFragment.this, new BasicResponse(), getActivity(), null, WebServicesConst.FORGET_URL, WebServicesConst.RES.FORGET_URL, true, true);
        mPostLib.startRequest("action", "submit_forgot_password", "email", email);
    }

    @Override
    public void onToBottomListener() {
        llButtons.setVisibility(View.GONE);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.LOGIN) {
            ModelLogin modelLogin = (ModelLogin) clsGson;
            if (modelLogin != null && modelLogin.status == 200) {
                prefs.setUser(modelLogin);
                hideButtons(LOGIN);
                Toast.makeText(getActivity(), "" + modelLogin.message, Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getActivity(), "" + modelLogin.message, Toast.LENGTH_SHORT).show();
                BaseFragment.toast(getActivity(),modelLogin.message);
            }
        } else if (requestCode == WebServicesConst.RES.FORGET_URL) {
            BasicResponse modelLogin = (BasicResponse) clsGson;
            Toast.makeText(getActivity(), "" + modelLogin.message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

}
