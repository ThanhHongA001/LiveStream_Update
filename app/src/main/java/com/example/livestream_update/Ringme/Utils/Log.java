package com.example.livestream_update.Ringme.Utils;

public class Log {

    public static void setEnableLog(boolean enable) {
        LogV2.ENABLE_LOG = enable;
        new Thread(LogV2::initialize);
    }

    private static final boolean ENABLE_LOG = BuildConfig.DEBUG;
    private static final String LOG_TAG = "Keeng -> ";

    public static void i(String message) {
        i(LOG_TAG, message);
    }

    public static void d(String message) {
        d(LOG_TAG, message);
    }

    public static void e(String message) {
        e(LOG_TAG, message);
    }

    public static void i(String tag, String string) {
        LogV2.i(tag, string);
    }

    public static void i(String tag, String string, Throwable throwable) {
        LogV2.i(tag, string, throwable);
    }

    public static void e(String tag, String string) {
        LogV2.e(tag, string);
    }

    public static void e(String tag, String string, Throwable throwable) {
        LogV2.e(tag, string, throwable);
    }

    public static void e(String tag, Throwable throwable) {
        if (throwable != null)
            LogV2.e(tag, throwable.getMessage(), throwable);
    }

    public static void d(String tag, String string) {
        LogV2.d(tag, string);
    }

    public static void d(String tag, String string, Throwable throwable) {
        LogV2.d(tag, string, throwable);
    }

    public static void v(String tag, String string) {
        LogV2.v(tag, string);
    }

    public static void v(String tag, String string, Throwable throwable) {
        LogV2.v(tag, string, throwable);
    }

    public static void w(String tag, String string) {
        LogV2.w(tag, string);
    }

    public static void w(String tag, String string, Throwable throwable) {
        LogV2.w(tag, string, throwable);
    }

    public static void f(String tag, String string) {
        LogV2.f(tag, string);
    }

    public static void f(String tag, String string, Throwable throwable) {
        LogV2.f(tag, string, throwable);
    }

    public static void sys(String tag, String string) {
        System.out.println(tag + "," + string);
    }

    public static void sys(String tag, String string, boolean isFile) {
        if (isFile)
            LogV2.f(tag, string);
        System.out.println(tag + "," + string);
    }
}