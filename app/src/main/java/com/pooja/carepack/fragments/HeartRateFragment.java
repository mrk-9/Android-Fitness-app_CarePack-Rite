package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.FrgPagerAdapter;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class HeartRateFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    public int menu_item = 4;
    private View view;
    private RadioGroup rGroup;
    private int[] radioButton = {R.id.radioBtnScreen1, R.id.radioBtnScreen2};
    private String[] titleBMI = {"Heart Rate", "Heart Rate Explanation"};
    private Menu menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_viewpager, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(menu_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_mail:
                toast("mailed");
                break;
            default:
                break;
        }
        return true;
    }

    private void findViews() {
        rGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewpager);
        String url = WebServicesConst.URL_HEART_RATE;//"file:///android_asset/en_herzfrequenz_beschreibung.html"
        Fragment frg = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("path",url);
        frg.setArguments(args);
        pager.setAdapter(new FrgPagerAdapter(getChildFragmentManager(), new Fragment[]{new HeartRateChartFragment(), frg}));
        pager.setOnPageChangeListener(this);
//		rGroup.check(R.id.radioBtnScreen1);
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int pos) {
        getActivity().setTitle(titleBMI[pos]);
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(pos == 0 ? true : false);
        }
        rGroup.check(radioButton[pos]);
    }
}
