package com.pooja.carepack.adapter;

import android.content.Context;
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
public class WorkoutListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private ArrayList<String> sessionNameList;
    private ArrayList<String> sessionDateList;
    private ArrayList<Float> sessionDistance;
    private View view;
    private Context context;
    private TextView tvSessionName, tvSessionDate, tvSessionDist;

    public WorkoutListAdapter(Context context, ArrayList<String> sessionName, ArrayList<String> sessionDate, ArrayList<Float> sessionDistance) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.sessionNameList = sessionName;
        this.sessionDateList = sessionDate;
        this.sessionDistance=sessionDistance;

    }

    @Override
    public int getCount() {
        return sessionNameList.size();
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
        view = mInflater.inflate(R.layout.row_workout, null);
        initUI();
        tvSessionName.setText(sessionNameList.get(position));
        tvSessionDate.setText(sessionDateList.get(position));
        tvSessionDist.setText(new DecimalFormat("#.##").format(sessionDistance.get(position))+"m");
        return view;
    }

    private void initUI() {
        tvSessionName = (TextView) view.findViewById(R.id.row_workout_tvSessionName);
        tvSessionDate = (TextView) view.findViewById(R.id.row_workout_tvSessionDate);
        tvSessionDist = (TextView) view.findViewById(R.id.row_workout_tvSessionDistance);
    }

}
