package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.FrgPagerAdapter;


/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class BMIFragment extends BaseFragment implements ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {

    private View view;
    private RadioGroup rGroup;
    private int[] radioButton = {R.id.frg_bmi_rb1, R.id.frg_bmi_rb2};
    private String[] titleBMI = {"Body Mass Index", "Body Mass Index Explanation"};
    private ViewPager pager;
    private RadioButton rb1,rb2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_bmi, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
    }

    private void findViews() {
        rGroup = (RadioGroup) view.findViewById(R.id.frg_bmi_radioGroup);
         pager = (ViewPager) view.findViewById(R.id.viewpager);
        rb1=(RadioButton)view.findViewById(R.id.frg_bmi_rb1);
        rb2=(RadioButton)view.findViewById(R.id.frg_bmi_rb2);
        String url = WebServicesConst.URL_BMI_DESC;//"file:///android_asset/en_bmi_beschreibung.html"
        Fragment frg = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("path", url);
        frg.setArguments(args);
        pager.setAdapter(new FrgPagerAdapter(getChildFragmentManager(), new Fragment[]{new BMIPagerFragment(), frg}));
        pager.setOnPageChangeListener(this);
//        rGroup.setOnCheckedChangeListener(this);
        rb1.setOnCheckedChangeListener(this);
        rb2.setOnCheckedChangeListener(this);
        rGroup.check(0);
    }


    @Override
    public void onPageScrollStateChanged(int pos) {
        android.util.Log.d("TAG","bmi frg onPageScrollStateChanged  "+pos);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        android.util.Log.d("TAG","bmi frg  onPageScrolled "+arg0+"  "+arg1+"   "+arg2);
//        rGroup.check(radioButton[pos]);
    }

    @Override
    public void onPageSelected(int pos) {
        getActivity().setTitle(titleBMI[pos]);
        rGroup.check(radioButton[pos]);
        android.util.Log.d("TAG","bmi frg  onPageSelected "+pos);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.frg_bmi_rb1:
                pager.setCurrentItem(0);
//                rGroup.check(radioButton[0]);
//                rb1.setChecked(true);
                break;
            case R.id.frg_bmi_rb2:
                pager.setCurrentItem(1);
//                rGroup.check(radioButton[1]);
//                rb2.setChecked(true);
                break;
        }
    }
}
