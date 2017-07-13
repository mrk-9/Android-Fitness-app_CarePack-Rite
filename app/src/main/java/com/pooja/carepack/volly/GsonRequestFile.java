/*
 * 
 */
package com.pooja.carepack.volly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Timestamp;

import org.apache.http.entity.mime.MultipartEntity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

// TODO: Auto-generated Javadoc
/**
 * The Class GsonRequestFile.
 */
public class GsonRequestFile extends Request<String> {

	// private final Gson gson;
	/** The listener. */
	// private final Class<String> clazz;
	private final Listener<String> listener;
	
	/** The entity. */
	private MultipartEntity entity = new MultipartEntity();

	/**
	 * Instantiates a new gson request file.
	 *
	 * @param method the method
	 * @param url the url
	 * @param o the o
	 * @param entity the entity
	 * @param listener the listener
	 * @param errorListener the error listener
	 */
	public GsonRequestFile(int method, String url, Class<Object> o, MultipartEntity entity, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, errorListener);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
		// this.gson = gsonBuilder.create();
		// this.clazz = clazz;
		this.entity = entity;
		this.listener = listener;
	}

	/* (non-Javadoc)
	 * @see com.android.volley.Request#deliverResponse(java.lang.Object)
	 */
	@Override
	protected void deliverResponse(String response) {
		listener.onResponse(response);
	}

	/* (non-Javadoc)
	 * @see com.android.volley.Request#parseNetworkResponse(com.android.volley.NetworkResponse)
	 */
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// return (Response<String>) Response.success(response.data, getCacheEntry());
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(json, HttpHeaderParser.parseIgnoreCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

	/* (non-Javadoc)
	 * @see com.android.volley.Request#getBodyContentType()
	 */
//	@Override
//	public String getBodyContentType() {
//		return entity.getContentType();
//	}

	/* (non-Javadoc)
	 * @see com.android.volley.Request#getBody()
	 */
	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}
}
