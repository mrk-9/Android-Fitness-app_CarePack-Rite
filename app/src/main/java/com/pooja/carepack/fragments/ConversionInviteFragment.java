package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.pooja.carepack.R;
import com.pooja.carepack.model.ModelInvite;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class ConversionInviteFragment extends BaseFragment implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    ViewPager pager;
    private View view;
    private RadioGroup rGroup;
    private int[] radioButton = {R.id.radioBtnScreen1, R.id.radioBtnScreen2};
    private ModelInvite modelInvite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_invitation, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(this);
        pager = (ViewPager) view.findViewById(R.id.viewpager);
        pager.setAdapter(new FrgPagerAdapter(getChildFragmentManager(), new Fragment[]{new InvitationListFragment(), new InviteListFragment()}));
        pager.setOnPageChangeListener(this);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        pager.setCurrentItem(checkedId == R.id.radioBtnScreen1 ? 0 : 1);
    }



    public class FrgPagerAdapter extends FragmentPagerAdapter {

        private final Fragment[] frgs;

        public FrgPagerAdapter(FragmentManager fm, Fragment[] frgs) {
            super(fm);
            this.frgs = frgs;
        }

        @Override
        public int getCount() {
            return frgs.length;
        }

        @Override
        public Fragment getItem(int pos) {
            return frgs[pos];
        }
    }

}
