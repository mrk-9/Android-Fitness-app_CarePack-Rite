package com.pooja.carepack.fragments;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.fitness.result.SessionStopResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pooja.carepack.R;
import com.pooja.carepack.activities.SplashActivity;
import com.pooja.carepack.utils.MyPrefs;
import com.rey.material.widget.Button;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class WorkoutAddCurrentFragment extends BaseFragment implements GoogleMap.OnInfoWindowClickListener {

//    public int menu_item = 6;
    private View view;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private MyPrefs prefs;
    private Button pause, stop;
    private Menu menu;
    private TextView  tvDistance;
    private Chronometer chTimer;
    private GoogleApiClient client;
    private boolean authInProgress;
    private int REQUEST_OAUTH = 1;
    private Double latitude;
    private Double longitude;
    private ArrayList<LatLng> points;
    private PolylineOptions polylineOptions;
    private ArrayList<String> sessionSpeedList, sessionDistanceList, sessionStepsList;
    private long sessionStartMiliseconds,sessionEndMiliseconds;
    private String sessionName;
    private String sessionActivity;
    private int totalSteps = 0;
    private float totalDistance = 0;
    private boolean sessionPause = false;
    private String sessionIdentifier;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_workout_add_current, container, false);
        initUI();
        Bundle bundle = getArguments();
        if (bundle != null) {
            sessionName = bundle.getString("SessionName");
            sessionActivity = bundle.getString("SessionActivity");
//            sessionStartMiliseconds=convertTimeToMiliSec(sessionTime);
//            startTimer(sessionStartMiliseconds);
        }

//        setHasOptionsMenu(true);
        startSessionClient(1);
        return view;
    }

    private long convertTimeToMiliSec(String sessionTime) {
        String[] splitTime = sessionTime.split(":");

        int sessionMiliseconds=0;
        if (splitTime.length == 3) {
            sessionMiliseconds = Integer.parseInt(splitTime[0]) * 60 * 60 * 1000 + Integer.parseInt(splitTime[1]) * 60 * 1000 + Integer.parseInt(splitTime[2]) * 1000;
            android.util.Log.d("TAG 3", "sessionMiliseconds   " + sessionMiliseconds);
        } else if (splitTime.length == 2) {
            sessionMiliseconds = Integer.parseInt(splitTime[0]) * 60 * 1000 + Integer.parseInt(splitTime[1]) * 1000;
            android.util.Log.d("TAG 2", "sessionMiliseconds   " + sessionMiliseconds);
        } else {
            sessionMiliseconds = Integer.parseInt(splitTime[0]) * 1000;
            android.util.Log.d("TAG 1", "sessionMiliseconds   " + sessionMiliseconds);
        }
        return sessionMiliseconds;
    }

    private void initUI() {
        points = new ArrayList<LatLng>();
        sessionStepsList = new ArrayList<String>();
        sessionDistanceList = new ArrayList<String>();
        sessionSpeedList = new ArrayList<String>();

//        tvTimer = (TextView) view.findViewById(R.id.frg_workour_add_current_tvTimer);
        tvDistance = (TextView) view.findViewById(R.id.frg_workout_add_current_tvDistance);
        chTimer = (Chronometer) view.findViewById(R.id.frg_workour_add_current_tvTimer);
        chTimer.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new MyPrefs(getActivity());
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stop = (Button) view.findViewById(R.id.workout_add_current_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chTimer.stop();

//                startSessionClient(0);
                Fitness.SessionsApi.stopSession(client, sessionIdentifier)
                        .setResultCallback(new ResultCallback<SessionStopResult>() {
                            @Override
                            public void onResult(SessionStopResult sessionStopResult) {
                                final Status status = sessionStopResult.getStatus();
                                if (status.isSuccess()) {
                                    toast("Current Session Stop Successfully..");
                                } else {
                                    toast("session stop failed");
                                }
                            }
                        });
                getActivity().onBackPressed();
                sessionPause = true;
                if(countDownTimer!=null){
                    android.util.Log.e("TAG","CountDowntimer cancle");
                    countDownTimer.cancel();
                }
//                ((MainActivity) getActivity()).addFragment(new WorkoutAddCurrentFragment(),"Current Workout");
            }
        });
        pause = (Button) view.findViewById(R.id.workout_add_current_pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chTimer.stop();
                sessionPause = true;
                if(countDownTimer!=null){
                    android.util.Log.e("TAG","CountDowntimer cancle");
                    countDownTimer.cancel();
                }
//                ((MainActivity) getActivity()).addFragment(new WorkoutAddCurrentFragment(),                        "Current Workout");
            }
        });
    }

