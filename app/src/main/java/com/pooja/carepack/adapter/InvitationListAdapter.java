package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.model.BasicResponse;
import com.pooja.carepack.model.ModelInvitation;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class InvitationListAdapter extends BaseAdapter implements LibPostListner{

    private final LayoutInflater mInflater;
    private View view;
    private ArrayList<ModelInvitation.User> userInvitationList;
    private ImageView ivUserImage,ivConfirmReq,ivRejectReq;
    private TextView tvUserName,tvUserEmail;
    private Context mContext;
    private MyPrefs prefs;
    private BasicResponse basicResModel;
    private int selectedPosition;

    public InvitationListAdapter(Context context, ModelInvitation modelInvitation) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userInvitationList=modelInvitation.getUsers();
        mContext=context;
        prefs=new MyPrefs(mContext);
    }

    @Override
    public int getCount() {
        return userInvitationList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_invitation, null);
        tvUserName=(TextView)view.findViewById(R.id.row_invitation_ivUserName);
        tvUserEmail=(TextView)view.findViewById(R.id.row_invitation_ivUserEmail);
        ivUserImage=(ImageView)view.findViewById(R.id.row_invitation_ivUserImage);
        ivConfirmReq=(ImageView)view.findViewById(R.id.row_invitation_ivConfirmReq);
        ivRejectReq=(ImageView)view.findViewById(R.id.row_invitation_ivRejectReq);

        tvUserName.setText(userInvitationList.get(position).getFullname());
        tvUserEmail.setText(userInvitationList.get(position).getEmail());
        Picasso.with(mContext).load(userInvitationList.get(position).getProfileImage()).into(ivUserImage);

        ivRejectReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(position);
            }
        });

        ivConfirmReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRequest(position);
            }
        });

        return view;
    }

    private void confirmRequest(int position) {
        selectedPosition=position;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "accept_request");
        hashMap.put("friendid", userInvitationList.get(position).getId());

        new PostLibResponse(InvitationListAdapter.this, new BasicResponse(), mContext, hashMap, WebServicesConst.FRIEND_STATUS, WebServicesConst.RES.FRIEND_STATUS, true, true);

    }

    private void rejectRequest(int position) {
        selectedPosition=position;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "reject_request");
        hashMap.put("friendid", userInvitationList.get(position).getId());

        new PostLibResponse(InvitationListAdapter.this, new BasicResponse(), mContext, hashMap, WebServicesConst.FRIEND_STATUS, WebServicesConst.RES.FRIEND_STATUS, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        basicResModel=(BasicResponse)clsGson;
        if(basicResModel!=null){
            int status=Integer.parseInt(basicResModel.getStatus().toString());
            android.util.Log.e("TAG","STATUS INVITATION RES   "+status);
            if(status==200){
               BaseFragment.toast(mContext,basicResModel.getMessage());//"Request Rejected Successfully.");
                userInvitationList.remove(selectedPosition);
                notifyDataSetInvalidated();
            }
            else{
                BaseFragment.toast(mContext,basicResModel.getMessage());
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
