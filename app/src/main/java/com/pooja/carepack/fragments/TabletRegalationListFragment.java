package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.EmptyAdapter;
import com.pooja.carepack.adapter.TableRegalationListAdapter;
import com.pooja.carepack.model.ModelTabletList;
import com.pooja.carepack.swipemenulistview.SwipeMenuListView;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TabletRegalationListFragment extends BaseFragment implements LibPostListner {

    private View view;
    private ListView mListView;
    private TableRegalationListAdapter tableRegalationAdapter;
    private ModelTabletList modelTableList;
    private EditText etSearch;
    private int menu_item = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_tablet_regulation_list, container, false);
        setHasOptionsMenu(true);
        etSearch = (EditText) view.findViewById(R.id.frg_tablet_regulation_list_search);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.lv_table_regalation);
        mListView.setAdapter(new EmptyAdapter(getActivity(), "No Tablets Defined."));
        mListView.setOnItemClickListener(new SwipeMenuListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.replaceFragment(new TabletRegalationAddFragment(), "Tablet Detail", "tabletID", "" + tableRegalationAdapter.getItemId(position));
            }
        });
        getTabletList();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tableRegalationAdapter.search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getTabletList() {
        new PostLibResponse(TabletRegalationListFragment.this, new ModelTabletList(), getActivity(), new HashMap<String, String>(), WebServicesConst.TABLE_REGULATION, WebServicesConst.RES.GET_TABLE_REGULATION, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (isVisible() && requestCode == WebServicesConst.RES.GET_TABLE_REGULATION) {
            modelTableList = (ModelTabletList) clsGson;
            if (modelTableList != null) {
                int status = modelTableList.status;
                if (status == 200) {
                    tableRegalationAdapter = new TableRegalationListAdapter(getActivity(), modelTableList.tablet_list);
                    mListView.setAdapter(tableRegalationAdapter);
                } else {
                    mListView.setAdapter(new EmptyAdapter(getActivity(), "" + modelTableList.message));
                }
            }
        }
        Utility.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

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
                mainActivity.replaceFragment(new TabletRegalationAddFragment(), "Add Tablet");
                break;
            default:
                break;
        }
        return true;
    }

}
