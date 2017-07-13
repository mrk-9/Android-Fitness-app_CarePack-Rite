/*
 * 
 */
package com.pooja.carepack.volly;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pooja.carepack.R;
import com.pooja.carepack.activities.BaseApplication;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.utils.MyPrefs;

import java.lang.reflect.Type;

// TODO: Auto-generated Javadoc

/**
 * The Class GetLibResponse.
 */
@SuppressWarnings("unused")
public class GetLibResponse {

    private  String isOnline;
    private MyPrefs prefs;
    /**
     * The listener.
     */
    private LibListner listener;

    /**
     * The dialog.
     */
    private ProgressDialog dialog;

    /**
     * The context.
     */
    private Context context;

    /**
     * The getuser url.
     */
    private String getuserURL;

    /**
     * The is progeress.
     */
    private boolean isProgeress = false;

    /**
     * The request code.
     */
    private int requestCode;

    /**
     * The is cache clear.
     */
    private boolean isCacheClear = true;

    /**
     * The tag.
     */
    private String TAG;

    /**
     * The object.
     */
    private Object object;

    /**
     * Instantiates a new gets the lib response.
     *
     * @param listener    the listener
     * @param clsModel    the cls model
     * @param context     the context
     * @param getURL      the get url
     * @param requestCode the request code
     */
    public GetLibResponse(LibListner listener, Object clsModel, Context context, String getURL, int requestCode) {
        prefs=new MyPrefs(context);
        isOnline = prefs.get(MyPrefs.keys.ISONLINE);
        this.listener = listener;
        this.requestCode = requestCode;
        this.getuserURL = getURL;
        this.context = context;
        this.TAG = clsModel.getClass().getName().toString();
        object = clsModel;
        startRequest();
    }

    /**
     * Instantiates a new gets the lib response.
     *
     * @param listener    the listener
     * @param clsModel    the cls model
     * @param context     the context
     * @param getURL      the get url
     * @param requestCode the request code
     * @param isProgress  the is progress
     */
    public GetLibResponse(LibListner listener, Object clsModel, Context context, String getURL, int requestCode, boolean isProgress) {
        this(listener, clsModel, context, getURL, requestCode);
        this.isProgeress = isProgress;
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.please_wait));
        if (isProgeress)
            dialog.show();
    }

    /**
     * Instantiates a new gets the lib response.
     *
     * @param listener    the listener
     * @param clsModel    the cls model
     * @param context     the context
     * @param getURL      the get url
     * @param requestCode the request code
     * @param isProgress  the is progress
     * @param isCancel    the is cancel
     */
    public GetLibResponse(LibListner listener, Object clsModel, Context context, String getURL, int requestCode, boolean isProgress, int isCancel) {
        this(listener, clsModel, context, getURL, requestCode);
        this.isProgeress = isProgress;
        dialog = new ProgressDialog(context);
        if (isCancel == 0)
            dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.please_wait));
        if (isProgeress)
            dialog.show();
    }

    /**
     * Instantiates a new gets the lib response.
     *
     * @param listener     the listener
     * @param clsModel     the cls model
     * @param context      the context
     * @param getURL       the get url
     * @param requestCode  the request code
     * @param isProgress   the is progress
     * @param isCacheClear the is cache clear
     */
    public GetLibResponse(LibListner listener, Object clsModel, Context context, String getURL, int requestCode, boolean isProgress, boolean isCacheClear) {
        this(listener, clsModel, context, getURL, requestCode, isProgress);
        this.isCacheClear = isCacheClear;
    }

    /**
     * Start request.
     */
    private void startRequest() {
        if(isOnline.equals("1")) {
            GsonRequest<Object> jsObjRequest = new GsonRequest<Object>(Method.GET, getuserURL, Object.class, null, this.createSuccessListener(), this.createErrorListener());
            BaseApplication.getInstance().addToRequestQueue(jsObjRequest, TAG);
        }
        else{
            try {


            Gson gson = new Gson();
//            String json = gson.toJson(prefs.get(getuserURL));
            BaseFragment.Log.d("Response " + prefs.get(getuserURL));
            Object newObject = gson.fromJson(prefs.get(getuserURL).toString(), object.getClass());
            listener.onResponseComplete(newObject, requestCode);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * Creates the success listener.
     *
     * @return the response. listener
     */
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
                Gson gson = new Gson();
                String json = gson.toJson(response);

                Object newObject = gson.fromJson(json.toString(), object.getClass());
                prefs.set(getuserURL,json.toString());
                listener.onResponseComplete(newObject, requestCode);

            }
        };
    }

    /**
     * Creates the error listener.
     *
     * @return the response. error listener
     */
    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isProgeress)
                    if (dialog != null)
                        dialog.dismiss();
                if (isCacheClear) {
                    onDestroy();
                }
                listener.onResponseError(error.getLocalizedMessage(), requestCode);
            }
        };
    }

    /**
     * On destroy.
     */
    public void onDestroy() {
        BaseApplication.getInstance().getRequestQueue().cancelAll(TAG);
        BaseApplication.getInstance().getRequestQueue().getCache().remove(getuserURL);
    }

    /**
     * The Class MessageAdapter.
     */
    public class MessageAdapter implements JsonSerializer<Object> {

        /* (non-Javadoc)
         * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
         */
        @Override
        public JsonElement serialize(Object message, Type type, JsonSerializationContext jsc) {
            JsonObject jsonObject = new JsonObject();

            return jsonObject;
        }
    }
}
