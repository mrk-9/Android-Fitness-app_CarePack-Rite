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
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.EmptyAdapter;
import com.pooja.carepack.adapter.MedicalFindingAdapter;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelMedicalFinding;
import com.pooja.carepack.swipemenulistview.SwipeMenu;
import com.pooja.carepack.swipemenulistview.SwipeMenuCreator;
import com.pooja.carepack.swipemenulistview.SwipeMenuItem;
import com.pooja.carepack.swipemenulistview.SwipeMenuListView;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class MedicalFindingFragment extends BaseFragment implements LibPostListner {

    private View view;
    private SwipeMenuListView mListView;
    private MedicalFindingAdapter medicalFindingAdapter;
    private MyPrefs prefs;
    private ModelMedicalFinding modelMedicalFinding;
    private Menu menu;
    private ModelEditEmailDeletVaccination modelDeleteMedicalFinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_medicalfinding, container, false);
        prefs = new MyPrefs(getActivity());
        getMedicalFindingList();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (SwipeMenuListView) view.findViewById(R.id.finding_lv_medical);
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


                SwipeMenuItem shareItem = new SwipeMenuItem(getActivity().getApplicationContext());
                shareItem.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                shareItem.setWidth(Utility.dp2px(getResources(), 50));
                shareItem.setIcon(R.drawable.ic_share);
                menu.addMenuItem(shareItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Record Found"));
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    Fragment fContainer = new MedicalFindingAddFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("MedicalFindingID", modelMedicalFinding.getMedicalFindings().get(position).getId());
                    fContainer.setArguments(bundle);
                    ((MainActivity) getActivity()).replaceFragment(fContainer, "Add Medical Finding");
                } else if (index == 1) {
                    deleteMedicalFindingList(modelMedicalFinding.getMedicalFindings().get(position).getId());
                }
                return true;
            }
        });
    }


    private void getMedicalFindingList() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));

        new PostLibResponse(MedicalFindingFragment.this, new ModelMedicalFinding(), getActivity(), hashMap, WebServicesConst.MEDICAL_FINDING + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.GET_MEDICALFINDING, true, true);
    }

    private void deleteMedicalFindingList(String medicalFindingID) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        if (medicalFindingID != null)
            hashMap.put("id", medicalFindingID);
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "delete_medical_finding");

        new PostLibResponse(MedicalFindingFragment.this, new ModelEditEmailDeletVaccination(), getActivity(), hashMap, WebServicesConst.MEDICAL_FINDING, WebServicesConst.RES.DELETE_MEDICALFINDING_INFO, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (isVisible()) {
            if (requestCode == WebServicesConst.RES.GET_MEDICALFINDING) {
                modelMedicalFinding = (ModelMedicalFinding) clsGson;
                if (modelMedicalFinding != null) {
                    int status = modelMedicalFinding.getStatus();
                    if (status == 200) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                medicalFindingAdapter = new MedicalFindingAdapter(getActivity(), modelMedicalFinding);
                                mListView.setAdapter(medicalFindingAdapter);
                            }
                        }, 500);

//                    Toast.makeText(getActivity(), modelMedicalFinding.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        mListView.setAdapter(new EmptyAdapter(getActivity(), modelMedicalFinding.getMessage()));
                    }
                }
            } else if (requestCode == WebServicesConst.RES.DELETE_MEDICALFINDING_INFO) {
                modelDeleteMedicalFinding = (ModelEditEmailDeletVaccination) clsGson;
                if (modelDeleteMedicalFinding != null) {
                    int status = modelDeleteMedicalFinding.getStatus();
                    if (status == 200) {
                        getMedicalFindingList();
//                    Toast.makeText(getActivity(), modelDeleteMedicalFinding.getMessage(), Toast.LENGTH_SHORT).show();
                    } else
                        toast(modelDeleteMedicalFinding.getMessage());
                }
            }
            Utility.setListViewHeightBasedOnChildren(mListView, 0);
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    public int menu_item = 1;
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
//                SaveVaccinationInfo();
                mainActivity.replaceFragment(new MedicalFindingAddFragment(), "Add Medical Finding");
                break;
            default:
                break;
        }
        return true;
    }
}
