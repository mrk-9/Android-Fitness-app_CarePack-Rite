package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.utils.MyMarkerView;
import com.pooja.carepack.utils.MyPrefs;
import com.rey.material.app.Dialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;


/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class FitnessFragment extends BaseFragment implements OnChartValueSelectedListener, View.OnClickListener {

    private static final String TAG = "TAG";
    private BarChart mChart;

    private View view;
    private int[] chartFilter = {R.id.fitness_steps_chart, R.id.fitness_workout_chart, R.id.fitness_sleep_chart, R.id.fitness_weight_chart};
    private LinearLayout workout;
    private GoogleApiClient client;
    private int REQUEST_OAUTH = 1;
    private boolean authInProgress;
    private OnDataPointListener mListener;
    private TextView tvWeight, tvSteps, tvWorkout;
    private int steps = 0;
    private ArrayList<String> stepsList;
    private ArrayList<Float> weightList;
    private ArrayList<String> dateList;
    private int totalWorkOut = 0;
    private int tempActivity = 0;
    private ProgressDialog pDialog;
    private int tempDist = 0;
    private ArrayList<Float> sessionDistance;
    private int menu_item = 8;
    private Menu menu;
    private String unit = "m";
    private float tempSessionDist = 0f;
//    private TextView steps,workout,sleep,awalk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_fitness, container, false);
        android.util.Log.d("TAG", "On CREATE");
        totalWorkOut = 0;
        setHasOptionsMenu(true);
        initUI();
        return view;
    }

    private void initUI() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait");
        tvWeight = (TextView) view.findViewById(R.id.frg_fitness_tvWeight);
        tvSteps = (TextView) view.findViewById(R.id.frg_fitness_tvSteps);
        tvWorkout = (TextView) view.findViewById(R.id.frg_fitness_tvWorkout);
        stepsList = new ArrayList<String>();
        weightList = new ArrayList<Float>();
        dateList = new ArrayList<String>();
        sessionDistance = new ArrayList<Float>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.util.Log.d("TAG", "ON ACTIVITY CREATE");
        workout = (LinearLayout) view.findViewById(R.id.ll_fitness_workout);
        workout.setOnClickListener(this);
        for (int id : chartFilter) {
            ((TextView) view.findViewById(id)).setOnClickListener(this);
        }

        mChart = (BarChart) view.findViewById(R.id.fitness_chart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

        mChart.setMarkerView(mv);


        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xl = mChart.getXAxis();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);

        mChart.getAxisRight().setEnabled(false);

//        setDatas(7, 50);
        pDialog.show();
        buildHistoryApiClient();
        setDatas(7, true, false, false, false);

    }


    private void setDatas(int count, boolean workout, boolean weight, boolean sleep, boolean steps) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i + 1) + "");
        }
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        if (workout)
            dataSets.add(addBarData(getWorkout(count), "WORKOUT", Color.parseColor
                    (workout ? "#7D9AA4" : "#ffffff")));
        if (weight)
            dataSets.add(addBarData(getWeight(count), "WEIGHT", Color.parseColor
                    (weight ? "#7D7AC2" : "#ffffff")));
        if (sleep)
            dataSets.add(addBarData(getSleep(count), "SLEEP", Color.parseColor
                    (sleep ? "#3499D6" : "#ffffff")));
        if (steps)
            dataSets.add(addBarData(getSteps(count), "STEPS", Color.parseColor
                    (steps ? "#F5B183" : "#ffffff")));

        BarData data = new BarData(xVals, dataSets);

        mChart.setData(data);
        mChart.animateY(1000);
        mChart.invalidate();
    }

    private float getRandom(int mSeekBarX) {
        float mult = mSeekBarX * 1000f;
        return (float) (Math.random() * mult) + 3;
    }

    private BarDataSet addBarData(ArrayList<BarEntry> yVals, String title, int color) {
        BarDataSet set = new BarDataSet(yVals, title);
        set.setColor(color);
        set.setBarSpacePercent(50f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private ArrayList<BarEntry> getSteps(int count) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        if (stepsList.size() > 0) {
            if (stepsList.size() > 7) {
                for (int i = 0; i < stepsList.size() - 7; i++) {
                    stepsList.remove(i);
                }
            } else {
                for (int i = 0; i < count; i++) {
                    if (i >= stepsList.size()) {
                        stepsList.add("0");
                    }
                    yVals1.add(new BarEntry(Float.parseFloat(stepsList.get(i)), i));
                }
            }
        }
        mChart.invalidate();
        return yVals1;
    }

    private ArrayList<BarEntry> getWorkout(int count) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<Float> temp = new ArrayList<Float>();
        if (unit == "km") {
            for (int pos = 0; pos < sessionDistance.size(); pos++) {
                temp.add(sessionDistance.get(pos) / 1000);
            }
//            if (temp.size() > 0) {
//                sessionDistance = temp;
//            }
        } else if (unit == "mi") {
            for (int pos = 0; pos < sessionDistance.size(); pos++) {
                temp.add((float) (sessionDistance.get(pos) / 1609.34));
            }
//            if (temp.size() > 0) {
//                sessionDistance = temp;
//            }
        } else if (unit == "m") {
            temp = sessionDistance;
        }

        if (temp.size() > 0) {
            if (temp.size() > count) {
                for (int i = 0; i < temp.size() - count; i++) {
                    temp.remove(i);
                }
            } else {
                for (int i = 0; i < count; i++) {
                    if (i >= temp.size()) {
                        temp.add(0f);
                    }
                    yVals1.add(new BarEntry(temp.get(i), i));
                }
            }
        }

        mChart.invalidate();
        return yVals1;
    }

    private ArrayList<BarEntry> getSleep(int count) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry(getRandom(5), i));
        }
        return yVals1;
    }

    private ArrayList<BarEntry> getWeight(int count) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        if (weightList.size() > 0) {
            if (weightList.size() > count) {
                for (int i = 0; i < weightList.size() - count; i++) {
                    weightList.remove(i);
                }
            } else {
                for (int i = 0; i < count; i++) {
                    if (i >= weightList.size()) {
                        weightList.add(weightList.get(i - 1));
                    }
                    yVals1.add(new BarEntry(weightList.get(i), i));
                }
            }
        }
        mChart.invalidate();
        return yVals1;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fitness_workout_chart:
