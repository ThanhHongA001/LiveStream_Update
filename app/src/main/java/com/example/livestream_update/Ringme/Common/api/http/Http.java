package com.example.livestream_update.Ringme.Common.api.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.common.api.http.requestBody.ProgressRequestBody;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HoangAnhTuan on 8/24/2017.
 */

public class Http implements Serializable {

    private static final String TAG = "Http";

    private static final int TIME_CACHE_DEFAULT = 60;
    private static final int NUMBER_RETRY_DEFAULT = 3;
    private static final int TIME_OUT_DEFAULT = 60;

    private final Map<String, String> params = new HashMap<>();
    private Object body = new Object();
    private final Map<String, Object> bodyMap = new HashMap<>();
    private final Map<String, String> header = new HashMap<>();
    private final Map<String, String> files = new HashMap<>();
    private String baseUrl = "";
    private String url = "";
    private String tag = "";
    private HttpCallBack callBack;
    private HttpMethod method;
    private boolean retry = true;
    private boolean cache = false;
    private int timeCache = TIME_CACHE_DEFAULT;
    private int numberRetry = NUMBER_RETRY_DEFAULT;
    private long timeOut = TIME_OUT_DEFAULT;

    private CompositeDisposable mCompositeDisposable;
    private Context context;
    private ProgressRequestBody progressRequestBody;

    public static Builder get() {
        return new Builder(null, HttpMethod.GET, null);
    }

    public static Builder post() {
        return new Builder(null, HttpMethod.POST, null);
    }

    public static Builder upload() {
        return new Builder(null, HttpMethod.FILE, null);
    }

    public static Builder get(String url) {
        return new Builder(url, HttpMethod.GET, null);
    }

    public static Builder post(String url) {
        return new Builder(url, HttpMethod.POST, null);
    }

    public static Builder upload(String url) {
        return new Builder(url, HttpMethod.FILE, null);
    }

    public static Builder get(Context context, String baseUrl, String url) {
        return new Builder(context, HttpMethod.GET, baseUrl, url);
    }

    public static Builder post(Context context, String baseUrl, String url) {
        return new Builder(context, HttpMethod.POST, baseUrl, url);
    }

    public static Builder upload(Context context, String baseUrl, String url) {
        return new Builder(context, HttpMethod.FILE, baseUrl, url);
    }

    public static Builder create(Context context) {
        return new Builder(null, null, context);
    }

    public static Builder postByRequestBody(Context context, String baseUrl, String url) {
        return new Builder(context, HttpMethod.POST_REQUEST_BODY, baseUrl, url);
    }

    public static void cancel(String tag) {
        Disposable disposable = Utils.getInstance().get(tag);
        if (disposable != null) disposable.dispose();
    }

    public static class Builder implements Serializable {

        private final Http http;

        Builder(String url, HttpMethod method, Context context) {
            http = new Http();
            try {
                if (!TextUtils.isEmpty(url)) {
                    URL link = new URL(url);

                    int port = link.getPort();
                    if (port <= 0) port = 80;

                    http.baseUrl = link.getProtocol() + "://" + link.getHost() + ":" + port + "/";
                    http.url = url.replace(http.baseUrl, "");
                    http.method = method;
                    http.context = context;
                }
            } catch (Exception e) {
                Log.e(TAG, "Builder", e);
            }
        }

        Builder(Context context, HttpMethod method, String baseUrl, String url) {
            http = new Http();
            http.context = context;
            http.baseUrl = baseUrl;
            http.url = url;
            http.method = method;
        }

        public Http execute() {
            http.execute();
            android.util.Log.d("http", "execute: " + http.body);
            return http;
        }

        public Builder setContext(Context context) {
            http.context = context;
            return this;
        }

        public Builder setTag(String tag) {
            http.tag = tag;
            return this;
        }

        public Builder setTimeOut(long timeOut) {
            http.timeOut = timeOut;
            return this;
        }

