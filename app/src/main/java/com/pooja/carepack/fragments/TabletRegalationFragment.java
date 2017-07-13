package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.pooja.carepack.adapter.TableRegalationAdapter;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelUserTabletList;
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
public class TabletRegalationFragment extends BaseFragment implements LibPostListner {

    public int menu_item = 1;
    private View view;
    private SwipeMenuListView mListView;
    private Menu menu;
    private TableRegalationAdapter tableRegalationAdapter;
    private ModelUserTabletList modelTableList;
    private int deletedPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_tablet_regulation, container, false);
        setHasOptionsMenu(true);
        findView();
        return view;
    }

    private void findView() {

    }

    private void getTabletList() {
        new PostLibResponse(TabletRegalationFragment.this, new ModelUserTabletList(), getActivity(), new HashMap<String, String>(), WebServicesConst.USER_TABLE_REGULATION + "/" + prefs.get(MyPrefs.keys.ID), WebServicesConst.RES.GET_USER_TABLE_REGULATION, true, true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = (SwipeMenuListView) view.findViewById(R.id.lv_table_regalation);
        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Tablets Found"));
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#7FB8EC")));
                deleteItem.setWidth(Utility.dp2px(getResources(), 50));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mListView.setOnItemClickListener(new SwipeMenuListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.replaceFragment(new TabletRegalationAddFragment(), "Tablet Detail", "UserTabletID", "" + tableRegalationAdapter.getItemId(position));
            }
        });
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    Log.d("delete menu click  " + position);
                    deleteUserTablet(position);
                }
                return true;
            }
        });
        getTabletList();

    }

    private void deleteUserTablet(int position) {
        deletedPosition = position;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("action", "delete_user_tablet");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", modelTableList.user_tablet.get(position).id);
        new PostLibResponse(TabletRegalationFragment.this, new ModelEditEmailDeletVaccination(), getActivity(), hashMap, WebServicesConst.USER_TABLE_REGULATION, WebServicesConst.RES.DELETE_TABLE_REGULATION, true, true);
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
                mainActivity.replaceFragment(new TabletRegalationListFragment(), "Add Tablet");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.GET_USER_TABLE_REGULATION) {
            modelTableList = (ModelUserTabletList) clsGson;
            if (modelTableList != null) {
                int status = modelTableList.status;
                if (status == 200) {
                    tableRegalationAdapter = new TableRegalationAdapter(getActivity(), modelTableList.user_tablet);
                    mListView.setAdapter(tableRegalationAdapter);
                    (view.findViewById(R.id.lv_table_regalation_ll)).setBackgroundResource(0);
                } else {
                    mListView.setAdapter(new EmptyAdapter(getActivity(), "" + modelTableList.message));
                    mListView.setMenuCreator(null);
                }
            }
        } else if (requestCode == WebServicesConst.RES.DELETE_TABLE_REGULATION) {
            ModelEditEmailDeletVaccination deleteModel = (ModelEditEmailDeletVaccination) clsGson;
            if (deleteModel != null) {
                int status = deleteModel.getStatus();
                if (status == 200) {
                    modelTableList.user_tablet.remove(deletedPosition);
                    if (modelTableList.user_tablet != null && modelTableList.user_tablet.size() > 0) {
                        tableRegalationAdapter = new TableRegalationAdapter(getActivity(), modelTableList.user_tablet);
                        mListView.setAdapter(tableRegalationAdapter);
                        (view.findViewById(R.id.lv_table_regalation_ll)).setBackgroundResource(0);
                    } else {
                        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Tablets Found."));
                        mListView.setMenuCreator(null);
                    }
                }
            }
        }
        Utility.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

}
