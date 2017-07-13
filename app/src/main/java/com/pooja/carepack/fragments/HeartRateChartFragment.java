package com.pooja.carepack.fragments;

import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.pooja.carepack.R;
import com.pooja.carepack.utils.AnimationUtil;

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
public class HeartRateChartFragment extends BaseFragment  //implements MainActivity.MyActivityResult{
{
    private static final String TAG = "TAG";
    ArrayList<String> strAVG;
    private View view;
    private LinearLayout llScroll;
    private ScrollView your_scrollview;
    private boolean expanding;
    private View expandView;
    private GoogleApiClient client = null;
    private boolean authInProgress;
    private int REQUEST_OAUTH = 1;
    private LineChart charts;
    private int[] chartId={R.id.heart_rate_charthour,1,R.id.heart_rate_chart12hour,3,R.id.heart_rate_chart24hour,5,R.id.heart_rate_chart48hour,7,R.id.heart_rate_chart7days,9};
    private String strStartTime,strEndTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_heart_rate_chart, container, false);
//        MainActivity.myActivityResult = HeartRateChartFragment.this;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        llScroll = (LinearLayout) view.findViewById(R.id.setting_scroll_layout);
        your_scrollview = (ScrollView) view.findViewById(R.id.setting_scroll_view);

        for (int index = 0; index < llScroll.getChildCount(); index++) {
            LinearLayout v = (LinearLayout) llScroll.getChildAt(index);
            v.setId(index);
            if (index % 2 == 0) {
                final int finalIndex = index;
                v.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View vv) {
                        LinearLayout nextView = (LinearLayout) view.findViewById(vv.getId() + 1);
                        if (expandView != null && vv != expandView) {
                            collapse(expandView);
                        }
                        expandView = vv;
                        if (nextView.getVisibility() == View.GONE) {
//                            Toast.makeText(getActivity(), "index " + finalIndex, Toast.LENGTH_SHORT).show();

                                buildHistoryApiClient(finalIndex);

                            expand(vv);
                        } else {
                            collapse(vv);
                        }
                    }
                });
            } else {
//                Toast.makeText(getActivity(), "index " + index, Toast.LENGTH_SHORT).show();
                setChart(v, index);
                v.setOnClickListener(null);
                v.setVisibility(View.GONE);
            }
        }

    }

    private void expand(View vv) {
        LinearLayout nextView = (LinearLayout) view.findViewById(vv.getId() + 1);
        if (nextView.getVisibility() == View.GONE) {
            LinearLayout currentView = (LinearLayout) view.findViewById(vv.getId());
            RelativeLayout rl = (RelativeLayout) currentView.getChildAt(1);
            ImageView ivArrow = (ImageView) rl.getChildAt(2);

            Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate2bottom);
            ivArrow.startAnimation(rotateAnim);
            AnimationUtil.expand(nextView);
        }
    }

    private void collapse(View vv) {
        LinearLayout nextView = (LinearLayout) view.findViewById(vv.getId() + 1);
        if (nextView.getVisibility() == View.VISIBLE) {
            LinearLayout currentView = (LinearLayout) view.findViewById(vv.getId());
            RelativeLayout rl = (RelativeLayout) currentView.getChildAt(1);
            ImageView ivArrow = (ImageView) rl.getChildAt(2);

            Animation rotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate2right);
            ivArrow.startAnimation(rotateAnim);
            AnimationUtil.collapse(nextView);
        }
    }


    public void setChart(LinearLayout chart, int finalIndex) {
        LinearLayout nextView = (LinearLayout) chart.getChildAt(0);
        LinearLayout nextView1 = (LinearLayout) nextView.getChildAt(0);
        charts = (LineChart) nextView1.getChildAt(0);
        charts.getLegend().setEnabled(true);

//        // get the legend (only possible after setting data)
        Legend l = charts.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);

        YAxis rightAxis = charts.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = charts.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);

        XAxis xAxis = charts.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.parseColor("#151E60"));
        xAxis.setDrawGridLines(true);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextColor(Color.WHITE);

        charts.setDescription("");
        charts.setBackgroundColor(Color.parseColor("#151E60"));
        charts.setDrawGridBackground(false);
