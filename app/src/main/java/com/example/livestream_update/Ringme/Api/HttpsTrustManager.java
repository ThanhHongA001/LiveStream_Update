package com.example.livestream_update.Ringme.Api;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpsTrustManager{
    private static final int TIME_CACHE_DEFAULT = 60;
    public static OkHttpClient getUnsafeOkHttpClient(long timeOut, boolean isLog) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            if (chain == null || chain.length == 0)
                                throw new IllegalArgumentException("Certificate is null or empty");
                            if (authType == null || authType.length() == 0)
                                throw new IllegalArgumentException("Authtype is null or empty");
                            if (!authType.equalsIgnoreCase("ECDHE_RSA") &&
                                    !authType.equalsIgnoreCase("ECDHE_ECDSA") &&
                                    !authType.equalsIgnoreCase("RSA") &&
                                    !authType.equalsIgnoreCase("ECDSA"))
                                throw new CertificateException("Certificate is not trust");
                            try {
                                chain[0].checkValidity();
                            } catch (Exception e) {
                                throw new CertificateException("Certificate is not valid or trusted");
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
            if(isLog){
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }

            return builder
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .writeTimeout(timeOut, TimeUnit.SECONDS)
                    .addInterceptor(provideInterceptorCache())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Interceptor provideInterceptorCache() {
        return new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                if(chain.request().toString().isEmpty()) {
                    return null;
                }
                Response originalResponse = chain.proceed(chain.request());
                if (NetworkUtils.isConnected()) {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + TIME_CACHE_DEFAULT)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // lưu request trong vòng 1 tuần
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }

    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}