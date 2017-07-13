package com.pooja.carepack.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pooja.carepack.R;
import com.pooja.carepack.activities.SplashActivity;
import com.pooja.carepack.adapter.WorkoutRouteAdapter;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;

import java.text.DateFormat;
import java.text.DecimalFormat;
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
public class WorkoutRouteFragment extends BaseFragment implements GoogleMap.OnInfoWindowClickListener {

    public int menu_item = 7;
    private View view;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private MyPrefs prefs;
    private ListView routeDetailsList;
    private String sessionName;
    private String TAG = "TAG";
    private GoogleApiClient client;
    private boolean authInProgress;
    private int REQUEST_OAUTH = 1;
    private String totalTime;
    private float totalDistance = 0;
    private TextView tvTotalTime, tvTotalDist, tvAvgSpeed;
    private long totalSeconds;
    private ArrayList<String> speedList;
    private ArrayList<String> heartRateList;
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> points;
    private Double latitude;
    private Double longitude;
    //    private ArrayList<String> ;
    private ProgressDialog pDialog;
    private LineChart charts;
    private String sessionIdentifier;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_workout_route, container, false);
        setHasOptionsMenu(true);
        initUI();
        Bundle bundle = getArguments();
        if (bundle != null) {
            sessionName = bundle.getString("SessionName");
            totalDistance = bundle.getFloat("SessionDist");//SessionIdentifier
//            sessionIdentifier = bundle.getString("SessionIdentifier");
//            Toast.makeText(getActivity(), sessionName, Toast.LENGTH_SHORT).show();
        }
        getSessionInfo();
        return view;
    }

    private void initUI() {
        prefs = new MyPrefs(getActivity());
        points = new ArrayList<LatLng>();
        speedList = new ArrayList<String>();
        heartRateList = new ArrayList<String>();
        pDialog = new ProgressDialog(getActivity());
        pDialog.show();

        charts = (LineChart) view.findViewById(R.id.workout_chart_detail);
        routeDetailsList = (ListView) view.findViewById(R.id.workout_detail_list_route);
        tvTotalTime = (TextView) view.findViewById(R.id.frg_workout_route_tvTotalTime);
        tvTotalDist = (TextView) view.findViewById(R.id.frg_workout_route_tvTotalDistance);
        tvAvgSpeed = (TextView) view.findViewById(R.id.frg_workout_route_tvAvgSpeed);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setChart(charts);


        Location loc = SplashActivity.getNewLocation(getActivity());
        if (loc != null) {
            prefs.set(MyPrefs.keys.LAT, "" + loc.getLatitude());
            prefs.set(MyPrefs.keys.LNG, "" + loc.getLongitude());
        } else {
            prefs.set(MyPrefs.keys.LAT, "0.0");
            prefs.set(MyPrefs.keys.LNG, "0.0");
        }
        FragmentManager fm = getChildFragmentManager();
        mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView);
        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapView, mMapFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(true);
        }
        if (mMap != null) {
//            loadEvents();
            return;
        }
        try {
            int chkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
            if (chkGooglePlayServices != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(chkGooglePlayServices, getActivity(), 1122).show();
            } else {
                mMap = mMapFragment.getMap();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setOnInfoWindowClickListener(WorkoutRouteFragment.this);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        Log.d("Map Loaded " + Double.parseDouble(prefs.get(MyPrefs.keys.LAT)) + " " + Double.parseDouble(prefs.get(MyPrefs.keys.LNG)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(prefs.get(MyPrefs.keys.LAT)), Double.parseDouble(prefs.get(MyPrefs.keys.LNG))), 13));
//                        mPinOtherDesc = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_other);
//                        mPinMeDesc = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_me);
//                        showCurrentLocationOnMap();
//                        loadEvents();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public void setChart(LineChart charts) {
        charts.getLegend().setEnabled(false);
        charts.setData(setData(10, 10));

        YAxis rightAxis = charts.getAxisRight();
        rightAxis.setEnabled(false);
//        rightAxis.setDrawGridLines(true);

        YAxis leftAxis = charts.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);

        XAxis xAxis = charts.getXAxis();
//        xAxis.setEnabled(false);
        xAxis.setTextColor(Color.parseColor("#151E60"));
        xAxis.setDrawGridLines(true);

//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        charts.setDescription("");
        charts.setBackgroundColor(Color.parseColor("#151E60"));
        charts.setDrawGridBackground(false);
//        charts.setBackgroundColor(Color.RED);
        charts.invalidate();
    }

    private LineData setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(0, 0));
        for (int i = 1; i < count; i++) {

            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "");
        // set1.setFillAlpha(110);
