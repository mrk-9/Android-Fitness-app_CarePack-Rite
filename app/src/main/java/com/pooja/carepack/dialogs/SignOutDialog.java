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

import com.pooja.carepack.R;
import com.pooja.carepack.interfaces.iSignoutListener;
import com.pooja.carepack.utils.MyPrefs;


// TODO: Auto-generated Javadoc

/**
 * The Class SignOutDialog.
 */
public class SignOutDialog {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The listener.
     */
    private iSignoutListener listener;

    /**
     * The prefs.
     */
    private MyPrefs prefs;
    private Dialog dialog;

    /**
     * Instantiates a new sign out dialog.
     *
     * @param c the c
     */
    public SignOutDialog(Context c) {
        mContext = c;
        prefs = new MyPrefs(mContext);
    }

    public void dismiss() {
        dismissDialogWithAnim(dialog);
    }

    /**
     * Show dialog.
     */
    public void showDialog() {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signout);
        Button btnSend = (Button) dialog.findViewById(R.id.btn_logout);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onSignout();
                dismiss();
            }
        });
        Button btnDiscard = (Button) dialog.findViewById(R.id.btn_discard);
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showDialogWithAnim(dialog);
    }

    /**
     * Sets the on sign out listener.
     *
     * @param l the new on sign out listener
     */
    public void setOnSignOutListener(iSignoutListener l) {
        listener = l;
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
