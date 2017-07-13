/*
 * 
 */
package com.pooja.carepack.volly;

import java.io.UnsupportedEncodingException;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.google.gson.Gson;
import com.pooja.carepack.activities.BaseApplication;

// TODO: Auto-generated Javadoc
/**
 * The Class GetLibCacheResponse.
 */
public class GetLibCacheResponse {

	/** The listener. */
	private LibCacheListner listener;
	
	/** The getuser url. */
	private String getuserURL;
	
	/** The request code. */
	private int requestCode;
	
	/** The object. */
	private Object object;

	/**
	 * Instantiates a new gets the lib cache response.
	 *
	 * @param listener the listener
	 * @param clsModel the cls model
	 * @param getURL the get url
	 * @param requestCode the request code
	 */
	public GetLibCacheResponse(LibCacheListner listener, Object clsModel, String getURL, int requestCode) {
		this.listener = listener;
		this.requestCode = requestCode;
		this.getuserURL = getURL;
		object = clsModel;
		getCache(getURL);
	}

	/**
	 * Gets the cache.
	 *
	 * @param url the url
	 * @return the cache
	 */
	public void getCache(String url) {
		Cache cache = BaseApplication.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(url);
		if (entry != null) {
			try {
				String data = new String(entry.data, "UTF-8");
				Gson gson = new Gson();
				Object newObject = gson.fromJson(data.toString(), object.getClass());
				listener.onCacheResponseComplete(0, newObject, requestCode);
				// handle data, like converting it to xml, json, bitmap etc.,
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			listener.onCacheResponseComplete(1, object.getClass(), requestCode);
			// Cached response doesn't exists. Make network call here
		}
	}

	/**
	 * Invalidate cache.
	 */
	public void invalidateCache() {
		BaseApplication.getInstance().getRequestQueue().getCache().invalidate(getuserURL, true);

	}

	/**
	 * Clear cache.
	 */
	public void clearCache() {
		BaseApplication.getInstance().getRequestQueue().getCache().remove(getuserURL);
	}

	/**
	 * Clear all cache.
	 */
	public void clearAllCache() {
		BaseApplication.getInstance().getRequestQueue().getCache().clear();
	}

}