//        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setDrawFilled(true);
//        set1.setFillColor(Color.parseColor("#151E60"));
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.WHITE);
        set1.setDrawValues(!set1.isDrawValuesEnabled());
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        return data;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
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
            case R.id.main_menu_share:
                toast("shared");
                break;
            default:
                break;
        }
        return true;
    }

    private void redrawLine(ArrayList<LatLng> points) {
        android.util.Log.e("TAG", "redraw line call ");
        android.util.Log.e("TAG", "Lat Lng  points size " + points.size());
        mMap.clear();  //clears all Markers and Polylines

        if (mMap != null) {
            android.util.Log.e("TAG", "googlemap not null");
//            MarkerOptions marker = new MarkerOptions();
//            marker.position(latlng);
//            mMap.addMarker(marker);
            // settin polyline in the map
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.BLUE);
            polylineOptions.width(10);
//            arrayPoints.add(point);
            polylineOptions.addAll(points);
            mMap.addPolyline(polylineOptions);
        }

    }

    private void getSessionInfo() {
        client = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                android.util.Log.e("TAG", "Connected   " + bundle);

                                new SessionApiAsyncRead().execute();
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

    private void dumpDataSet(DataSet dataSet) {
        android.util.Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        for (DataPoint dp : dataSet.getDataPoints()) {
            DateFormat dateFormat = getTimeInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            android.util.Log.i(TAG, "Data point:");
            android.util.Log.i(TAG, "\tType: " + dp.getDataType().getName());
            android.util.Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            android.util.Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

            for (Field field : dp.getDataType().getFields()) {
                android.util.Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                String value = String.valueOf(dp.getValue(field));
                if (field.getName().equals("latitude")) {
                    latitude = Double.valueOf(String.valueOf(value));
                } else if (field.getName().equals("longitude")) {
                    longitude = Double.valueOf(String.valueOf(value));
                } else if (field.getName().equals("speed")) {
                    android.util.Log.e("TAG", "Speed   " + value);
                    speedList.add(String.valueOf(value));
                }

                if (latitude != null && longitude != null) {
                    LatLng latlng = new LatLng(latitude, longitude);
                    points.add(latlng);
                }

            }
        }
    }

    private void dumpSession(Session session) {
        DateFormat dateFormat = getTimeInstance();
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        android.util.Log.i(TAG, "Data returned for Session: " + session.getName()
                + "\n\tDescription: " + session.getDescription()
                + "\n\tStart: " + dateFormat.format(session.getStartTime(TimeUnit.MILLISECONDS))
                + "\n\tEnd: " + dateFormat.format(session.getEndTime(TimeUnit.MILLISECONDS)));

        long differTime = session.getEndTime(TimeUnit.MILLISECONDS) - session.getStartTime(TimeUnit.MILLISECONDS);
//        dateFormat = new SimpleDateFormat("hh:mm:ss");
        totalTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(differTime),
                TimeUnit.MILLISECONDS.toMinutes(differTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(differTime)),
                TimeUnit.MILLISECONDS.toSeconds(differTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(differTime)));
        totalSeconds = TimeUnit.MILLISECONDS.toSeconds(differTime);
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
                .read(DataType.TYPE_SPEED)
                .read(DataType.TYPE_HEART_RATE_BPM)
                .read(DataType.TYPE_LOCATION_SAMPLE)
                .read(DataType.TYPE_LOCATION_TRACK)
                .read(DataType.TYPE_CALORIES_EXPENDED)
                .read(DataType.AGGREGATE_SPEED_SUMMARY)
                .read(DataType.TYPE_DISTANCE_DELTA)
                .setSessionName(sessionName)
                .build();

        // [END build_read_session_request]

        return readRequest;
    }

    private void setInfo() {
        if (totalTime != null)
            tvTotalTime.setText(totalTime);
        if (totalDistance != 0)
            tvTotalDist.setText(totalDistance + "m");
        if (totalTime != null && totalDistance != 0) {
            android.util.Log.e("TAG", "Seconds   *****    " + totalSeconds);
            float avgSpeed = totalDistance / totalSeconds;
            tvAvgSpeed.setText(new DecimalFormat("#.##").format(avgSpeed) + "m/sec");
        }
        if (points.size() > 0) {
            redrawLine(points);
        }
    }

    private class SessionApiAsyncRead extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SessionReadRequest readRequest = readFitnessSession();

            SessionReadResult sessionReadResult =
                    Fitness.SessionsApi.readSession(client, readRequest)
                            .await(1, TimeUnit.MINUTES);

            // Get a list of the sessions that match the criteria to check the result.
            android.util.Log.i("TAG", "Session read was successful. Number of returned sessions is: " + sessionReadResult.getSessions().size());
            for (Session session : sessionReadResult.getSessions()) {
                // Process the session
                dumpSession(session);

                // Process the data sets for this session
                List<DataSet> dataSets = sessionReadResult.getDataSet(session);
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            setInfo();

            //Convert speed m/s to m/min
            android.util.Log.e("TAG", "Speed List size   " + speedList.size());
            ArrayList<Float> speedPerMin = new ArrayList<Float>();
            ArrayList<String> durationList = new ArrayList<String>();
            int test=0;
            for (int pos = 0; pos < speedList.size(); pos += 60) {
                float tempSpeed = 0;
                for (int pos1 = 0; pos1 < 60; pos1++) {
                    if (pos + pos1 < speedList.size()) {
                        tempSpeed = tempSpeed + Float.parseFloat(speedList.get(pos + pos1));
                        android.util.Log.e("TAG", "tempSpeed  " + pos + "  ++  " + pos1 + "  ++  " + speedList.get(pos + pos1)+"    "+ test+++"   index   "+(pos+pos1));
                    } else {
                        android.util.Log.e("TAG", "last sec  " + pos1);
                        durationList.add(pos1+" sec");
                        break;
                    }
                }
                durationList.add("1 min");
                speedPerMin.add(tempSpeed);
            }

            if (speedPerMin.size() > 0) {
                routeDetailsList.setAdapter(new WorkoutRouteAdapter(getActivity(), speedPerMin,durationList));
                Utility.setListViewHeightBasedOnChildren(routeDetailsList, 0);
            }

        }
    }


}
