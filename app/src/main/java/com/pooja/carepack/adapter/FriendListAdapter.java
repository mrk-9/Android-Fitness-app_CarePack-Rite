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
public class FriendListAdapter extends BaseAdapter implements LibPostListner{

    private final LayoutInflater mInflater;
    private View view;
    ArrayList<ModelInvitation.User>  userFriendList;
    private ImageView ivUserImage,ivRemoveFriend;
    private TextView tvUserName,tvUserEmail;
    private Context mContext;
    private MyPrefs prefs;
    private BasicResponse basicResModel;
    private int selectedPosition;

    public FriendListAdapter(Context context, ModelInvitation modelfriendList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userFriendList= modelfriendList.getUsers();
        mContext=context;
        prefs=new MyPrefs(mContext);
    }

    @Override
    public int getCount() {
        return userFriendList.size();
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
        view = mInflater.inflate(R.layout.row_friend_list, null);
        ivUserImage=(ImageView)view.findViewById(R.id.row_friend_list_ivUserImage);
        ivRemoveFriend=(ImageView)view.findViewById(R.id.row_friend_list_ivRemoveFriend);
        tvUserName=(TextView)view.findViewById(R.id.row_friend_list_ivUserName);
        tvUserEmail=(TextView)view.findViewById(R.id.row_friend_list_ivUserEmail);

        Picasso.with(mContext).load(userFriendList.get(position).getProfileImage()).into(ivUserImage);
        tvUserName.setText(userFriendList.get(position).getFullname().toString());
        tvUserEmail.setText(userFriendList.get(position).getEmail().toString());

        ivRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFriend(position);
            }
        });

        return view;
    }

    private void removeFriend(int position) {
        selectedPosition=position;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "remove_friend");
        hashMap.put("friendid", userFriendList.get(position).getId());

        new PostLibResponse(FriendListAdapter.this, new BasicResponse(), mContext, hashMap, WebServicesConst.FRIEND_STATUS, WebServicesConst.RES.FRIEND_STATUS, true, true);
    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        basicResModel=(BasicResponse)clsGson;
        if(basicResModel!=null){
            int status= basicResModel.getStatus();
            android.util.Log.e("TAG","STATUS INVITATION RES   "+status);
            if(status==200){
                BaseFragment.toast(mContext, basicResModel.getMessage());//"Request Rejected Successfully.");
                userFriendList.remove(selectedPosition);
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
