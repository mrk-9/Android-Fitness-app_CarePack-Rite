/*
 * 
 */
package com.pooja.carepack.volly;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.security.Timestamp;
import java.util.Map;

// TODO: Auto-generated Javadoc

/**
 * The Class GsonRequest.
 *
 * @param <T> the generic type
 */
public class GsonRequest<T> extends Request<T> {

    private final Gson gson;
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> parmas, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        this.gson = gsonBuilder.create();
        this.clazz = clazz;
        this.headers = parmas;
        this.listener = listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseIgnoreCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