//        charts.setBackgroundColor(Color.RED);
        charts.invalidate();
    }

    private LineData setData(int count, float range, ArrayList<String> strAVG) {

        android.util.Log.d(TAG, "set data()++++  "+ strAVG.size());
        for (int i = 0; i <count ; i++) {
            android.util.Log.d(TAG, "rate ++++  "+ strAVG.get(i).toString());
        }

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(0+"");
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(0, 0));
        for (int i = 1; i <= count; i++) {
            float val = Float.parseFloat(strAVG.get(i-1));
            yVals.add(new Entry(val, i));
            android.util.Log.d(TAG, "val  " + val + "  i  " + i);
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, strStartTime+" - "+strEndTime);

        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setDrawFilled(true);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.WHITE);
        set1.setDrawValues(!set1.isDrawValuesEnabled());

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        return data;
    }

    private void buildHistoryApiClient(final int index) {
        client = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                android.util.Log.e("TAG", "Connected   " + bundle);

//                                new readData().execute(index);
                                historyApi(historyApiReadRequest(index),index);
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


    private DataReadRequest historyApiReadRequest(Integer index) {

        long startTime = 0;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        final long ONEHOUR=60*60*1000;
        final long ONEDAY=ONEHOUR*24;

        if (index == 0) {
            startTime = endTime-(ONEHOUR);
        } else if (index == 2) {
            startTime = endTime-(ONEHOUR*12);
        } else if (index == 4) {
            startTime = endTime-(ONEDAY);
        } else if (index == 6) {
            startTime = endTime-(ONEDAY*2);
        }else if (index == 8) {
            startTime = endTime-(ONEDAY*7);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.setTimeInMillis(startTime);
        strStartTime=formatter.format(cal.getTime());
        cal.setTimeInMillis(endTime);
        strEndTime=formatter.format(cal.getTime());
//        formatter.setLenient(false);
//        String oldTime = "2016-01-11 12:00:00";
//        Date oldDate = null;
//        try {
//            oldDate = formatter.parse(oldTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long startTime = oldDate.getTime();


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                .bucketByTime(1, TimeUnit.HOURS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return readRequest;
    }

    private void historyApi(DataReadRequest readRequest, final int index) {
            strAVG=new ArrayList<String>();
        Fitness.HistoryApi.readData(client, readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(DataReadResult dataReadResult) {

                if (dataReadResult.getBuckets().size() > 0) {
                    android.util.Log.e(TAG, "BUCKET DataSet.size(): " + dataReadResult.getBuckets().size());
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
//                        android.util.Log.e(TAG, "BUCKET dataSets size : " + dataSets.size());
                        for (DataSet dataSet : dataSets) {
                            android.util.Log.e(TAG, "BUCKET dataSet size : " + dataSet.getDataPoints().size());
                            for (DataPoint dp : dataSet.getDataPoints()) {
                                describeDataPoint(dp, getTimeInstance(), index);
                            }
                        }
                    }
                } else if (dataReadResult.getDataSets().size() > 0) {
//                    android.util.Log.e(TAG, "DATASET dataSet.size(): " + dataReadResult.getDataSets().size());
                    for (DataSet dataSet : dataReadResult.getDataSets()) {
//                        android.util.Log.e(TAG, "DATASET dataType: " + dataSet.getDataType().getName());

                        for (DataPoint dp : dataSet.getDataPoints()) {
                            describeDataPoint(dp, getTimeInstance(),index);
                        }
                    }
                }

            }
        });
    }

    public void describeDataPoint(DataPoint dp, DateFormat dateFormat,int index) {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String msg = "dataPoint:   "+index
                + "   type: " + dp.getDataType().getName() + "\n"
                + ", range: [" + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + "-" + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + "]\n"
                + ", fields: [";//+dp.getDataSource().getName()+"   SPEED   "+dp.getValue(Field.FIELD_SPEED)+"   SIZE   "+dp.getDataType().getFields().size();

        for (Field field : dp.getDataType().getFields()) {
            msg += field.getName() + "===" + dp.getValue(field) + " ";
            if (field.getName().equals("average")) {
                strAVG.add(String.valueOf(dp.getValue(field)));
            }
        }
        if(strAVG.size()>0){
            charts= (LineChart) view.findViewById(chartId[index]);
            charts.setData(setData(strAVG.size(), 10, strAVG));
            charts.invalidate();
        }

        msg += "]";
        android.util.Log.e(TAG, "DATA POINT +++++++++" + msg);
    }


//    private class readData extends AsyncTask<Integer, Void, Void> {
//        protected Void doInBackground(Integer... params) {
//            DataReadRequest readRequest = historyApiReadRequest(params[0]);
//
//
//            DataReadResult dataReadResult =
//                    Fitness.HistoryApi.readData(client, readRequest).await(1, TimeUnit.MINUTES);
//
//            if (dataReadResult.getBuckets().size() > 0) {
//                android.util.Log.i(TAG, "Number of returned buckets of DataSets is: "
//                        + dataReadResult.getBuckets().size());
//                for (Bucket bucket : dataReadResult.getBuckets()) {
//                    List<DataSet> dataSets = bucket.getDataSets();
//                    for (DataSet dataSet : dataSets) {
////                        dumpDataSet(dataSet);
//                        android.util.Log.i(TAG, "BUCKET Number of returned DataSet size  ***  " + dataSet.getDataPoints().size());
//                        for (DataPoint dp : dataSet.getDataPoints()) {
//                            describeDataPoint(dp, getTimeInstance());
//                        }
//                    }
//                }
//            } else if (dataReadResult.getDataSets().size() > 0) {
//                android.util.Log.i(TAG, "Number of returned DataSets is: "
//                        + dataReadResult.getDataSets().size());
//                for (DataSet dataSet : dataReadResult.getDataSets()) {
////                    dumpDataSet(dataSet);
//                    android.util.Log.i(TAG, "Number of returned DataSet size  ***  " + dataSet.getDataPoints().size());
//                    for (DataPoint dp : dataSet.getDataPoints()) {
//                        describeDataPoint(dp, getTimeInstance());
//                    }
//                }
//            }
//
//            return null;
//        }
//
//    }

//    @Override
//    public void myOnActivityResult(int requestCode, int resultCode, Intent data) {
//        android.util.Log.d(TAG,"frg onActivityResult: REQUEST_OAUTH    "+requestCode+"     "+resultCode);
//        if (requestCode == REQUEST_OAUTH) {
////            authInProgress = false;
//            if (resultCode == Activity.RESULT_OK) {
//                // Make sure the app is not already connected or attempting to connect
//                if (!client.isConnecting() && !client.isConnected()) {
//                    android.util.Log.d(TAG,"onActivityResult: client.connect()");
//                    client.connect();
//                }
//            }
//        }
//    }


}