        public Builder setTimeCache(int timeCache) {
            http.timeCache = timeCache;
            http.cache = true;
            return this;
        }

        public Builder setNumberRetry(int numberRetry) {
            http.numberRetry = numberRetry;
            http.retry = true;
            return this;
        }

        public Builder setCompositeDisposable(CompositeDisposable mCompositeDisposable) {
            http.mCompositeDisposable = mCompositeDisposable;
            return this;
        }

        public Builder setRetry(boolean retry) {
            if (http.numberRetry != NUMBER_RETRY_DEFAULT) return this;
            http.retry = retry;
            return this;
        }

        public Builder setCache(boolean cache) {
            if (http.timeCache != TIME_CACHE_DEFAULT) return this;
            http.cache = cache;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            if (TextUtils.isEmpty(http.baseUrl)) http.baseUrl = baseUrl;
            return this;
        }

        public Builder setUrl(String url) {
            if (TextUtils.isEmpty(http.url)) http.url = url;
            return this;
        }

        public Builder setMethod(HttpMethod method) {
            if (http.method == null) http.method = method;
            return this;
        }

        public Builder putHeader(String key, String value) {
            if (value == null) value = "";
            http.header.put(key, value);
            return this;
        }

        public Builder putParameter(String key, String value) {
            if (value == null) value = "";
            http.params.put(key, value);
            return this;
        }

        public Builder putBody(Object value) {
            http.body = value;
            return this;
        }

        public Builder putFile(String key, String filepath) {
            http.files.put(key, filepath);
            return this;
        }

        public Builder withCallBack(HttpCallBack callBack) {
            http.callBack = callBack;
            return this;
        }
    }

    public void cancel() {
        Disposable disposable = Utils.getInstance().get(tag);
        if (disposable != null) disposable.dispose();
        if (progressRequestBody != null) {
            progressRequestBody.cancel();
            progressRequestBody = null;
        }
    }

    private Interceptor provideInterceptorCache() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                if (isNetworkAvailable(context)) {
                    int maxAge = timeCache;// lưu request trong vòng 1 phut
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // lưu request trong vòng 1 tuấn
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }

    private HttpService providerService() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /*
         * thiết lập cache cho api
         */
        if (cache && context != null) {
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            builder.addInterceptor(provideInterceptorCache());
            builder.cache(cache);
        }
        /*
         * thiết lập time out cho request
         */
//        OkHttpClient okHttpClient = builder
//                .connectTimeout(timeOut, TimeUnit.SECONDS)
//                .readTimeout(timeOut, TimeUnit.SECONDS)
//                .writeTimeout(timeOut, TimeUnit.SECONDS)
//                .build();
        OkHttpClient okHttpClient = HttpsTrustManager.getUnsafeOkHttpClient(timeOut, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(HttpService.class);
    }

    private Observable<ResponseBody> providerObservable() throws Exception {
        Observable<ResponseBody> observable = null;
        HttpService service = providerService();
        if (service != null) {
            Utilities.addDefaultParamsRequest(params);
            Utilities.addDefaultHeadersRequest(header);
            switch (method) {
                case GET:
                    observable = service.get(url, header, params);
                    break;
                case POST:
                    observable = service.post(url, header, params);
                    break;
                case FILE:
                    List<MultipartBody.Part> fileList = new ArrayList<>();
                    Set keyFile = files.keySet();
                    int position = 0;
                    int sum = fileList.size();
                    for (Object key1 : keyFile) {
                        String name = (String) key1;
                        String path = files.get(name);
                        fileList.add(prepareFilePart(name, path, sum, position, callBack));
                    }

                    Map<String, RequestBody> desMap = new HashMap<>();
                    Set keyDes = params.keySet();
                    for (Object key1 : keyDes) {
                        String key = (String) key1;
                        String value = params.get(key);
                        desMap.put(key, createPartFromString(value));
                    }

                    observable = service.file(url, header, fileList, desMap);
                    break;
                case POST_REQUEST_BODY:
//                    Map<String, RequestBody> desMapRB = new HashMap<>();
//                    Set keyDesc = body.keySet();
//                    for (Object key1 : keyDesc) {
//                        String key = (String) key1;
//                        String value = body.get(key);
//                        desMapRB.put(key, createPartFromString(value));
//                    }

                    observable = service.postByRequestBody(url, header, params, body);
                    break;
                default:
                    observable = service.get(url, header, params);
            }
        }
        printRequestInfo();
        return observable;
    }

