package com.pooja.carepack.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.pooja.carepack.R;
import com.pooja.carepack.utils.TouchImageView;
import com.rey.material.widget.LinearLayout;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Yudiz on 08/02/16.
 */
public class DialogFragmentZoomImage extends DialogFragment implements View.OnClickListener {
    private Dialog view;
    private TouchImageView ivImage;
    private String imagePath;
    private LinearLayout llBack;
    private String chatImagePath;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
//        view.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view.setContentView(R.layout.dialog_image_zoom);
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        initUI();

        Bundle bundle = getArguments();
        if (bundle != null) {
            imagePath = bundle.getString("ImagePath");
            Log.d("TAG", "Imagepath in zoom dialog  ----  " + imagePath);
            if(bundle.getString("ChatImagePath")!=null){
                chatImagePath=bundle.getString("ChatImagePath");
            }
        }
        if (imagePath != null)
            Picasso.with(getActivity()).load(new File(imagePath)).into(ivImage);
        if(chatImagePath!=null)
            Picasso.with(getActivity()).load(chatImagePath).into(ivImage);

        view.show();

        return view;
    }

    private void initUI() {
        llBack = (LinearLayout) view.findViewById(R.id.dialog_image_zoom_ll_back);
        llBack.setOnClickListener(this);
        ivImage = (TouchImageView) view.findViewById(R.id.dialog_image_zoom_ivImage);
        ivImage.setMaxZoom(4f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_image_zoom_ll_back:
                DialogFragmentZoomImage.this.view.dismiss();
                break;
        }
    }
}