//    private void startTimer(long sessionStartMiliseconds) {
//
//        countDownTimer=new CountDownTimer(sessionStartMiliseconds, 1000) { // adjust the milli seconds here
//
//            public void onTick(long millisUntilFinished) {
//
//                tvTimer.setText("" + String.format("%02d:%02d:%02d",
//                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
//                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
//                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//            }
//
//            public void onFinish() {
//                tvTimer.setText("done");
//                Fitness.SessionsApi.stopSession(client, sessionIdentifier)
//                        .setResultCallback(new ResultCallback<SessionStopResult>() {
//                            @Override
//                            public void onResult(SessionStopResult sessionStopResult) {
//                                final Status status = sessionStopResult.getStatus();
//                                if (status.isSuccess()) {
//                                    toast("Current Session Stop Successfully..");
//                                } else {
//                                    toast("session stop failed");
//                                }
//                            }
//                        });
//                getActivity().onBackPressed();
//                sessionPause = true;
//            }
//        }.start();
//    }


    @Override
    public void onResume() {
        super.onResume();
//        if (menu != null && menu.size() > menu_item) {
//            menu.getItem(menu_item).setVisible(true);
//        }
        if (mMap != null) {
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
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setOnInfoWindowClickListener(WorkoutAddCurrentFragment.this);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        Log.d("Map Loaded " + Double.parseDouble(prefs.get(MyPrefs.keys.LAT)) + " " + Double.parseDouble(prefs.get(MyPrefs.keys.LNG)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(prefs.get(MyPrefs.keys.LAT)), Double.parseDouble(prefs.get(MyPrefs.keys.LNG))), 13));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (menu != null && menu.size() > menu_item)
//            menu.getItem(menu_item).setVisible(false);
//    }
//
//    @SuppressLint("NewApi")
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_menu, menu);
//        this.menu = menu;
//        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        menu.getItem(menu_item).setVisible(true);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.main_menu_music:
//                toast("Music");
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public void subscribe() {

        Fitness.RecordingApi.subscribe(client, DataType.TYPE_ACTIVITY_SAMPLE)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                android.util.Log.i("TAG", "Existing subscription for activity detected.");
                            } else {
                                android.util.Log.i("TAG", "Successfully subscribed!");
                            }
                        } else {
                            android.util.Log.i("TAG", "There was a problem subscribing.");
                        }
                    }
                });
        Fitness.RecordingApi.subscribe(client, DataType.TYPE_LOCATION_TRACK)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                android.util.Log.i("TAG", "Existing subscription for activity detected.");
                            } else {
                                android.util.Log.i("TAG", "Successfully subscribed!");
                            }
                        } else {
                            android.util.Log.i("TAG", "There was a problem subscribing.");
                        }
                    }
                });
        Fitness.RecordingApi.subscribe(client, DataType.TYPE_DISTANCE_DELTA)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                android.util.Log.i("TAG", "Existing subscription for activity detected.");
                            } else {
                                android.util.Log.i("TAG", "Successfully subscribed!");
                            }
                        } else {
                            android.util.Log.i("TAG", "There was a problem subscribing.");
                        }
                    }
                });
        Fitness.RecordingApi.subscribe(client, DataType.TYPE_CALORIES_CONSUMED)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                android.util.Log.i("TAG", "Existing subscription for activity detected.");
                            } else {
                                android.util.Log.i("TAG", "Successfully subscribed!");
                            }
                        } else {
                            android.util.Log.i("TAG", "There was a problem subscribing.");
                        }
                    }
                });

    }


    private void startSessionClient(final int status) {
        client = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                android.util.Log.e("TAG", "Connected   " + bundle);
                                subscribe();
//                                if (status == 0)
                                new startSessionSync().execute();
//                                if (status == 1)
                                findSensorData();
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

    private void redrawLine(ArrayList<LatLng> points) {
        android.util.Log.e("TAG", "redraw line call ");
        android.util.Log.e("TAG", "Lat Lng  points size " + points.size());
        mMap.clear();  //clears all Markers and Polylines

        if (mMap != null && points!=null) {
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

    //get data using sensor
    private void findSensorData() {
        Fitness.SensorsApi.findDataSources(client, new DataSourcesRequest.Builder()
                .setDataTypes(
                        DataType.AGGREGATE_ACTIVITY_SUMMARY,
                        DataType.TYPE_ACTIVITY_SAMPLE,
                        DataType.TYPE_LOCATION_SAMPLE,
                        DataType.TYPE_DISTANCE_DELTA,DataType.TYPE_CALORIES_CONSUMED,DataType.AGGREGATE_CALORIES_EXPENDED,
                        DataType.TYPE_CALORIES_EXPENDED, DataType.TYPE_SPEED, DataType.TYPE_HEART_RATE_BPM)
                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {


                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {

//                        datasources.clear();
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Device device = dataSource.getDevice();
                            String fields = dataSource.getDataType().getFields().toString();
//                            datasources.add(device.getManufacturer() + " " + device.getModel() + " [" + dataSource.getDataType().getName() + " " + fields + "]");
//                            android.util.Log.i("TAG", "DATA SOURCE     " + device.getModel() + " [" + dataSource.getDataType().getName() + " " + fields + "]");

                            final DataType dataType = dataSource.getDataType();
                            if (dataType.equals(DataType.TYPE_LOCATION_SAMPLE) ||
                                    dataType.equals(DataType.TYPE_DISTANCE_DELTA) ||
                                    dataType.equals(DataType.TYPE_SPEED) ||
                                    dataType.equals(DataType.TYPE_HEART_RATE_BPM) ||
                                    dataType.equals(DataType.AGGREGATE_CALORIES_EXPENDED) ||
                                    dataType.equals(DataType.TYPE_CALORIES_CONSUMED) ||
                                    dataType.equals(DataType.TYPE_CALORIES_EXPENDED)) {

                                Fitness.SensorsApi.add(client,
                                        new SensorRequest.Builder()
                                                .setDataSource(dataSource)
                                                .setDataType(dataSource.getDataType())
                                                .setSamplingRate(1, TimeUnit.MINUTES)
                                                .build(),
                                        new OnDataPointListener() {
                                            @Override
                                            public void onDataPoint(DataPoint dataPoint) {
                                                String msg = "onDataPoint:   " + dataPoint.getDataType();
                                                for (Field field : dataPoint.getDataType().getFields()) {
                                                    Value value = dataPoint.getValue(field);
                                                    msg += "=========>  Value  :  " + field + "=" + value + ", ";
                                                    if (!sessionPause) {
                                                        if (field.getName().equals("latitude")) {
                                                            latitude = Double.valueOf(String.valueOf(value));
                                                        } else if (field.getName().equals("longitude")) {
                                                            longitude = Double.valueOf(String.valueOf(value));
                                                        } else if (field.getName().equals("steps")) {
                                                            totalSteps = totalSteps + Integer.parseInt(String.valueOf(value));
//                                                        sessionStepsList.add(String.valueOf(value));
                                                        } else if (field.getName().equals("speed")) {
                                                            sessionSpeedList.add(String.valueOf(value));
                                                        } else if (field.getName().equals("distance")) {
                                                            totalDistance = totalDistance + Float.parseFloat(String.valueOf(value));
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    tvDistance.setText(new DecimalFormat("#.##").format(totalDistance) + "");
                                                                }
                                                            });
                                                            sessionDistanceList.add(String.valueOf(value));
                                                        }

                                                        if (latitude != null && longitude != null) {
//                                                        Log.e("TAG", "Latitude   " + latitude + "  Longitude   " + longitude);
                                                            LatLng latlng = new LatLng(latitude, longitude);
                                                            points.add(latlng);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    redrawLine(points);
                                                                }
                                                            });
//
                                                        }
                                                    }
                                                }
//                                                final String finalMsg = msg;
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        toast(getActivity(), finalMsg);
//                                                    }
//                                                });
                                                android.util.Log.i("TAG", "DATA    " + msg);

                                            }
                                        })
                                        .setResultCallback(new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                if (status.isSuccess()) {
                                                    android.util.Log.i("TAG", "Listener for " + dataType.getName() + " registered");
                                                } else {
                                                    android.util.Log.i("TAG", "Failed to register listener for " + dataType.getName());
                                                }
                                            }
                                        });
                            }
                        }
