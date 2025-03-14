package com.example.livestream_update.Ringme.TabVideo;

import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
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
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
