package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.model.ModelConversion;
import com.pooja.carepack.utils.CircleImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class MessageConversionAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private View view;
    private ArrayList<ModelConversion.Thread> convertionList;
    private TextView tvUserName, tvLastMsg, tvTime;
    private CircleImageView ivUSerImage;
    private Context mContext;

    public MessageConversionAdapter(Context context, ArrayList<ModelConversion.Thread> convertionList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.convertionList = convertionList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return convertionList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_msg_conversion, null);

        tvUserName = (TextView) view.findViewById(R.id.row_msg_conversion_tvUserName);
        tvLastMsg = (TextView) view.findViewById(R.id.row_msg_conversion_tvLastMsg);
        tvTime = (TextView) view.findViewById(R.id.row_msg_conversion_tvTime);
        ivUSerImage = (CircleImageView) view.findViewById(R.id.row_msg_conversion_ivUserImage);

        tvUserName.setText(convertionList.get(position).getFullname());
        tvLastMsg.setText(convertionList.get(position).getMessage());
        Picasso.with(mContext).load(convertionList.get(position).getProfileImage()).into(ivUSerImage);

        //convert miliseconds to date and time
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");//("dd/MM/yyyy hh:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(convertionList.get(position).getUpdateddate()));
        String dateTime = formatter.format(calendar.getTime());
        tvTime.setText(dateTime);
        return view;
    }

}