//                        datasourcesListener.onDatasourcesListed();
                    }
                });

    }

    private SessionInsertRequest insertFitnessSession() {
        android.util.Log.i("TAG", "Creating a new session");
        // Setting start and end times for our run.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.MINUTE, -10);
        long startTime = endTime - sessionStartMiliseconds;

        // speed
        DataSource speedDataSource = new DataSource.Builder()
                .setAppPackageName(getActivity().getPackageName())
                .setDataType(DataType.TYPE_SPEED)
                .setName("- speed")
                .setType(DataSource.TYPE_RAW)
                .build();


        DataSet speedDataSet = DataSet.create(speedDataSource);

        if (sessionSpeedList.size() > 0) {
            for (int pos = 0; pos < sessionSpeedList.size(); pos++) {
                DataPoint speedDP = speedDataSet.createDataPoint()
                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                speedDP.getValue(Field.FIELD_SPEED).setFloat(Float.parseFloat(sessionSpeedList.get(pos)));
                speedDataSet.add(speedDP);
            }
        } else {
            DataPoint speedDP = speedDataSet.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            speedDP.getValue(Field.FIELD_SPEED).setFloat(1);
            speedDataSet.add(speedDP);
        }

//        //activity segment
        DataSource activitySegmentDataSource = new DataSource.Builder()
                .setAppPackageName(getActivity().getPackageName())
                .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                .setName("-activity segments")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet activitySegments = DataSet.create(activitySegmentDataSource);

        DataPoint activitySegmentDP = activitySegments.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        activitySegmentDP.getValue(Field.FIELD_ACTIVITY).setActivity(FitnessActivities.WALKING);
        activitySegments.add(activitySegmentDP);

        //distance
        DataSource distanceDataSource = new DataSource.Builder()
                .setAppPackageName(getActivity().getPackageName())
                .setDataType(DataType.TYPE_DISTANCE_DELTA)
                .setName("-distance")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet distances = DataSet.create(distanceDataSource);

        if (sessionDistanceList.size() > 0) {
            for (int pos = 0; pos < sessionDistanceList.size(); pos++) {
                DataPoint distanceDP = distances.createDataPoint()
                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                distanceDP.getValue(Field.FIELD_DISTANCE).setFloat(Float.parseFloat(sessionDistanceList.get(pos)));
                distances.add(distanceDP);
            }
        } else {
            DataPoint distanceDP = distances.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            distanceDP.getValue(Field.FIELD_DISTANCE).setInt(10);
            distances.add(distanceDP);
        }

        //steps
        DataSource stepsDataSource = new DataSource.Builder()
                .setAppPackageName(getActivity().getPackageName())
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("-steps")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet steps = DataSet.create(stepsDataSource);
        if (totalSteps != 0) {
            DataPoint stepsDP = steps.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            stepsDP.getValue(Field.FIELD_STEPS).setInt(totalSteps);
            steps.add(stepsDP);
        } else {
            DataPoint stepsDP = steps.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            stepsDP.getValue(Field.FIELD_STEPS).setInt(10);
            steps.add(stepsDP);
        }

        //lat
        DataSource latDataSource = new DataSource.Builder()
                .setAppPackageName(getActivity().getPackageName())
                .setDataType(DataType.TYPE_LOCATION_TRACK)
                .setName("-steps")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet lats = DataSet.create(latDataSource);

        for (int pos = 0; pos < points.size(); pos++) {
            DataPoint latsDP = lats.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            latsDP.getValue(Field.FIELD_LATITUDE).setFloat(Float.parseFloat(String.valueOf(points.get(pos).latitude)));
            latsDP.getValue(Field.FIELD_LONGITUDE).setFloat(Float.parseFloat(String.valueOf(points.get(pos).longitude)));
            lats.add(latsDP);
        }

        //lng
        DataSource lngDataSource = new DataSource.Builder()
                .setAppPackageName(getActivity().getPackageName())
                .setDataType(DataType.TYPE_LOCATION_SAMPLE)
                .setName("-steps")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet lngs = DataSet.create(lngDataSource);

        for (int pos = 0; pos < points.size(); pos++) {
            DataPoint lngsDP = lngs.createDataPoint()
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
            lngsDP.getValue(Field.FIELD_LATITUDE).setFloat(Float.parseFloat(String.valueOf(points.get(pos).longitude)));
            lngs.add(lngsDP);
        }

        Session session = new Session.Builder()
                .setName("WALK 2812")
                .setDescription("safal")
                .setIdentifier("testing" + new Random(1000))
                .setActivity(sessionActivity)
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .setEndTime(endTime, TimeUnit.MILLISECONDS)
                .build();

        // Build a session insert request
        SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
                .setSession(session)
                .addDataSet(speedDataSet)
                .addDataSet(activitySegments)
                .addDataSet(distances)
//                .addDataSet(steps)
//                .addDataSet(lats)
//                .addDataSet(lngs)
                .build();

        return insertRequest;
    }

    private Session startSession() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.YEAR, -1);
