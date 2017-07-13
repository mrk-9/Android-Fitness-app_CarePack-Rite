/*
 * 
 */
package com.pooja.carepack.activities;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pooja.carepack.R;
import com.pooja.carepack.utils.MyPrefs;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// TODO: Auto-generated Javadoc

/**
 * The Class BaseApplication.
 */
public class BaseApplication extends Application {
    /**
     * The Constant TAG.
     */
    public static final String TAG = BaseApplication.class.getSimpleName();
    /**
     * The m instance.
     */
    private static BaseApplication mInstance;
    private GoogleCloudMessaging gcm;
    private MyPrefs prefs;
    private String regid;
    /**
     * The m request queue.
     */
    private RequestQueue mRequestQueue;

    /**
     * Gets the single instance of BaseApplication.
     *
     * @return single instance of BaseApplication
     */
    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        // Mint.initAndStartSession(BaseApplication.this, "8c2b05e1 DM7RJ3A7");
        mInstance = this;
        getAppKeyHash();

        prefs = new MyPrefs(this.getApplicationContext());
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this.getApplicationContext());
            regid = getRegistrationId(this.getApplicationContext());
            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                storeRegistrationId(regid);
            }
        }

    }

    //check for play service
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS && resultCode != ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            return false;
        } else {
            return true;
        }
    }


    private String getRegistrationId(Context context) {
        String registrationId = prefs.get(MyPrefs.keys.GCMKEY);
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = prefs.getInt(MyPrefs.keys.GCMVERSION);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(getString(R.string.sender_id));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                storeRegistrationId(regid);
            }

            ;
        }.execute();
    }

    private void storeRegistrationId(String regId) {
        Log.d("Tag", "Key : " + regId);
        prefs.set(MyPrefs.keys.GCMKEY, regId);
        prefs.set(MyPrefs.keys.GCMVERSION, getAppVersion(getApplicationContext()));
    }


    /**
     * Gets the app key hash.
     *
     * @return the app key hash
     */
    private String getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Tag", "Hash key >>>>> " + something);
                return something;
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return null;

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    /**
     * Gets the request queue.
     *
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds the to request queue.
     *
     * @param <T> the generic type
     * @param req the req
     * @param tag the tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Adds the to request queue.
     *
     * @param <T> the generic type
     * @param req the req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancel pending requests.
     *
     * @param tag the tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
