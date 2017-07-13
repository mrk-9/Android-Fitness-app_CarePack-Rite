package com.pooja.carepack.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pooja.carepack.R;
import com.pooja.carepack.activities.SplashActivity;
import com.pooja.carepack.utils.MyPrefs;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class WorkoutAddFragment extends BaseFragment implements GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    private View view;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private MyPrefs prefs;
    private Button start;
//    private TextView tvTimer;
    private Dialog.Builder builder;
    private ImageView ivActivity;
    private TextView tvActvity;
    private EditText etRouteName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_workout_add, container, false);
        initUI();
        return view;
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

    private void initUI() {
        etRouteName = (EditText) view.findViewById(R.id.frg_workout_add_etRouteName);
        ivActivity = (ImageView) view.findViewById(R.id.frg_workout_add_ivActivity);
        tvActvity = (TextView) view.findViewById(R.id.frg_workout_add_tvActivity);
//        tvTimer.setOnClickListener(this);
        ivActivity.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start = (Button) view.findViewById(R.id.workout_add_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Long timerMiliSec=e
//
                if (etRouteName.getText().length()==0) {
                    toast("Please enter route name.");
                } else {
                    Fragment frg = new WorkoutAddCurrentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("SessionName",etRouteName.getText().toString());
                    bundle.putString("SessionActivity", tvActvity.getText().toString().toLowerCase());
                    frg.setArguments(bundle);
                    mainActivity.replaceFragment(frg, "Current Workout");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
                mMap.setOnInfoWindowClickListener(WorkoutAddFragment.this);
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


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.frg_workout_add_tvTimer:
//                showTimerDialog();
//                break;
            case R.id.frg_workout_add_ivActivity:
                showActivtyDailog();
                break;
        }

    }

    private void showActivtyDailog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.choose_activity_dialog);
//        dialog.setTitle("Title...");

        LinearLayout llWalk = (LinearLayout) dialog.findViewById(R.id.choose_actiivyt_dialog_llWalk);
        LinearLayout llRun = (LinearLayout) dialog.findViewById(R.id.choose_actiivyt_dialog_llRun);
        LinearLayout llCycle = (LinearLayout) dialog.findViewById(R.id.choose_actiivyt_dialog_llCycle);

        llWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivActivity.setImageResource(R.drawable.ic_walk);
                tvActvity.setText("Walking");
                dialog.dismiss();
            }
        });
        llRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivActivity.setImageResource(R.drawable.ic_run);
                tvActvity.setText("Running");
                dialog.dismiss();
            }
        });
        llCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivActivity.setImageResource(R.drawable.ic_cycle);
                tvActvity.setText("Biking");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showTimerDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.timer_picker_dialog);
//        dialog.setTitle("Title...");

        final EditText etHours = (EditText) dialog.findViewById(R.id.timer_picker_dialog_tvHours);
        final EditText etMinutes = (EditText) dialog.findViewById(R.id.timer_picker_dialog_tvMinutes);
        final EditText etSeconds = (EditText) dialog.findViewById(R.id.timer_picker_dialog_tvSeconds);
        Button btnOk = (Button) dialog.findViewById(R.id.timer_picker_dialog_btnOK);
        Button btnCancle = (Button) dialog.findViewById(R.id.timer_picker_dialog_btnCancle);

        etHours.setOnClickListener(this);
        etMinutes.setOnClickListener(this);
        etSeconds.setOnClickListener(this);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tvTimer.setText(etHours.getText().toString() + ":" + etMinutes.getText().toString() + ":" + etSeconds.getText().toString());
                dialog.dismiss();
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
}
