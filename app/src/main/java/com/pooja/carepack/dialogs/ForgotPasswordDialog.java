/*
 * 
 */
package com.pooja.carepack.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.pooja.carepack.R;
import com.pooja.carepack.interfaces.iForgotListener;
import com.pooja.carepack.utils.Utility;
import com.rey.material.widget.EditText;

// TODO: Auto-generated Javadoc

/**
 * The Class ForgotPasswordDialog.
 */
public class ForgotPasswordDialog {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The listener.
     */
    private iForgotListener listener;

    /**
     * Instantiates a new forgot password dialog.
     *
     * @param context the context
     */
    public ForgotPasswordDialog(Context context) {
        mContext = context;
    }

    /**
     * Toast.
     *
     * @param msg the msg
     */
    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the on forgot password listener.
     *
     * @param l the new on forgot password listener
     */
    public void setOnForgotPasswordListener(iForgotListener l) {
        listener = l;
    }

    /**
     * Show dialog.
     */
    public void showDialog() {
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enter_email);
        final EditText edtEmail = (EditText) dialog.findViewById(R.id.edt_email);
        Button btnSend = (Button) dialog.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(mContext, edtEmail);
                if (!Utility.hasConnection(mContext)) {
                    // noConnection();
                    toast("Internet seems to be offline");
                    return;
                } else if (edtEmail.getText().toString().equals("")) {
                    toast("Please enter email");
                    edtEmail.requestFocus();
                    return;
                } else if (!Utility.isEmailValid(edtEmail.getText().toString())) {
                    toast("Please enter valid email.");
                    edtEmail.requestFocus();
                    return;
                }
                if (listener != null)
                    listener.onForgotPassword(edtEmail.getText().toString());
                dismissDialogWithAnim(dialog);
            }
        });
        Button btnDiscard = (Button) dialog.findViewById(R.id.btn_discard);
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialogWithAnim(dialog);
            }
        });
        showDialogWithAnim(dialog);
    }

    /**
     * Dismiss dialog with anim.
     *
     * @param dialog the dialog
     */
    private void dismissDialogWithAnim(final Dialog dialog) {
        Animation mBottomAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_out_bottom);
        Animation mTopAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_out_top);
        Animation mFadeOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_fade_out);
        mTopAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.dismiss();
            }
        });
        ((View) dialog.findViewById(R.id.dialog_top_view).getParent()).startAnimation(mFadeOutAnimation);
        dialog.findViewById(R.id.dialog_top_view).startAnimation(mTopAnimation);
        dialog.findViewById(R.id.dialog_bottom_view).startAnimation(mBottomAnimation);
    }

    /**
     * Show dialog with anim.
     *
     * @param dialog the dialog
     */
    private void showDialogWithAnim(Dialog dialog) {
        dialog.show();
        Animation mBottomAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_in_bottom);
        Animation mTopAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_in_top);
        Animation mFadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.abc_fade_in);
        ((View) dialog.findViewById(R.id.dialog_top_view).getParent()).startAnimation(mFadeInAnimation);
        dialog.findViewById(R.id.dialog_top_view).startAnimation(mTopAnimation);
        dialog.findViewById(R.id.dialog_bottom_view).startAnimation(mBottomAnimation);
    }
}
