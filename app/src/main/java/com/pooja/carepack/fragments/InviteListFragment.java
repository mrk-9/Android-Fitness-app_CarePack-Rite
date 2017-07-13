package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.InviteListAdapter;
import com.pooja.carepack.model.ModelInvite;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class InviteListFragment extends BaseFragment implements LibPostListner {

    private View view;
    private ListView lv;
    private ModelInvite modelInvite;
    private ArrayList<ModelInvite.User> userInviteList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_invitelist, container, false);
        android.util.Log.d("TAG", "INvite list frg oncreate");
        getInviteList();
//        Bundle bundle=getArguments();
//        if(bundle!=null){
//            modelInvite= (ModelInvite) bundle.getSerializable("UserInviteList");
//        }
//        if(modelInvite!=null){
//            lv.setAdapter(new InviteListAdapter(getActivity(), modelInvite));
//        }

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv = (ListView) view.findViewById(R.id.lv_invite);

    }

    private void getInviteList() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "get_invite");

        new PostLibResponse(InviteListFragment.this, new ModelInvite(), getActivity(), hashMap, WebServicesConst.GET_USER, WebServicesConst.RES.GET_USER, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelInvite = (ModelInvite) clsGson;
        if (modelInvite != null) {
            int status = Integer.parseInt(String.valueOf(modelInvite.getStatus()));
            android.util.Log.e("TAG", "STATUS INVITATION RES   " + status);
            if (status == 200) {
                lv.setAdapter(new InviteListAdapter(getActivity(), modelInvite));
            } else {
                toast(modelInvite.getMessage());
            }
        }

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
