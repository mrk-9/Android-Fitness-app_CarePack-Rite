package com.pooja.carepack.fragments;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.utils.CoverFlow;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.ProgressWheel;
import com.pooja.carepack.utils.Utility;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;

import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class BMIPagerFragment extends BaseFragment {

    int counter = 0;
    private View view;
    private CoverFlow gallery;
    private int imageSize;
    private float weight = 0, height = 0;
    private double bmi, progress = 0;
    //        private TextView tvBMI;
    private TextView tvStatus, tvStatusBMI;
    private ProgressWheel pwBMI;
    private boolean running;
    private double bmiProgress = 0;
    //    private ImageView imvGallery;
    private Integer[] mImageIds = {
            R.drawable.bmi1,
            R.drawable.bmi2,
            R.drawable.bmi3,
            R.drawable.bmi4,
            R.drawable.bmi5
    };
    private int galleryProgress = 0;

    public static String getStatus(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 23) {
            return "Normal Range";
        } else if (bmi < 25) {
            return "Overweight—At Risk";
        } else if (bmi < 30) {
            return "Overweight—Moderately Obese";
        } else {
            return "Overweight—Severely Obese";
        }
    }

    public static String getBMIRange(double bmi) {
        if (bmi < 18.5) {
            return "(<18.5)";
        } else if (bmi < 23) {
            return "(18.5-22.9)";
        } else if (bmi < 25) {
            return "(23.0-24.9)";
        } else if (bmi < 30) {
            return "(25.0-29.9)";
        } else {
            return "(>=30)";
        }
    }

    public static int getStatusPosition(double bmi) {
        if (bmi < 18.5) {
            return 0;
        } else if (bmi < 23) {
            return 1;
        } else if (bmi < 25) {
            return 2;
        } else if (bmi < 30) {
            return 3;
        } else {
            return 4;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_bmi_pager1, container, false);

        initUI();
        if (!prefs.get(MyPrefs.keys.HEIGHT).equals("") && !prefs.get(MyPrefs.keys.WEIGHT).equals("")) {
            height = Float.parseFloat(prefs.get(MyPrefs.keys.HEIGHT));
            weight = Float.parseFloat(prefs.get(MyPrefs.keys.WEIGHT));
        }
        if (height < 50 || weight < 20 || prefs.get(MyPrefs.keys.HEIGHT).equals("") || prefs.get(MyPrefs.keys.WEIGHT).equals("")) {
            showSetHWDialog();
//            toast(R.string.toast_invalid_height_weight);
            pwBMI.setText("Your BMI \n " + 0);
            pwBMI.setProgress(360);
//            imvGallery.setImageResource(mImageIds[0]);
            tvStatus.setText("Underweight");
            tvStatusBMI.setText("(<18.5)");
        } else {
            bmi = calculateBMI(height, weight);
            setData();
        }

        return view;
    }

    private void showSetHWDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.set_bmi_dialog);

        Button btnOk = (Button) dialog.findViewById(R.id.set_bmi_dialog_btnOk);
//        Button btnCancle = (Button) dialog.findViewById(R.id.set_bmi_dialog_btnCancle);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(new SettingBodyFragment(), "Body Setting");
                dialog.dismiss();
            }
        });
