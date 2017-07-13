package com.pooja.carepack.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pooja.carepack.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class WorkoutRouteAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private View view;
    private ArrayList<Float> speedPerMin;
    private ArrayList<String> durationList;
    private Context mContext;
    private TextView tvWorkoutSpeed, tvNumber,tvDuration;

    public WorkoutRouteAdapter(Context context, ArrayList<Float> speedPerMin, ArrayList<String> durationList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.speedPerMin = speedPerMin;
        this.durationList=durationList;
        this.mContext = context;
        Log.d("TAG", "adt Speed Per Min   " + this.speedPerMin.size());
    }

    @Override
    public int getCount() {

        return speedPerMin.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_workout_route, null);
        tvWorkoutSpeed = (TextView) view.findViewById(R.id.row_workout_speed_tvSpeed);
        tvNumber = (TextView) view.findViewById(R.id.row_workout_speed_tvNumber);
        tvDuration=(TextView)view.findViewById(R.id.row_workout_speed_tvDuration);
        setData(position);
        return view;
    }

    private void setData(int position) {
        tvNumber.setText((position + 1) + "");
        tvWorkoutSpeed.setText(new DecimalFormat("#.##").format(speedPerMin.get(position)) + " m/min");
        tvDuration.setText(durationList.get(position).toString());
    }

}
