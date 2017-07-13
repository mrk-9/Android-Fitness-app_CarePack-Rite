package com.pooja.carepack.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.FriendListAdapter;
import com.pooja.carepack.model.ModelInvitation;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.widget.LinearLayout;

import java.util.HashMap;

/**
 * Created by Yudiz on 04/02/16.
 */
public class FriendListDialogFragment extends DialogFragment implements LibPostListner, AdapterView.OnItemClickListener,View.OnClickListener {
    private Dialog view;
    private ListView lstFriendlist;
    private ModelInvitation modelFriendList;
    private MyPrefs prefs;
    private String selectedFriendName;
    private LinearLayout llBack;
    private String selectedFriendID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        view = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
//        view.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view.setContentView(R.layout.frg_friend_list);
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        initUI();
        getFriendList();
        view.show();

        return view;
    }

    private void initUI() {
        prefs = new MyPrefs(getActivity());
        lstFriendlist = (ListView) view.findViewById(R.id.frg_friend_list_lstFreind);
        lstFriendlist.setOnItemClickListener(this);
        llBack=(LinearLayout)view.findViewById(R.id.frg_friend_list_ll_back);
        llBack.setOnClickListener(this);
    }

    private void getFriendList() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "get_friends");

        new PostLibResponse(FriendListDialogFragment.this, new ModelInvitation(), getActivity(), hashMap, WebServicesConst.GET_USER, WebServicesConst.RES.GET_USER, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelFriendList = (ModelInvitation) clsGson;
        if (modelFriendList != null) {
            int status = modelFriendList.getStatus();
            if (status == 200) {
                lstFriendlist.setAdapter(new FriendListAdapter(getActivity(), modelFriendList));
            } else {
                BaseFragment.toast(getActivity(), modelFriendList.getMessage());
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedFriendName = modelFriendList.getUsers().get(position).getFullname();
        selectedFriendID = modelFriendList.getUsers().get(position).getId();
        FriendListDialogFragment.this.view.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub
        super.onDismiss(dialog);
//		Toast.makeText(getActivity(),  ""+ SelectedLocation,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        if (selectedFriendName != null) {
            intent.putExtra("FriendName", selectedFriendName);
            intent.putExtra("FriendID", selectedFriendID);
        }
        getTargetFragment().onActivityResult(100, 0, intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frg_friend_list_ll_back:
                FriendListDialogFragment.this.view.dismiss();
                break;
        }
    }
}
