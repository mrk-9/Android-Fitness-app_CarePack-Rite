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
import com.pooja.carepack.utils.CircleImageView;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yudiz on 03/02/16.
 */
public class SearchUserAdapter extends BaseAdapter implements LibPostListner {

    private final LayoutInflater mInflater;
    ArrayList<ModelInvitation.User> userSearchList;
    private View view;
    private TextView tvUserName, tvUserEmail;
    private CircleImageView ivUserImage;
    private Context mContext;
    private MyPrefs prefs;
    private BasicResponse basicResModel;
    private ImageView ivSentREquest;
    private int selectedPosition;

    public SearchUserAdapter(Context context, ModelInvitation modelInvitation) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userSearchList = modelInvitation.getUsers();
        mContext = context;
        prefs=new MyPrefs(mContext);
    }

    @Override
    public int getCount() {
        return userSearchList.size();
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
        view = mInflater.inflate(R.layout.row_search_user, null);
        tvUserName = (TextView) view.findViewById(R.id.row_search_user_ivUserName);
        tvUserEmail = (TextView) view.findViewById(R.id.row_search_user_ivUserEmail);
        ivUserImage = (CircleImageView) view.findViewById(R.id.row_search_user_ivUserImage);
        ivSentREquest=(ImageView)view.findViewById(R.id.row_search_user_ivUserStatus);

        if(!userSearchList.get(position).getStatus().equals("accept")) {
            tvUserName.setText(userSearchList.get(position).getFullname());
            tvUserEmail.setText(userSearchList.get(position).getEmail());
            Picasso.with(mContext).load(userSearchList.get(position).getProfileImage()).into(ivUserImage);
        }

        ivSentREquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendRequest(position);
            }
        });

        return view;
    }

    private void sendRequest(int position) {
        selectedPosition=position;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "send_request");
        hashMap.put("friendid", userSearchList.get(position).getId());

        new PostLibResponse(SearchUserAdapter.this, new BasicResponse(), mContext, hashMap, WebServicesConst.FRIEND_STATUS, WebServicesConst.RES.FRIEND_STATUS, true, true);

    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        basicResModel=(BasicResponse)clsGson;
        if(basicResModel!=null){
            int status=basicResModel.getStatus();
            android.util.Log.e("TAG","STATUS INVITATION RES   "+status);
            if(status==200){
                BaseFragment.toast(mContext, basicResModel.getMessage());//"Request Rejected Successfully.");
//                ivSentREquest.setImageResource(R.drawable.ic_confirm);
                userSearchList.remove(selectedPosition);
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
