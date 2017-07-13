package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pooja.carepack.R;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TreatmentAppointmentAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private View view;
    private Context context;
    private ImageView ivDrawerImage;
    private TextView tvTitle, tvdate;
    private CheckBox cbStatus;
    private JsonArray data;

    public TreatmentAppointmentAdapter(Context context, JsonArray data) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.context = context;
    }

    public String getId(int position) {
        JsonObject obj = (JsonObject) getItem(position);
        return obj.get("id").toString().replaceAll("\"", "");
    }

    public String getTitle(int position) {
        JsonObject obj = (JsonObject) getItem(position);
        return obj.get("title").toString().replaceAll("\"", "");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getAsJsonObject();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_treatment_appointment, null);

        tvTitle = (TextView) view.findViewById(R.id.row_treatment_appointment_tvTitle);

        view.setTag("" + getId(position));
        tvTitle.setText(getTitle(position));
        return view;
    }

}
