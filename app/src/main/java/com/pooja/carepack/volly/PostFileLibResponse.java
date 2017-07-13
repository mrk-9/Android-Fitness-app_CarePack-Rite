/*
 * 
 */
package com.pooja.carepack.volly;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

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
import com.pooja.carepack.fragments.BaseFragment.*;

// TODO: Auto-generated Javadoc
/**
 * The Class PostFileLibResponse.
 */
@SuppressWarnings("unused")
public class PostFileLibResponse {
	
	/** The listener. */
	private LibPostListner listener;
	
	/** The dialog. */
	private ProgressDialog dialog;
	
	/** The context. */
	private Context context;
	
	/** The getuser url. */
	private String getuserURL;
	
	/** The is progeress. */
	private boolean isProgeress = false;
	
	/** The request code. */
	private int requestCode;
	
	/** The is cache clear. */
	private boolean isCacheClear = true;
	
	/** The tag. */
	private String TAG;
	
	/** The object. */
	private Object object;
	
	/** The parmas. */
	private MultipartEntity parmas;

	/**
	 * Instantiates a new post file lib response.
	 *
	 * @param listener the listener
	 * @param clsModel the cls model
	 * @param context the context
	 * @param parmas the parmas
	 * @param getURL the get url
	 * @param requestCode the request code
	 */
	public PostFileLibResponse(LibPostListner listener, Object clsModel, Context context, MultipartEntity parmas, String getURL, int requestCode) {
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

	/**
	 * Instantiates a new post file lib response.
	 *
	 * @param listener the listener
	 * @param clsModel the cls model
	 * @param context the context
	 * @param parmas the parmas
	 * @param getURL the get url
	 * @param requestCode the request code
	 * @param isProgress the is progress
	 */
	public PostFileLibResponse(LibPostListner listener, Object clsModel, Context context, MultipartEntity parmas, String getURL, int requestCode, boolean isProgress) {
		this(listener, clsModel, context, parmas, getURL, requestCode);
		this.isProgeress = isProgress;
		dialog = new ProgressDialog(context);
		dialog.setMessage("Please wait");
		if (isProgeress)
			dialog.show();
	}

	/**
	 * Instantiates a new post file lib response.
	 *
	 * @param listener the listener
	 * @param clsModel the cls model
	 * @param context the context
	 * @param parmas the parmas
	 * @param getURL the get url
	 * @param requestCode the request code
	 * @param isProgress the is progress
	 * @param isCacheClear the is cache clear
	 */
	public PostFileLibResponse(LibPostListner listener, Object clsModel, Context context, MultipartEntity parmas, String getURL, int requestCode, boolean isProgress, boolean isCacheClear) {
		this(listener, clsModel, context, parmas, getURL, requestCode, isProgress);
		this.isCacheClear = isCacheClear;
	}

	/**
	 * Start request.
	 */
	private void startRequest() {
		addToRequestQueue(parmas);
	}

	/**
	 * Start request.
	 *
	 * @param file the file
	 * @param param the param
	 */
	public void startRequest(File file, String... param) {
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("profilePicture", new FileBody(file));
		String urlParam = getuserURL;
		for (int i = 0; i < param.length; i += 2) {
			try {
				if (param[i + 1] != null && param[i + 1].length() > 0) {
					if (i == 0)
						urlParam += "?";
					else
						urlParam += "&";
					urlParam += param[i] + "=" + param[i + 1];

					try {
						entity.addPart(param[i], new StringBody(param[i + 1]));
					} catch (UnsupportedEncodingException e) {
						Log.d("UnsupportedEncodingException");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.d("execute >>>> " + urlParam);
		addToRequestQueue(entity);
	}

	/**
	 * Adds the to request queue.
	 *
	 * @param parmas the parmas
	 */
	private void addToRequestQueue(MultipartEntity parmas) {
		GsonRequestFile jsObjRequest = new GsonRequestFile(Method.POST, getuserURL, Object.class, parmas, this.createSuccessListener(), this.createErrorListener());
		BaseApplication.getInstance().addToRequestQueue(jsObjRequest, TAG);
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 60 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	/**
	 * Creates the success listener.
	 *
	 * @return the response. listener
	 */
	private Response.Listener<String> createSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
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
					Log.d("Response " + json);
					Object newObject = gson.fromJson(json.toString(), object.getClass());
					listener.onPostResponseComplete(newObject, requestCode);
				} catch (JsonSyntaxException e) {

					e.printStackTrace();
					listener.onPostResponseError("" + e.getMessage(), requestCode);
				}
			}
		};
	}

	/**
	 * Cancel.
	 *
	 * @param TAG the tag
	 */
	public void cancel(String TAG) {
		Log.d("On Cancel" + TAG);
		// LibApplication.getInstance().getRequestQueue().cancelAll(TAG);

		BaseApplication.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				request.cancel();
				return true;
			}
		});

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
				Log.d("Error:Volly " + error.getMessage());
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

	/**
	 * On destroy.
	 */
	public void onDestroy() {
		BaseApplication.getInstance().getRequestQueue().cancelAll(TAG);
		BaseApplication.getInstance().getRequestQueue().getCache().remove(getuserURL);
		// BaseApplication.getInstance().getRequestQueue().getCache().clear();
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