    private void printRequestInfo() {
        if (BuildConfig.DEBUG) {
            String link = baseUrl + url;
            StringBuilder params = new StringBuilder();
            if (this.params != null) {
                Set<String> keys = this.params.keySet();

                for (String key : keys) {
                    if (key != null) {
                        params.append(key).append("=").append(URLEncoder.encode(this.params.get(key))).append("&");
                    }
                }
            }
            if (!TextUtils.isEmpty(params.toString())) {
                link += "?" + params;
                link = link.substring(0, link.length() - 1);
            }
            Log.d(TAG, "RequestInfo link: " + link);
            Log.d(TAG, "method: " + method);
            Log.d(TAG, "baseUrl: " + baseUrl);
            Log.d(TAG, "url: " + url);
            if (header != null) {
                Set<String> keys = header.keySet();
                for (String key : keys) {
                    if (key != null)
                        Log.d(TAG, "header -> " + key + ": " + header.get(key));
                }
            }
            Log.d(TAG, "RequestInfo --------------------------------------------------");
        }
    }

    private void execute() {
        if (TextUtils.isEmpty(baseUrl) || TextUtils.isEmpty(url)) return;
        Observer<ResponseBody> observer = new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (BuildConfig.DEBUG) Log.i(TAG, "onSubscribe tag: " + tag);
                if (!TextUtils.isEmpty(tag)) {
                    Utils.getInstance().put(tag, d);
                }
                if (mCompositeDisposable != null) {
                    mCompositeDisposable.add(d);
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    if (BuildConfig.DEBUG) Log.i(TAG, "onNext: " + data);
                    if (callBack != null) callBack.onSuccess(data);
                } catch (OutOfMemoryError e) {
                    onError(e);
                } catch (Exception e) {
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (BuildConfig.DEBUG) Log.e(TAG, "onError: " + url + "\t", e);
                if (callBack != null) callBack.onFailure(e.toString());
                onComplete();
            }

            @Override
            public void onComplete() {
                if (BuildConfig.DEBUG) Log.i(TAG, "onComplete tag: " + tag + "\nLink: " + url);
                if (callBack != null) callBack.onCompleted();
                if (!TextUtils.isEmpty(tag)) {
                    Utils.getInstance().remove(tag);
                }
            }
        };
        try {
            Observable<ResponseBody> observable = providerObservable();
            if (retry) observable.retry(numberRetry);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            observer.onError(e);
        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String name, String path, int sum, int position, HttpCallBack callBack) {
        File file = new File(path);
        progressRequestBody = new ProgressRequestBody(file, callBack, position, sum);
        return MultipartBody.Part.createFormData(name, file.getName(), progressRequestBody);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    static class Utils {
        private static Utils ourInstance;

        public static Utils getInstance() {
            if (ourInstance == null) ourInstance = new Utils();
            return ourInstance;
        }

        private final Map<String, Disposable> disposableMap;

        private Utils() {
            disposableMap = new HashMap<>();
        }

        void put(String tag, Disposable mDisposable) {
            disposableMap.put(tag, mDisposable);
        }

        Disposable get(String tag) {
            return disposableMap.get(tag);
        }

        void remove(String tag) {
            Disposable disposable = disposableMap.get(tag);
            if (disposable != null) {
                disposable.dispose();
                disposableMap.remove(tag);
            }
        }

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}