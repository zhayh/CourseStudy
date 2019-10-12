package edu.niit.android.course.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;


public class HttpsUtil {

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    public static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }
        };
        return trustAllCerts;
    }

    // 实现HostnameVerifier接口(信任所有的 https 证书)
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

//    public static void handleSSLHandshakeByVolley() {
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, getTrustManager(), new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new TrustAllHostnameVerifier());
//        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//            e.printStackTrace();
//        }
//    }

    public static OkHttpClient handleSSLHandshakeByOkHttp() {
        X509TrustManager trustManager = (X509TrustManager) getTrustManager()[0];

        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                // 支持HTTPS请求，跳过证书验证
                .sslSocketFactory(getSSLSocketFactory(), trustManager)
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }

//    public static Retrofit handleSSHHandshakeByRetrofit(String baseUrl) {
//        OkHttpClient okHttpClient = handleSSLHandshakeByOkHttp();
//        return new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(baseUrl)
//                .client(okHttpClient)
//                .build();
//    }
//    retrofitAPI = new Retrofit.Builder()
//            .baseUrl(AppConfig.baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//            .client(mHttpClient)//配置okhttp
//                    .build()
//                    .create(RetrofitAPI.class);
}
