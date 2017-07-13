package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.FrgPagerAdapter;
import com.pooja.carepack.model.ModelTeeth;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TeethFragment extends BaseFragment implements ViewPager.OnPageChangeListener, LibPostListner {

    private View view;
    private RadioGroup rGroup;
    private int[] radioButton = {R.id.radioBtnScreen1, R.id.radioBtnScreen2, R.id.radioBtnScreen3};
    private TeethPager1Fragment teeth1;
    private TeethPager2Fragment teeth2;
    private TeethPager3Fragment teeth3;
    private ModelTeeth.UserTeeth user_teeth;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_viewpager, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
    }

    private void findViews() {
        rGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        rGroup.getChildAt(2).setVisibility(View.VISIBLE);

        pager = (ViewPager) view.findViewById(R.id.viewpager);
        teeth1 = new TeethPager1Fragment();
        teeth2 = new TeethPager2Fragment();
        teeth3 = new TeethPager3Fragment();
        pager.setAdapter(new FrgPagerAdapter(getChildFragmentManager(), new Fragment[]{teeth1, teeth2, teeth3}));
        pager.setOnPageChangeListener(this);

        getTeethInfo();
    }

    private void getTeethInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        new PostLibResponse(TeethFragment.this, new ModelTeeth(), getActivity(), hashMap, WebServicesConst.TEETH + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.EDIT_MEDICALFINDING_INFO, true, true);
    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        ModelTeeth teeth = (ModelTeeth) clsGson;
        if (teeth.user_teeth != null) {
            user_teeth = teeth.user_teeth;
            if (teeth.user_teeth.l != null) {
                teeth1.setResponseListener(this, user_teeth.l,pager);
            }
            if (teeth.user_teeth.f != null) {
                teeth2.setResponseListener(this, user_teeth.f,pager);
            }
            if (teeth.user_teeth.r != null) {
                teeth3.setResponseListener(this, user_teeth.r,pager);
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int pos) {
        rGroup.check(radioButton[pos]);
    }


}