//        long startTime = cal.getTimeInMillis();
        sessionIdentifier = "sessionstart" + new Random().nextInt(1000);
        android.util.Log.d("TAG", "sessionIdentifier  ===  " + sessionIdentifier);
        //session
        Session session = new Session.Builder()
                .setName(sessionName)
                .setIdentifier(sessionIdentifier)
                .setDescription("testing")
                .setStartTime(endTime, TimeUnit.MILLISECONDS)
                .setActivity(sessionActivity)
                .build();
        return session;
    }

    private class startSessionSync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
//            SessionReadRequest readRequest = readFitnessSession();

//            SessionInsertRequest insertRequest = insertFitnessSession();
//            android.util.Log.i("TAG", "Inserting the session in the History API");
//            com.google.android.gms.common.api.Status insertStatus =
//                    Fitness.SessionsApi.insertSession(client, insertRequest)
//                            .await(1, TimeUnit.MINUTES);
//
//            // Before querying the session, check to see if the insertion succeeded.
//            if (!insertStatus.isSuccess()) {
//                android.util.Log.i("TAG", "There was a problem inserting the session: " +
//                        insertStatus.getStatusMessage());
//                return null;
//            }

            Session session = startSession();
            Fitness.SessionsApi.startSession(client, session)
                    .setResultCallback(new ResultCallback<com.google.android.gms.common.api.Status>() {
                        @Override
                        public void onResult(com.google.android.gms.common.api.Status status) {
                            if (status.isSuccess()) {
                                toast("Session start");
                            } else {
                                toast("Session Failed.");
                                android.util.Log.e("TAG", "Error starting session " + status.getStatusCode());
                                if (status.hasResolution()) {
                                    try {
                                        status.startResolutionForResult(mainActivity, 999);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            getActivity().onBackPressed();
//            toast("Session saved successfully.");
        }
    }


//    public class CounterClass extends CountDownTimer {
//        public CounterClass(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        @Override
//        public void onFinish() {
//            textViewTime.setText("Completed.");
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            long millis = millisUntilFinished;
//            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
//                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
//                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//            System.out.println(hms);
//            textViewTime.setText(hms);
//        }
//    }

}
//    public void startFitDataSession(FitDataTypeSetting[] dataTypeSettings, String sessionDescription, OnDataPointListener listener) {
//        for (FitDataTypeSetting dataTypeSetting : dataTypeSettings) {
//            registerFitDataListener(dataTypeSetting, listener);
//            startRecordingFitData(dataTypeSetting);
//        }
//        Session session = new Session.Builder()
//                .setName(SESSION_NAME)
//                .setDescription(sessionDescription)
//                .setActivity(FitnessActivities.RUNNING_JOGGING)
//                .setStartTime(System.currentTimeMillis(), TimeUnit.MILLISECONDS).build();
//        PendingResult<Status> pendingResult = Fitness.SessionsApi.startSession(mGoogleApiClient, session);
//        pendingResult.setResultCallback(new FitResultCallback<Status>(this, FitResultCallback.RegisterType.SESSION, null, true));
//    }

//            Session session = startSession();
//
//            PendingResult<com.google.android.gms.common.api.Status> pendingResult =
//                    Fitness.SessionsApi.startSession(client, session);
//
//            pendingResult.setResultCallback(new ResultCallbacks<com.google.android.gms.common.api.Status>() {
//                @Override
//                public void onSuccess(com.google.android.gms.common.api.Status status) {
//                    android.util.Log.d("TAG STAT MSG S", status.getStatusMessage()+"****"+status.describeContents());
//                    android.util.Log.d("TAG STAT S", status.getStatus() + "***");
//                }
//
//                @Override
//                public void onFailure(com.google.android.gms.common.api.Status status) {
//                    android.util.Log.d("TAG STAT MSG F", status.getStatusMessage()+"****"+status.describeContents());
//                    android.util.Log.d("TAG STAT F", status.getStatus() + "***");
//
//                }
//            });


//    private Session startSession() {
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        cal.setTime(now);
//        long endTime = cal.getTimeInMillis();
////        cal.add(Calendar.YEAR, -1);
////        long startTime = cal.getTimeInMillis();
//
//        //session
//        Session session = new Session.Builder()
//                .setName("WALK 271")
//                .setIdentifier("test")
//                .setDescription("testing")
//                .setStartTime(endTime, TimeUnit.MILLISECONDS)
//                .setActivity(FitnessActivities.WALKING)
//                .build();
//        return session;
//    }

//    private void startTimer() {
//
//        new CountDownTimer(0, 1000) { // adjust the milli seconds here
//
//            public void onTick(long millisUntilFinished) {
//
//                tvTimer.setText("" + String.format("%02d:%02d:%02d",
//                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
//                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
//                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//            }
//
//            public void onFinish() {
//                tvTimer.setText("done!");
//            }
//        }.start();
//    }