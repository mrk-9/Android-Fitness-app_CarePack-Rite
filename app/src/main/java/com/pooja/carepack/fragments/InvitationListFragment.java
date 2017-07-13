package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.InvitationListAdapter;
import com.pooja.carepack.model.ModelInvitation;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class InvitationListFragment extends BaseFragment implements LibPostListner{

    private View view;
    private ListView lv;
    private ModelInvitation modelInvitation;
    private int menu_item=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_invitation_list, container, false);
        android.util.Log.d("TAG", "INvitation list frg oncreate");
        getInvotationList();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv = (ListView)view.findViewById(R.id.lv_invitation);

    }
    private void getInvotationList() {

            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
            hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
            hashMap.put("action", "get_invitation");

            new PostLibResponse(InvitationListFragment.this, new ModelInvitation(), getActivity(), hashMap, WebServicesConst.GET_USER, WebServicesConst.RES.GET_USER, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelInvitation=(ModelInvitation)clsGson;
        if(modelInvitation!=null){
            int status=Integer.parseInt(String.valueOf(modelInvitation.getStatus()));
            android.util.Log.e("TAG","STATUS INVITATION RES   "+status);
            if(status==200){
                lv.setAdapter(new InvitationListAdapter(getActivity(),modelInvitation));
            }
            else{
                toast(modelInvitation.getMessage());
            }
        }
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
                mainActivity.replaceFragment(new SearchUserFragment(), "User");
                break;
            default:
                break;
        }
        return true;
    }
}