//        btnCancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                getActivity().onBackPressed();
//                getActivity().onBackPressed();
//            }
//        });


        dialog.show();
    }

    private void setData() {


        final Runnable r1 = new Runnable() {
            public void run() {
                running = true;
                int i = 0;
                while (bmiProgress < bmi) {
                    pwBMI.setText("Your BMI \n " + new DecimalFormat("#.##").format(bmiProgress) + "");

                    bmiProgress += (bmi / 360);
                    pwBMI.incrementProgress();
                    android.util.Log.e("TAG", "BMI  " + bmiProgress + "  :  " + bmi / 360 + "  :  " + i++ + " : " + bmi);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                running = false;
            }
        };

        if (!running) {
            bmiProgress = 0;
            pwBMI.resetCount();
            pwBMI.setProgress(360);
            Thread s1 = new Thread(r1);
            s1.start();
            if (!s1.isAlive())
                pwBMI.setText("Your BMI \n " + new DecimalFormat("#.##").format(bmi) + "");
        }

        //        imvGallery.setImageResource(mImageIds[getStatusPosition(bmi)]);
//        pwBMI.setText("Your BMI \n " + new DecimalFormat("#.##").format(bmi) + "");
        tvStatus.setText(getStatus(bmi));
        tvStatusBMI.setText(" " + getBMIRange(bmi));


    }

    private void initUI() {
//        tvBMI = (TextView) view.findViewById(R.id.frg_bmi_pager1_tvBMI);
        tvStatus = (TextView) view.findViewById(R.id.frg_bmi_pager1_tvStatus);
        pwBMI = (ProgressWheel) view.findViewById(R.id.frg_bmi_pager1_progressWheel);
//        imvGallery = (ImageView) view.findViewById(R.id.frg_bmi_pager1_ivGallery);
        tvStatusBMI = (TextView) view.findViewById(R.id.frg_bmi_pager1_tvStatus1);

//        tvBMI.setText(new DecimalFormat("#.##").format(bmi)+ "");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        android.util.Log.e("TAG", "onActivityCreate");
        imageSize = Utility.getScreenPart(getActivity(), 25);

        gallery = (CoverFlow) view.findViewById(R.id.bmi_gallery);

        gallery.setAdapter(new ImageAdapter(getActivity()));


        gallery.setSpacing(-25);
        gallery.setSelection(4, true);
        gallery.setAnimationDuration(2000);
        gallery.setEnabled(true);

        if (!(height == 0 || weight == 0)) {
            new CountDownTimer(3000, 500) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (counter < 5) {
                        gallery.setSelection(counter, true);
                        counter++;
                    }
                }

                @Override
                public void onFinish() {
                        gallery.setSelection(getStatusPosition(bmi), true);
                }
            }.start();
        }
        else
            gallery.setSelection(0, true);


//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if(finalPos==5){
//
//                    }
//                    else {
//                        if (!(height == 0 || weight == 0)) {
//                            gallery.setSelection(finalPos, true);
//                        } else
//                            gallery.setSelection(0, true);
//                    }
//                }
//            }, 3000);

    }

    private double calculateBMI(double height, double weight) {
        weight = weight * 2.2046;  //convert in kg to pound
        height = height * 0.393701; //convert inch to cm
        return weight * 703 / (height * height);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        private FileInputStream fis;
        private int statusPos;

        private Integer[] mImageIds = {
                R.drawable.person_bmi_1,
                R.drawable.person_bmi_2,
                R.drawable.person_bmi_3,
                R.drawable.person_bmi_4,
                R.drawable.person_bmi_5
        };

        private ImageView[] mImages;

        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[mImageIds.length];
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            //Use this code if you want to load from resources
            ImageView i = new ImageView(mContext);
            i.setImageResource(mImageIds[position]);

            i.setLayoutParams(new CoverFlow.LayoutParams(imageSize, imageSize));
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            //Make sure we set anti-aliasing otherwise we get jaggies
            BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
            drawable.setAntiAlias(true);
            return i;

            //return mImages[position];
        }

        public float getScale(boolean focused, int offset) {
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
        }

    }
}
//set gasllery view
//        final Runnable r1 = new Runnable() {
//            public void run() {
//                running = true;
//                if (!(height == 0 || weight == 0)) {
//                    while (galleryProgress < 5) {
//                        android.util.Log.e("TAG", "galleryProgress  " + galleryProgress);
//                        gallery.setSelection(galleryProgress, true);
//                        galleryProgress++;
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    gallery.setSelection(getStatusPosition(bmi), true);
//                } else
//                    gallery.setSelection(0, true);
//                running = false;
//            }
//        };
//
//        if (!running) {
//            galleryProgress = 0;
//            Thread s1 = new Thread(r1);
//            s1.start();
//        }
