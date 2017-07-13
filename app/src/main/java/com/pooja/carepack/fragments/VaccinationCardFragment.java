package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.EmptyAdapter;
import com.pooja.carepack.adapter.VaccinationCardAdapter;
import com.pooja.carepack.adapter.VaccinationGridViewAdapter;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelUserVaccinationList;
import com.pooja.carepack.model.ModelVaccinationList;
import com.pooja.carepack.swipemenulistview.SwipeMenu;
import com.pooja.carepack.swipemenulistview.SwipeMenuCreator;
import com.pooja.carepack.swipemenulistview.SwipeMenuItem;
import com.pooja.carepack.swipemenulistview.SwipeMenuListView;
import com.pooja.carepack.utils.FlowLayout;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class VaccinationCardFragment extends BaseFragment implements LibPostListner {
    public int menu_item = 1;
    private View view;
    private SwipeMenuListView mListView;
    private VaccinationCardAdapter vaccinationCardAdapter;
    private Menu menu;
    private ModelVaccinationList modelVaccinationList;
    //    private GridView gvVaccination;
    private VaccinationGridViewAdapter adtGridVaccination;
    private MyPrefs prefs;
    private ModelUserVaccinationList modelUserVaccinationList;
    private String emailStatus;
    private ModelEditEmailDeletVaccination modelEditEmailDeletVaccination;
    private int deletedPosition;
    private FlowLayout flVaccination;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_vaccination_card, container, false);
        intiUI();
        setHasOptionsMenu(true);
        getVaccinationList();
        getUserVaccinationList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = (SwipeMenuListView) view.findViewById(R.id.vaccination_lv);
        mListView.setOnItemClickListener(new SwipeMenuListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(getActivity().getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                editItem.setWidth(Utility.dp2px(getResources(), 50));
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                deleteItem.setWidth(Utility.dp2px(getResources(), 50));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);

            }
        };
        mListView.setMenuCreator(creator);

        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Record Found"));
        Utility.setListViewHeightBasedOnChildren(mListView);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
//                    Log.d("TAG","edit menu click  "+position);
                    Fragment fContainer = new VaccinationCardAddFragment();
                    Bundle bundel = new Bundle();
                    bundel.putString("UserVaccinationID", modelUserVaccinationList.getUserVaccination().get(position).getId());
                    fContainer.setArguments(bundel);
                    mainActivity.replaceFragment(fContainer, "Add Vaccination");
                } else if (index == 1) {
                    Log.d("delete menu click  " + position);
                    deleteUserVaccination(position);
                }
                return true;
            }
        });
    }

    private void intiUI() {
        prefs = new MyPrefs(getActivity());
        flVaccination = (FlowLayout) view.findViewById(R.id.vaccination_fl);
        flVaccination.setAdapter(new EmptyAdapter(getActivity(), "No Vaccination"));
    }

    private void deleteUserVaccination(int position) {
        deletedPosition = position;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("action", "delete_user_vaccination");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", modelUserVaccinationList.getUserVaccination().get(position).getId());
        new PostLibResponse(VaccinationCardFragment.this, new ModelEditEmailDeletVaccination(), getActivity(), hashMap, WebServicesConst.USER_VACCINATION_LIST, WebServicesConst.RES.DELETE_USER_VACCINATION, true, true);
    }

    private void getVaccinationList() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(VaccinationCardFragment.this, new ModelVaccinationList(), getActivity(), hashMap, WebServicesConst.VACCINATION_LIST, WebServicesConst.RES.GET_VACCINATION_LIST, true, true);
    }

    private void getUserVaccinationList() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(VaccinationCardFragment.this, new ModelUserVaccinationList(), getActivity(), hashMap, WebServicesConst.USER_VACCINATION_LIST + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.GET_USER_VACCINATION_LIST, true, true);
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
            case R.id.main_menu_add:
                mainActivity.replaceFragment(new VaccinationCardAddFragment(), "Add Vaccination");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        Log.d("Request ccode : " + requestCode);

        if (requestCode == WebServicesConst.RES.GET_VACCINATION_LIST) {
            modelVaccinationList = (ModelVaccinationList) clsGson;
            if (modelVaccinationList != null) {
                int status = modelVaccinationList.getStatus();
                if (status == 200) {
                    adtGridVaccination = new VaccinationGridViewAdapter(getActivity(), modelVaccinationList);
                    flVaccination.setAdapter(adtGridVaccination);
                } else {
                    toast(modelVaccinationList.getMessage());
                }
            }
        } else if (requestCode == WebServicesConst.RES.GET_USER_VACCINATION_LIST) {
            modelUserVaccinationList = (ModelUserVaccinationList) clsGson;
            if (modelUserVaccinationList != null) {
                int status = modelUserVaccinationList.getStatus();
                if (status == 200) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vaccinationCardAdapter = new VaccinationCardAdapter(getActivity(), modelUserVaccinationList);
                            mListView.setAdapter(vaccinationCardAdapter);
                        }
                    }, 500);


                } else {
                    mListView.setAdapter(new EmptyAdapter(getActivity(), "" + modelUserVaccinationList.getMessage()));
                    mListView.setMenuCreator(null);
                }
            }
        } else if (requestCode == WebServicesConst.RES.DELETE_USER_VACCINATION) {
            modelEditEmailDeletVaccination = (ModelEditEmailDeletVaccination) clsGson;
            if (modelEditEmailDeletVaccination != null) {
                int status = modelEditEmailDeletVaccination.getStatus();
                if (status == 200) {
                    vaccinationCardAdapter.refreshAdapter();
                    vaccinationCardAdapter.notifyDataSetChanged();
                    getUserVaccinationList();
//                    toast(modelEditEmailDeletVaccination.getMessage());
                } else {
                    toast(modelEditEmailDeletVaccination.getMessage());
                }
            }
        }
        Utility.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
