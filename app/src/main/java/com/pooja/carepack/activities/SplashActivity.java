package com.pooja.carepack.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pooja.carepack.R;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.fragments.WelcomeFragment;
import com.pooja.carepack.interfaces.OnFbLoginListener;
import com.pooja.carepack.utils.MyPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    private boolean doubleBackToExitPressedOnce;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private OnFbLoginListener onFbLoginListener;
    private LocationManager mLocationManager;
    private MyPrefs prefs;
    private MobileDataStateChangedReceiver mobileDataReceiver;

    public static Location getNewLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
        if (!isGPSEnabled && !isNetworkEnabled) {
            BaseFragment.toast(context, "Please Enable GPS");
            BaseFragment.Log.d("no network provider is enabled");
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
//                Log.d("Network", "Network Enabled");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        return location;
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
//                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            return location;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WelcomeFragment.millies = 1500;

        prefs = new MyPrefs(this);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        } else {
            setTheme(R.style.AppTheme);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getSupportFragmentManager().beginTransaction().add(R.id.ll_container, new WelcomeFragment(), "welcome").commit();
        Location loc = getNewLocation(this);
        if (loc != null) {
            prefs.set(MyPrefs.keys.LAT, "" + loc.getLatitude());
            prefs.set(MyPrefs.keys.LNG, "" + loc.getLongitude());
        } else {
            prefs.set(MyPrefs.keys.LAT, "0.0");
            prefs.set(MyPrefs.keys.LNG, "0.0");
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            WelcomeFragment.millies = 0;
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void setFragment(Fragment frg) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("");
        ft.replace(R.id.ll_container, frg, frg.getClass().getSimpleName()).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            if (resultCode == RESULT_OK) {
                try {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loginWithFacebook(OnFbLoginListener onFbLoginListener) {
        this.onFbLoginListener = onFbLoginListener;
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        ArrayList<String> alPermission = new ArrayList<String>();
        alPermission.add("email");
        alPermission.add("user_birthday");
        alPermission.add("public_profile");
        LoginManager.getInstance().logInWithReadPermissions(this, alPermission);
    }

    @Override
    public void onSuccess(LoginResult result) {
        accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    BaseFragment.Log.d("FB >>>  " + object.toString());
                    if (object.has("name")) {
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = "";
                        if (object.has("email"))
                            email = object.getString("email");
                        if (email.equals(""))
                            email = id + "@facebook.com";
                        String image = "https://graph.facebook.com/" + id + "/picture?type=large";
                        LoginManager.getInstance().logOut();

                        onFbLoginListener.onFbLogin(id, name, email, image);
//                        loginSocial(name, email, CODE_LOGIN_FB, LoginType.TYPE_FB);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, link, email, gender, birthday, first_name, last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException e) {

    }
    public static class NetworkUtil {

        static final int TYPE_WIFI = 1;
        static final int TYPE_MOBILE = 2;
        static final int TYPE_NOT_CONNECTED = 0;

        public static int getConnectivityStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }
    }

    private class MobileDataStateChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                int state = NetworkUtil.getConnectivityStatus(context);
                if (state == NetworkUtil.TYPE_WIFI || state == NetworkUtil.TYPE_MOBILE) {
//                    offlineDialog.dismiss();
                    prefs.set(MyPrefs.keys.ISONLINE,"1");
                } else if (state == NetworkUtil.TYPE_NOT_CONNECTED) {
//                    offlineDialog.show();
                    prefs.set(MyPrefs.keys.ISONLINE, "0");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mobileDataReceiver = new MobileDataStateChangedReceiver();
        registerReceiver(mobileDataReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mobileDataReceiver != null) {
                unregisterReceiver(mobileDataReceiver);
            }
        } catch (Exception e) {

        }
    }
}
