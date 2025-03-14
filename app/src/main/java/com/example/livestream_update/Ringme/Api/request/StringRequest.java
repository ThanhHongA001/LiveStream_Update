package com.example.livestream_update.Ringme.Api.request;

/*
 * Copyright (c) 2018.
 * www.bigzun.com
 */


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.utils.Log;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by namnh40 on 4/10/2018.
 */

public class StringRequest extends  com.android.volley.toolbox.StringRequest {
    private final String TAG = getClass().getSimpleName();
    private final WeakHashMap<String, String> headers = new WeakHashMap<>();
    private final WeakHashMap<String, String> params = new WeakHashMap<>();

    public StringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        if (BuildConfig.DEBUG) {
            String methodStr;
            switch (method) {
                case Method.GET:
                    methodStr = "GET";
                    break;
                case Method.POST:
                    methodStr = "POST";
                    break;
                case Method.PUT:
                    methodStr = "PUT";
                    break;
                case Method.DELETE:
                    methodStr = "DELETE";
                    break;
                case Method.HEAD:
                    methodStr = "HEAD";
                    break;
                case Method.OPTIONS:
                    methodStr = "OPTIONS";
                    break;
                case Method.TRACE:
                    methodStr = "TRACE";
                    break;
                case Method.PATCH:
                    methodStr = "PATCH";
                    break;
                default:
                    methodStr = "unKnown";
                    break;
            }
            Log.e(TAG, "Method: " + methodStr + " - Url: " + url);
        }
    }

    public StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Method: GET - Url: " + url);
        }
    }

    @Override
    protected String getParamsEncoding() {
        return "UTF-8";
    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "HEADERS: " + headers);
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "PARAMS: " + params);
        return params != null ? params : super.getParams();
    }

    public void setHeader(String key, Object value) {
        if (value == null) {
            headers.put(key, "");
            return;
        }
        headers.put(key, value.toString());
    }

    public void setParams(String key, Object value) {
        if (value == null) {
            params.put(key, "");
            return;
        }
        params.put(key, value.toString());
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String result = "";
        try {
            try {
                result = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                result = new String(response.data);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parseNetworkResponse statusCode: " + response.statusCode
                    + " - networkTimeMs: " + response.networkTimeMs + "\n" + result);
        try {
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
