package com.example.livestream_update.Ringme.Api;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.utils.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;

public class GsonRequest<T> extends Request<T> {

    private final String TAG = getClass().getSimpleName();
    private final Listener<T> mListener;
    private final String mRequestBody;
    private Gson mGson;
    private Class<T> mJavaClass;
    private WeakHashMap<String, String> headers = new WeakHashMap<>();
    private WeakHashMap<String, String> params = new WeakHashMap<>();

    public GsonRequest(int method, String url, Class<T> cls, String requestBody, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mRequestBody = requestBody;
        mJavaClass = cls;
        mListener = listener;
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

    @Override
    protected String getParamsEncoding() {
        return "UTF-8";
    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null && response != null) {
            mListener.onResponse(response);
        } else {
            Log.e(TAG, "Response is null");
            getErrorListener().onErrorResponse(new VolleyError());
        }
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
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String className = mJavaClass.getCanonicalName();
            String result = new String(response.data, Charset.forName("UTF-8"));
            if (BuildConfig.DEBUG)
                Log.d(TAG, "parseNetworkResponse statusCode: " + response.statusCode
                        + " - networkTimeMs: " + response.networkTimeMs + " - className: " + className
                        + "\n" + result);
            T parsedGSON = mGson.fromJson(result, mJavaClass);
            return Response.success(parsedGSON, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? super.getBody() : mRequestBody.getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException | AuthFailureError e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, e);
            return null;
        }
    }
}

