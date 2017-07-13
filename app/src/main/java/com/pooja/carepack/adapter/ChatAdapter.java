package com.pooja.carepack.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.dialogs.DialogFragmentZoomImage;
import com.pooja.carepack.model.ModelThread;
import com.pooja.carepack.utils.CircleImageView;
import com.pooja.carepack.utils.MyPrefs;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class ChatAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    ArrayList<ModelThread> threadList;
    private Context mContext;
    private View view;
    //    private ArrayList<ModelThreadList.Thread> threadList;
    private MyPrefs prefs;
    private CircleImageView ivProfilePic;
    private TextView tvUsername, tvMessage,tvTime;
    private ImageView ivSentImage;
    private FragmentManager fManager;


    public ChatAdapter(Context context, ArrayList<ModelThread> modelThreadList, FragmentManager supportFragmentManager) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.threadList = modelThreadList;
        mContext = context;
        this.fManager = supportFragmentManager;
        prefs = new MyPrefs(context);

    }

    @Override
    public int getCount() {
        return threadList.size();
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
        Log.d("TAG", "Chat adapter ++++++    " + threadList.get(position).getFullname().toString() + "    " + prefs.get(MyPrefs.keys.USERNAME).toString());
        if (threadList.get(position).getFullname().toString().equals(prefs.get(MyPrefs.keys.USERNAME).toString()))
            view = mInflater.inflate(R.layout.row_chat_out, null);
        else
            view = mInflater.inflate(R.layout.row_chat_in, null);

        tvUsername = (TextView) view.findViewById(R.id.row_chat_tvUsername);
        tvMessage = (TextView) view.findViewById(R.id.row_chat_tvMessage);
        ivProfilePic = (CircleImageView) view.findViewById(R.id.row_chat_ivProfilePic);
        ivSentImage = (ImageView) view.findViewById(R.id.row_chat_ivSentImage);
        tvTime=(TextView)view.findViewById(R.id.row_chat_tvTime);

        tvUsername.setText(threadList.get(position).getFullname());
        tvMessage.setText(threadList.get(position).getMessage());
        Picasso.with(mContext).load(threadList.get(position).getProfileImage()).into(ivProfilePic);
        if (threadList.get(position).getImages().size() > 0) {
            if (!threadList.get(position).getImages().get(0).getImage150().equals("")) {
                Log.d("TAG CHAt adapter  ", threadList.get(position).getImages().get(0).getImage150() + "    " + position);
                ivSentImage.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(threadList.get(position).getImages().get(0).getImage150()).into(ivSentImage);
            }
        }

        ivSentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    DialogFragment newFragment = new DialogFragmentZoomImage();
                    Bundle bundle = new Bundle();
                    if (threadList.get(position).getImages().size() > 0) {
                        if (!threadList.get(position).getImages().get(0).getImage150().equals("") && threadList.get(position).getImages().get(0).getImage150() != null) {
                            bundle.putString("ChatImagePath", threadList.get(position).getImages().get(0).getImage150());
                        }
                    }
                    newFragment.setArguments(bundle);
                    newFragment.show(fManager, "Zoom Image");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");//("dd/MM/yyyy hh:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(threadList.get(position).getUpdateddate()));
        String dateTime = formatter.format(calendar.getTime());
        tvTime.setText(dateTime);

        return view;
    }

}
