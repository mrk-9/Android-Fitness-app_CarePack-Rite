package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.pooja.carepack.R;
import com.pooja.carepack.adapter.WorkoutListAdapter;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class WorkoutFragment extends BaseFragment {

    public int menu_item = 5;
    private View view;
    private ListView lv;
    private Menu menu;
    private String TAG = "TAG";
    private GoogleApiClient client;
    private boolean authInProgress;
    private int REQUEST_OAUTH = 1;
    private ArrayList<String> sessionName;
//    private ArrayList<String> sessionIdentifire;
    private ArrayList<String> sessionDate;
    private ArrayList<Float> sessionDistance;
    private ProgressDialog pDialog;
    private String strSessionName, strSessionDate;
    private int tempDist = 0, tempActivity = 0;
    private float tempSessionDist = 0;
    public static String CUR_WORKOUT_NAME="";
    private Animation shakeAnim;
//    private String strSessionIdentifier;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_workout, container, false);
        setHasOptionsMenu(true);
        intiUI();

        return view;
    }

    private void intiUI() {
        lv = (ListView) view.findViewById(R.id.lv_workout);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        sessionName = new ArrayList<String>();
        sessionDate = new ArrayList<String>();
        sessionDistance = new ArrayList<Float>();
//        sessionIdentifire=new ArrayList<String>();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment frg = new WorkoutRouteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("SessionName", sessionName.get(position));
                bundle.putFloat("SessionDist", sessionDistance.get(position));
//                bundle.putString("SessionIdentifier", sessionIdentifire.get(position));
                frg.setArguments(bundle);
                mainActivity.replaceFragment(frg, "My Recorded Workout");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pDialog.show();
        getSessionData();
    }

    private void getSessionData() {
        client = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SESSIONS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
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


                if (field.getName().equals("activity") && (!value.equals("4")) && (!value.equals("3"))) {
//                    android.util.Log.d(TAG, "inner0   " + value + "   " + (value.equals("7") || value.equals("8") || value.equals("1")));
                    if (value.equals("7") || value.equals("8") || value.equals("1")) {
//                            i += 1;
                        if (tempActivity != 1) {
                            android.util.Log.d(TAG, "inner1  ");
                            sessionName.add(strSessionName);
                            sessionDate.add(strSessionDate);
//                            sessionIdentifire.add(strSessionIdentifier);
                            tempDist = 1;
                            tempActivity = 1;
                        }
                    }
                }
                if (tempDist == 1 || tempActivity==1) {
                    if (field.getName().equals("distance")) {
                        android.util.Log.d("TAG", "inner2 DISTANCE  ***   " + value);
                        tempSessionDist = tempSessionDist + Float.valueOf(String.valueOf(dp.getValue(field)));
                        tempDist = 0;
                    }
                }

            }
        }
    }

    private void dumpSession(Session session) {
        DateFormat dateFormat = getTimeInstance();
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        tempActivity = 0;
        tempDist = 0;
        tempSessionDist = 0;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        strSessionName = session.getName();
//        strSessionIdentifier=session.getIdentifier();
        strSessionDate = String.valueOf(dateFormat.format(session.getStartTime(TimeUnit.MILLISECONDS)));


        android.util.Log.i(TAG, "Data returned for Session: " + session.getName()
                + "\n\tDescription: " + session.getDescription()
                + "\n\tStart: " + dateFormat.format(session.getStartTime(TimeUnit.MILLISECONDS))
                + "\n\tEnd: " + dateFormat.format(session.getEndTime(TimeUnit.MILLISECONDS)));
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
            case R.id.main_menu_start:
                showWorkOutNameDialog();
                break;
            default:
                break;
        }
        return true;
    }

    private void showWorkOutNameDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.set_workout_activity_info);
//        dialog.setTitle("Title...");

        Button btnOk = (Button) dialog.findViewById(R.id.set_workout_activity_info_btnOk);
        Button btnCancle = (Button) dialog.findViewById(R.id.set_workout_activity_info_btnCancle);
        final EditText etWorkOutName = (EditText) dialog.findViewById(R.id.set_workout_activity_info_etWorkoutTitle);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etWorkOutName.getText().toString().length()!=0) {
                    mainActivity.replaceFragment(new WorkoutAddFragment(), "Add Workout");
                    CUR_WORKOUT_NAME=etWorkOutName.getText().toString();
                    dialog.dismiss();
                } else {
                    etWorkOutName.startAnimation(shakeAnim);
                    etWorkOutName.requestFocus();
                    toast("Please enter Workout Title.");
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
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
                android.util.Log.i("TAG", "=====Session read ====== " );
                // Process the session
                dumpSession(session);

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
            pDialog.dismiss();
            if (sessionDistance.size() > 0) {
                for (int i = 0; i < sessionDistance.size(); i++) {
                    android.util.Log.i("TAG", "DIST   " + sessionDistance.get(i));
                }
            }
            if (sessionName.size() > 0 && sessionDate.size() > 0) {
                Collections.reverse(sessionName);
                Collections.reverse(sessionDate);
                Collections.reverse(sessionDistance);
                lv.setAdapter(new WorkoutListAdapter(getActivity(), sessionName, sessionDate, sessionDistance));
            }
        }
    }


}
