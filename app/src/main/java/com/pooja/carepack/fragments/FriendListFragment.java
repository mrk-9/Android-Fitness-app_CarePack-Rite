package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.FriendListAdapter;
import com.pooja.carepack.model.ModelInvitation;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Yudiz on 03/02/16.
 */
public class FriendListFragment extends BaseFragment implements LibPostListner{

    private ListView lstFriendlist;
    private ModelInvitation modelFriendList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_friend_list, container, false);
        initUI();
        getFriendList();
        return view;
    }

    private void initUI() {
        lstFriendlist=(ListView)view.findViewById(R.id.frg_friend_list_lstFreind);
    }
    private void getFriendList() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "get_friends");

        new PostLibResponse(FriendListFragment.this, new ModelInvitation(), getActivity(), hashMap, WebServicesConst.GET_USER, WebServicesConst.RES.GET_USER, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelFriendList=(ModelInvitation)clsGson;
        if(modelFriendList!=null){
            int status=modelFriendList.getStatus();
            if(status==200){
                lstFriendlist.setAdapter(new FriendListAdapter(getActivity(),modelFriendList));
            }
            else{
                toast(modelFriendList.getMessage());
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
