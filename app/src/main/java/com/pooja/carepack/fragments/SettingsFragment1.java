//package com.pooja.carepack.fragments;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//
//import com.pooja.carepack.R;
//import com.pooja.carepack.activities.MainActivity;
//import com.pooja.carepack.utils.AnimationUtil;
//import com.pooja.carepack.utils.MyPrefs;
//
///**
// * Created by Vinay Rathod on 24/11/15.
// */
//public class SettingsFragment1 extends BaseFragment {
//
//    private View view;
//    private LinearLayout llScroll;
//    private ScrollView your_scrollview;
//    private boolean expanding;
//    private MyPrefs prefs;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.frg_settings1, container, false);
//        prefs = new MyPrefs(getActivity());
//        if (prefs.getUser() == null) {
//            ((MainActivity) getActivity()).addFragment(new SettingProfileFragment(), "Settings");
//        }
//
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        llScroll = (LinearLayout) view.findViewById(R.id.setting_scroll_layout);
//        your_scrollview = (ScrollView) view.findViewById(R.id.setting_scroll_view);
//
//        for (int index = 0; index < llScroll.getChildCount(); index++) {
//            LinearLayout v = (LinearLayout) llScroll.getChildAt(index);
//            v.setId(index);
//            if (index % 2 == 0) {
//                v.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View vv) {
//                        onExpand(vv);
//                    }
//                });
//                if (index == 0) {
//                    RelativeLayout rl = (RelativeLayout) v.getChildAt(0);
//                    ImageView ivArrow = (ImageView) rl.getChildAt(1);
//                    Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate2bottom);
//                    ivArrow.startAnimation(rotateAnim);
//                }
//            } else {
//                v.setOnClickListener(null);
//            }
//        }
//    }
//
//    private void onExpand(View vv) {
//        expanding = true;
//        LinearLayout nextView = (LinearLayout) view.findViewById(vv.getId() + 1);
//        if (vv.getId() == 0) {
//            ((MainActivity) getActivity()).addFragment(new SettingProfileFragment(), "Settings");
//            expanding = false;
//        } else if (nextView.getChildCount() > 0) {
////            Toast.makeText(getActivity(),"if   "+vv.getId(),Toast.LENGTH_SHORT).show();
//            LinearLayout currentView = (LinearLayout) view.findViewById(vv.getId());
//            RelativeLayout rl = (RelativeLayout) currentView.getChildAt(0);
//            ImageView ivArrow = (ImageView) rl.getChildAt(1);
//            if (nextView.getVisibility() == View.GONE) {
//                Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate2bottom);
//                ivArrow.startAnimation(rotateAnim);
//            } else {
//                Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate2right);
//                ivArrow.startAnimation(rotateAnim);
//            }
//            AnimationUtil.animate(nextView);
//        } else {
////            Toast.makeText(getActivity(),"else   "+vv.getId(),Toast.LENGTH_SHORT).show();
//            ((MainActivity) getActivity()).addFragment(new SettingBodyFragment(), "Body Settings");
//            expanding = false;
//        }
//    }
//
//}
