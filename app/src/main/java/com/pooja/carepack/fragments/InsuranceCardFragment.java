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
import com.pooja.carepack.model.ModelInsurance;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class InsuranceCardFragment extends BaseFragment implements ViewPager.OnPageChangeListener, LibPostListner {

    private View view;
    private RadioGroup rGroup;
    private int[] radioButton = {R.id.radioBtnScreen1, R.id.radioBtnScreen2};
    private InsuranceCardPager1Fragment insu1;
    private InsuranceCardPager2Fragment insu2;

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
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewpager);
        insu1 = new InsuranceCardPager1Fragment();
        insu2 = new InsuranceCardPager2Fragment();
        pager.setAdapter(new FrgPagerAdapter(getChildFragmentManager(), new Fragment[]{insu1, insu2}));
        pager.setOnPageChangeListener(this);
        getInsuranceInfo();
    }

    private void getInsuranceInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        new PostLibResponse(InsuranceCardFragment.this, new ModelInsurance(), getActivity(), hashMap, WebServicesConst.INSURANCE_CARD + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.EDIT_MEDICALFINDING_INFO, true, true);
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

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        ModelInsurance insurance = (ModelInsurance) clsGson;
        if (insurance.insurance_card != null) {
            for (int i = 0; i < insurance.insurance_card.size(); i++) {
                if (insurance.insurance_card.get(i).card_type.equals("f")) {
                    insu1.setImage(insurance.insurance_card.get(i).image);
                } else if (insurance.insurance_card.get(i).card_type.equals("b")) {
                    insu2.setImage(insurance.insurance_card.get(i).image);
                }
            }

        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}