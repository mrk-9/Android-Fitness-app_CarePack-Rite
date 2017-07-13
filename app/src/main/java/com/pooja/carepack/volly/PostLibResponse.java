/*
 * 
 */
package com.pooja.carepack.volly;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.pooja.carepack.activities.BaseApplication;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PostLibResponse {

    private  String isOnline;
    private  MyPrefs prefs;
    private LibPostListner listener;
    private ProgressDialog dialog;
    private Context context;
    private String getuserURL;
    private boolean isProgeress = false;
    private int requestCode;
    private boolean isCacheClear = true;
    private String TAG;
    private Object object;
    private File file;
    private Map<String, String> parmas;

    public PostLibResponse(LibPostListner listener, Object clsModel, Context context, Map<String, String> parmas, String getURL, int requestCode) {
        prefs=new MyPrefs(context);
         isOnline = prefs.get(MyPrefs.keys.ISONLINE);
        this.listener = listener;
        this.requestCode = requestCode;
        this.getuserURL = getURL;
        this.context = context;
        this.parmas = parmas;
        this.TAG = clsModel.getClass().getName().toString();
        object = clsModel;
        if (parmas != null)
            startRequest();

    }

    public PostLibResponse(LibPostListner listener, Object clsModel, Context context, Map<String, String> parmas, String getURL, int requestCode, boolean isProgress) {
        this(listener, clsModel, context, parmas, getURL, requestCode);
        if (Utility.hasConnection(context)) {
            this.isProgeress = isProgress;
            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait");
            if (isProgeress)
                dialog.show();
        } else {
            BaseFragment.toast(context, "Internet connection seem to be offline.");
        }
    }

    public PostLibResponse(LibPostListner listener, Object clsModel, Context context, Map<String, String> parmas, String getURL, int requestCode, boolean isProgress, boolean isCacheClear) {
        this(listener, clsModel, context, parmas, getURL, requestCode, isProgress);
        this.isCacheClear = isCacheClear;
    }

    public PostLibResponse(LibPostListner listener, Object clsModel, Context context, Map<String, String> parmas, File file, String getURL, int requestCode) {
        if (Utility.hasConnection(context)) {
            this.listener = listener;
            this.requestCode = requestCode;
            this.getuserURL = getURL;
            this.context = context;
            this.parmas = parmas;
            this.file = file;
            this.isCacheClear = true;
            this.TAG = clsModel.getClass().getName().toString();
            object = clsModel;
            GsonFileRequest<Object> jsObjRequest = new GsonFileRequest<Object>(Method.POST, getuserURL, Object.class, parmas, file, this.createSuccessListener(), this.createErrorListener());
            BaseApplication.getInstance().addToRequestQueue(jsObjRequest, TAG);
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 60 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            BaseFragment.toast(context, "Internet connection seem to be offline.");
        }
    }

    private void startRequest() {
        if(isOnline.equals("1")) {
            BaseFragment.Log.d("execute >> " + getuserURL);
            logParam(parmas);
            GsonRequest<Object> jsObjRequest = new GsonRequest<Object>(Method.POST, getuserURL, Object.class, parmas, this.createSuccessListener(), this.createErrorListener());
            BaseApplication.getInstance().addToRequestQueue(jsObjRequest, TAG);
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 60 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
        else{
            try {
                Gson gson = new Gson();
//                String json = gson.toJson(prefs.get(getuserURL));
                BaseFragment.Log.d("Response " + prefs.get(getuserURL+parmas));

                Object newObject = gson.fromJson(prefs.get(getuserURL+parmas).toString(), object.getClass());
                listener.onPostResponseComplete(newObject, requestCode);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void logParam(Map<String, String> parmas) {
        for (String key : parmas.keySet()) {
            BaseFragment.Log.d(" param >>> " + key + "->" + parmas.get(key));
        }
    }


    public void startRequest(String... param) {
        Map<String, String> params = new HashMap<String, String>();
        String urlParam = getuserURL;
        for (int i = 0; i < param.length; i += 2) {
            try {
                if (param[i + 1] != null && param[i + 1] != null) {
                    if (i == 0)
                        urlParam += "?";
                    else
                        urlParam += "&";
                    urlParam += param[i] + "=" + param[i + 1];
                    params.put(param[i], param[i + 1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BaseFragment.Log.d("execute >>>> " + urlParam);
        addToRequestQueue(params);
    }

    private void addToRequestQueue(Map<String, String> parmas) {
        GsonRequest<Object> jsObjRequest = new GsonRequest<Object>(Method.POST, getuserURL, Object.class, parmas, this.createSuccessListener(), this.createErrorListener());
        BaseApplication.getInstance().addToRequestQueue(jsObjRequest, TAG);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 60 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private Response.Listener<Object> createSuccessListener() {
        return new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                if (isProgeress)
                    if (dialog != null)
                        dialog.dismiss();
                if (isCacheClear) {
                    onDestroy();
                }
                //
                try {
                    Gson gson = new Gson();
                    String json = gson.toJson(response);
                    BaseFragment.Log.d("Response " + json);
                    Object newObject = gson.fromJson(json.toString(), object.getClass());
                    prefs.set(getuserURL+parmas,json.toString());
                    listener.onPostResponseComplete(newObject, requestCode);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    listener.onPostResponseError("" + e.getMessage(), requestCode);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    listener.onPostResponseError("" + e.getMessage(), requestCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onPostResponseError("" + e.getMessage(), requestCode);
                }
            }
        };
    }

    public void cancel(String TAG) {
        BaseFragment.Log.d("On Cancel" + TAG);

        BaseApplication.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                request.cancel();
                return true;
            }
        });

    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseFragment.Log.d("Error:Volly " + error.networkResponse + " :" + error.toString());
                if (isProgeress)
                    if (dialog != null)
                        dialog.dismiss();
                if (isCacheClear) {
                    onDestroy();
                }
                listener.onPostResponseError(error.getLocalizedMessage(), requestCode);
            }
        };
    }

    public void onDestroy() {
        BaseApplication.getInstance().getRequestQueue().cancelAll(TAG);
        BaseApplication.getInstance().getRequestQueue().getCache().remove(getuserURL);
        // BaseApplication.getInstance().getRequestQueue().getCache().clear();
    }

    public class MessageAdapter implements JsonSerializer<Object> {

        @Override
        public JsonElement serialize(Object message, Type type, JsonSerializationContext jsc) {
            JsonObject jsonObject = new JsonObject();
            return jsonObject;
        }
    }
}
