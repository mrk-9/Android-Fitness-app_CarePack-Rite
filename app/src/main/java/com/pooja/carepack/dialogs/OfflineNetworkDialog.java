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

import com.pooja.carepack.R;

public class OfflineNetworkDialog {

    private Context mContext;
    private Dialog dialog;

    public OfflineNetworkDialog(Context context) {
        mContext = context;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dismissDialogWithAnim(dialog);
    }

    public void show() {
        if (dialog == null) {
            dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_offline_network);
        }
        if (!dialog.isShowing()) {
            showDialogWithAnim(dialog);
        }
    }

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
