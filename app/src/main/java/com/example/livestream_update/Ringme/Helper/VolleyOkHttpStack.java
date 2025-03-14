package com.example.livestream_update.Ringme.Helper;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.api.OkHttp;
import com.vtm.ringme.common.api.BaseApi;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by toanvk2 on 11/7/2016.
 */

public class VolleyOkHttpStack implements HttpStack {
    private static final String TAG = VolleyOkHttpStack.class.getSimpleName();

    private OkHttpClient client;
    private ApplicationController mApp;

    public VolleyOkHttpStack(ApplicationController mApp) {
        /*OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //allowAllSSLOkHttp(builder);
        client = builder.build();*/
        client = OkHttp.getClient();
        this.mApp = mApp;
        //allowAllSSLOkHttp(client.newBuilder());
    }

    public VolleyOkHttpStack(OkHttpClient client) {
        this.client = client;
    }

    @SuppressWarnings("deprecation")
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        if (client == null) {
            client = OkHttp.reCreateClient();
        }
        OkHttpClient.Builder clientBuilder = client.newBuilder();
        /*if (client != null) {
            clientBuilder = client.newBuilder();
        } else {
            clientBuilder = new OkHttpClient.Builder();
        }*/
        int timeoutMs = request.getTimeoutMs();
        if (timeoutMs <= 0) {
            timeoutMs = Constants.HTTP.CONNECTION_TIMEOUT;
        }
        clientBuilder.connectTimeout(timeoutMs, TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(timeoutMs, TimeUnit.MILLISECONDS);
        clientBuilder.writeTimeout(timeoutMs, TimeUnit.MILLISECONDS);
        okhttp3.Request.Builder okHttpRequestBuilder = new okhttp3.Request.Builder();
        okHttpRequestBuilder.url(request.getUrl());
        Map<String, String> headers = request.getHeaders();
        for (final String name : headers.keySet()) {
            okHttpRequestBuilder.addHeader(name, headers.get(name));
            if (BuildConfig.DEBUG)
                Log.i(TAG, "performRequest headers: " + name + " - " + headers.get(name));
        }
        for (final String name : additionalHeaders.keySet()) {
            okHttpRequestBuilder.addHeader(name, additionalHeaders.get(name));
            if (BuildConfig.DEBUG)
                Log.i(TAG, "performRequest additionalHeaders: " + name + " - " + additionalHeaders.get(name));
        }
        okHttpRequestBuilder.header(BaseApi.RINGME_API, mApp.getKakoakApi());
        okHttpRequestBuilder.header(BaseApi.UUID, Utilities.getUuidApp());
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "performRequest header: " + BaseApi.RINGME_API + " - " + mApp.getKakoakApi()
                    + "\n" + BaseApi.UUID + " - " + Utilities.getUuidApp());
        }
        setConnectionParametersForRequest(okHttpRequestBuilder, request);
        client = clientBuilder.build();
        okhttp3.Request okHttpRequest = okHttpRequestBuilder.build();
        Call okHttpCall = client.newCall(okHttpRequest);
        Response okHttpResponse = okHttpCall.execute();

        StatusLine responseStatus = new BasicStatusLine(parseProtocol(okHttpResponse.protocol()), okHttpResponse.code
                (), okHttpResponse.message());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromOkHttpResponse(okHttpResponse));

        Headers responseHeaders = okHttpResponse.headers();
        for (int i = 0, len = responseHeaders.size(); i < len; i++) {
            final String name = responseHeaders.name(i), value = responseHeaders.value(i);
            if (name != null) {
                response.addHeader(new BasicHeader(name, value));
            }
        }
        return response;
    }

    @SuppressWarnings("deprecation")
    private static HttpEntity entityFromOkHttpResponse(Response r) throws IOException {
        BasicHttpEntity entity = new BasicHttpEntity();
        ResponseBody body = r.body();
        entity.setContent(body.byteStream());
        entity.setContentLength(body.contentLength());
        entity.setContentEncoding(r.header("Content-Encoding"));

        if (body.contentType() != null) {
            entity.setContentType(body.contentType().type());
        }
        return entity;
    }

    @SuppressWarnings("deprecation")
    private static void setConnectionParametersForRequest(okhttp3.Request.Builder builder, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                // Ensure backwards compatibility.  Volley assumes a request with a null body is a GET.
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    builder.post(RequestBody.create(MediaType.parse(request.getPostBodyContentType()), postBody));
                }
                break;
            case Request.Method.GET:
                builder.get();
                break;
            case Request.Method.DELETE:
                builder.delete();
                break;
            case Request.Method.POST:
                builder.post(createRequestBody(request));
                break;
            case Request.Method.PUT:
                builder.put(createRequestBody(request));
                break;
            case Request.Method.HEAD:
                builder.head();
                break;
            case Request.Method.OPTIONS:
                builder.method("OPTIONS", null);
                break;
            case Request.Method.TRACE:
                builder.method("TRACE", null);
                break;
            case Request.Method.PATCH:
                builder.patch(createRequestBody(request));
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    @SuppressWarnings("deprecation")
    private static ProtocolVersion parseProtocol(final Protocol p) {
        switch (p) {
            case HTTP_1_0:
                return new ProtocolVersion("HTTP", 1, 0);
            case HTTP_1_1:
                return new ProtocolVersion("HTTP", 1, 1);
            case SPDY_3:
                return new ProtocolVersion("SPDY", 3, 1);
            case HTTP_2:
                return new ProtocolVersion("HTTP", 2, 0);
        }

        throw new IllegalAccessError("Unkwown protocol");
    }

    private static RequestBody createRequestBody(Request r) throws AuthFailureError {
        final byte[] body = r.getBody();
        if (body == null) {
            return null;
        }

        return RequestBody.create(MediaType.parse(r.getBodyContentType()), body);
    }

}