//                toast("Workout tap");
                setDatas(7, true, false, false, false);
                break;
            case R.id.fitness_weight_chart:
//                toast("weight tap");
                setDatas(7, false, true, false, false);
                break;
            case R.id.fitness_sleep_chart:
//                toast("sleep tap");
                setDatas(7, false, false, true, false);
                break;
            case R.id.fitness_steps_chart:
//                toast("steps tap");
                setDatas(7, false, false, false, true);
                break;
            case R.id.ll_fitness_workout:
                ((MainActivity) getActivity()).replaceFragment(new WorkoutFragment(), "Workouts");
                break;


        }
    }

    private void buildHistoryApiClient() {
        client = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                android.util.Log.e("TAG", "Connected   " + bundle);

//                                new readData().execute(index);
                                historyApi(historyApiReadRequest());
                                historyApi(readHeight());
                                new getToatlWorkout().execute();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                android.util.Log.d("TAG", "Connection suspended");
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    android.util.Log.d("TAG", "Connection lost. Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    android.util.Log.d("TAG", "Connection lost. Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                android.util.Log.d("TAG", "Connection failed. Cause: " + result.toString());
                                if (!result.hasResolution()) {
                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(), 0).show();

                                    return;
                                }

                                if (!authInProgress) {
                                    try {
                                        android.util.Log.d("TAG", "Attempting to resolve failed connection");
                                        authInProgress = true;
                                        result.startResolutionForResult(getActivity(), REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        android.util.Log.d("TAG", "Exception while starting resolution activity: " + e.getMessage());
                                        client.connect();
                                    }
                                }
                            }

                        }
                )
                .build();
        client.connect();


    }


    private DataReadRequest historyApiReadRequest() {

//        final long ONEHOUR = 60 * 60 * 1000;
//        final long ONEDAY = ONEHOUR * 24;
        long startTime = 0;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();


        cal.set(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        startTime = cal.getTimeInMillis();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.setTimeInMillis(startTime);
        String strStartTime = formatter.format(cal.getTime());
        android.util.Log.e("TAG", "fitness frg Start Date   " + strStartTime);


        DataReadRequest readRequest = new DataReadRequest.Builder()
//                .aggregate(DataType.TYPE_WEIGHT, DataType.AGGREGATE_WEIGHT_SUMMARY)
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return readRequest;
    }

    private DataReadRequest readHeight() {
        long startTime = 0;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        startTime = cal.getTimeInMillis();

        DataReadRequest readHeightRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_HEIGHT)
                .read(DataType.TYPE_WEIGHT)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .setLimit(7)
                .build();
        return readHeightRequest;
    }

    private void historyApi(DataReadRequest readRequest) {
//        strAVG = new ArrayList<String>();
        Fitness.HistoryApi.readData(client, readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {

                if (dataReadResult.getBuckets().size() > 0) {
                    android.util.Log.e(TAG, "BUCKET DataSet.size(): " + dataReadResult.getBuckets().size());
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<com.google.android.gms.fitness.data.DataSet> dataSets = bucket.getDataSets();
//                        android.util.Log.e(TAG, "BUCKET dataSets size : " + dataSets.size());
                        for (com.google.android.gms.fitness.data.DataSet dataSet : dataSets) {
                            android.util.Log.e(TAG, "BUCKET dataSet size : " + dataSet.getDataPoints().size());
                            for (DataPoint dp : dataSet.getDataPoints()) {
                                describeDataPoint(dp, getTimeInstance());
                            }
                        }
                    }
                } else if (dataReadResult.getDataSets().size() > 0) {
//                    android.util.Log.e(TAG, "DATASET dataSet.size(): " + dataReadResult.getDataSets().size());
                    for (com.google.android.gms.fitness.data.DataSet dataSet : dataReadResult.getDataSets()) {
//                        android.util.Log.e(TAG, "DATASET dataType: " + dataSet.getDataType().getName());

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp, getTimeInstance());
                        }
                    }
                }

            }
        });
    }

    public void describeDataPoint(DataPoint dp, DateFormat dateFormat) {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String msg = "dataPoint:   "
                + "   type: " + dp.getDataType().getName() + "\n"
                + ", range: [" + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + "-" + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + "]\n"
                + ", fields: [";//+dp.getDataSource().getName()+"   SPEED   "+dp.getValue(Field.FIELD_SPEED)+"   SIZE   "+dp.getDataType().getFields().size();

        for (Field field : dp.getDataType().getFields()) {
            msg += field.getName() + "===" + dp.getValue(field) + " ";
            if (field.getName().equals("average")) {
//                weightList.add(Float.parseFloat(String.valueOf(dp.getValue(field))));
            }
            if (field.getName().equals("steps")) {
                steps = Integer.parseInt(String.valueOf(dp.getValue(field)));
                stepsList.add(String.valueOf(steps));
                tvSteps.setText(String.valueOf(steps));
            }
            if(field.getName().equals("height")){
                prefs.set(MyPrefs.keys.HEIGHT,String.valueOf(dp.getValue(field)));
            }
            if(field.getName().equals("weight")){
                prefs.set(MyPrefs.keys.WEIGHT,String.valueOf(dp.getValue(field)));
                tvWeight.setText(String.valueOf(dp.getValue(field)));
                weightList.add(Float.parseFloat(String.valueOf(dp.getValue(field))));
            }
        }

        msg += "]";
        android.util.Log.e(TAG, "DATA POINT +++++++++" + msg);
        android.util.Log.e(TAG, "STEPS +++++++++" + steps);
    }

    private SessionReadRequest readFitnessSession() {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();

        // Build a session read request
        SessionReadRequest readRequest = new SessionReadRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .readSessionsFromAllApps()
                .read(DataType.TYPE_ACTIVITY_SEGMENT)
                .read(DataType.TYPE_DISTANCE_DELTA)
                .build();

        return readRequest;
    }

    private void dumpDataSet(DataSet dataSet) {
        android.util.Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        for (DataPoint dp : dataSet.getDataPoints()) {
            DateFormat dateFormat = getTimeInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            for (Field field : dp.getDataType().getFields()) {
                android.util.Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                String value = String.valueOf(dp.getValue(field));

                if (field.getName().equals("activity") && (!value.equals("4")) && (!value.equals("3"))) {
                    if (value.equals("7") || value.equals("8") || value.equals("1")) {
                        if (tempActivity != 1) {

                            totalWorkOut += 1;
                            tempActivity = 1;
                            tempDist = 1;
                        }
//
                    }
                }
                if (tempDist == 1) {
                    if (field.getName().equals("distance")) {
                        android.util.Log.d("TAG", "inner2 DISTANCE  ***   " + value);
                        tempSessionDist = tempSessionDist + Float.valueOf(String.valueOf(dp.getValue(field)));
                        tempDist = 0;
                    }
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(menu_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_setUnit:
                showUnitDialog();
                break;
            default:
                break;
        }
        return true;
    }

    private void showUnitDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.choose_unit_picker_dialog);
//        dialog.setTitle("Title...");

        final com.rey.material.widget.LinearLayout llMeter = (com.rey.material.widget.LinearLayout) dialog.findViewById(R.id.choose_unit_dialog_llmeter);
        final com.rey.material.widget.LinearLayout llKm = (com.rey.material.widget.LinearLayout) dialog.findViewById(R.id.choose_unit_dialog_llkm);
        final com.rey.material.widget.LinearLayout llMiles = (com.rey.material.widget.LinearLayout) dialog.findViewById(R.id.choose_unit_dialog_llmiles);

        llMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unit = "m";
                dialog.dismiss();
            }
        });
        llKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unit = "km";
                dialog.dismiss();
            }
        });
        llMiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unit = "mi";
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class getToatlWorkout extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SessionReadRequest readRequest = readFitnessSession();

            SessionReadResult sessionReadResult =
                    Fitness.SessionsApi.readSession(client, readRequest)
                            .await(1, TimeUnit.MINUTES);

            // Get a list of the sessions that match the criteria to check the result.
            android.util.Log.i("TAG", "Session read was successful. Number of returned sessions is: " + sessionReadResult.getSessions().size());
//            totalWorkOut=sessionReadResult.getSessions().size();
            for (Session session : sessionReadResult.getSessions()) {
                // Process the session
//                dumpSession(session);
                tempActivity = 0;
                tempDist = 0;
                tempSessionDist = 0;
                // Process the data sets for this session
                List<DataSet> dataSets = sessionReadResult.getDataSet(session);
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
                if (tempDist == 1 && tempActivity == 1) {
                    sessionDistance.add(0f);
                } else if (tempDist == 0 && tempActivity == 1) {
                    sessionDistance.add(tempSessionDist);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvWorkout.setText(String.valueOf(totalWorkOut));
            for (int i = 0; i < sessionDistance.size(); i++) {
                android.util.Log.d("TAG", "DISTANCE  ***   " + sessionDistance.get(i));
            }
            pDialog.dismiss();
        }
    }


}
