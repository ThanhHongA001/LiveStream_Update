package com.example.livestream_update.Ringme.Utils;


import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class LogV2 {
    public static boolean ENABLE_LOG = BuildConfig.DEBUG;
    public static final String TAG = "Log";
    private final SimpleDateFormat mFormatter;
    private final Date mDate;
    private final Logger mLogger;

    private static LogV2 sInstance;

    public static void initialize() {
        if (ENABLE_LOG) {
            LogV2 log = getInstance();
            if (log.mLogger != null) {
                try {
                    final int LOG_FILE_LIMIT = 5 * 1024 * 1024;
                    final int LOG_FILE_MAX_COUNT = 10;
                    StringBuilder logFilePath = new StringBuilder();
                    logFilePath.append(Environment.getExternalStorageDirectory()
                            + File.separator + "Kakoak" + File.separator + ".logs");
                    File directory = new File(logFilePath.toString());
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    logFilePath.append(File.separator);
                    logFilePath.append(TAG);
                    logFilePath.append("%g%u.log");
                    FileHandler fileHandler = new FileHandler(logFilePath.toString(), LOG_FILE_LIMIT, LOG_FILE_MAX_COUNT, true);
                    fileHandler.setFormatter(new Formatter() {
                        @Override
                        public String format(LogRecord r) {
                            return r.getMessage() + "\n";
                        }
                    });
                    log.mLogger.addHandler(fileHandler);
                } catch (IOException e) {
                    Log.e(TAG,"Exception",e);
                }
            }
        }
    }

    private LogV2() {
        mFormatter = new SimpleDateFormat("MM/dd HH:mm:ss.SSS", Locale.getDefault());
        mDate = new Date();
        mLogger = Logger.getLogger(TAG);
        if (mLogger != null) {
            mLogger.setLevel(Level.ALL);
            mLogger.setUseParentHandlers(false);
        } else {
            android.util.Log.e(TAG, "logger is null.");
        }
    }


    public static synchronized LogV2 getInstance() {
        if (sInstance == null) {
            sInstance = new LogV2();
        }
        return sInstance;
    }

    public static void i(String tag, String string) {
        if (ENABLE_LOG) {
            android.util.Log.i(tag, string);
        }
    }

    public static void i(String tag, String string, Throwable throwable) {
        if (ENABLE_LOG) {
            android.util.Log.i(tag, string, throwable);
        }
    }

    public static void e(String tag, String string) {
        if (ENABLE_LOG) {
            android.util.Log.e(tag, string);
        }
    }

    public static void e(String tag, String string, Throwable throwable) {
        if (ENABLE_LOG) {
            android.util.Log.e(tag, string, throwable);
        }
    }

    public static void d(String tag, String string) {
        if (ENABLE_LOG) {
            android.util.Log.d(tag, string);
        }
    }

    public static void d(String tag, String string, Throwable throwable) {
        if (ENABLE_LOG) {
            android.util.Log.d(tag, string, throwable);
        }
    }

    public static void v(String tag, String string) {
        if (ENABLE_LOG) {
            android.util.Log.v(tag, string);
        }
    }

    public static void v(String tag, String string, Throwable throwable) {
        if (ENABLE_LOG) {
            android.util.Log.v(tag, string, throwable);
        }
    }

    public static void w(String tag, String string) {
        if (ENABLE_LOG) {
            android.util.Log.w(tag, string);
        }
    }

    public static void w(String tag, String string, Throwable throwable) {
        if (ENABLE_LOG) {
            android.util.Log.w(tag, string, throwable);
        }
    }

    public static void f(String tag, String format, Object... args) {
        if (ENABLE_LOG) {
            LogV2 log = getInstance();
            String message = log.buildMessage(format, args);
            if (log.mLogger != null && message != null) {
                message = "[F]" + message;
                log.mLogger.severe(message);
                android.util.Log.d(tag, format);
            } else {
                android.util.Log.d(tag, format);
            }
        }
    }

    public static void f(String tag, String format, Throwable throwable, Object... args) {
        if (ENABLE_LOG) {
            LogV2 log = getInstance();
            String message = log.buildMessage(format, args);
            if (log.mLogger != null && message != null) {
                message = "[F]" + message;
                log.mLogger.severe(message);
                android.util.Log.d(tag, format, throwable);
            } else {
                android.util.Log.d(tag, format, throwable);
            }
        }
    }

    private String buildMessage(String format, Object... args) {
        if (ENABLE_LOG) {
            String msg = (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
            StringBuilder caller = new StringBuilder();
            try {
                // Walk up the stack looking for the first caller outside.
                // It will be at least two frames up, so start there.
                StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                StackTraceElement element = trace[4];
                String clsName = element.getClassName();
                caller.append(clsName.substring(clsName.lastIndexOf('.') + 1));
                caller.append('.');
                caller.append(element.getMethodName());
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(TAG,"Exception",e);
                caller.append("<unknown>");
            }
            mDate.setTime(System.currentTimeMillis());
            return String.format(Locale.US, " %s:[%d] %s: %s",
                    mFormatter.format(mDate), Thread.currentThread().getId(), caller, msg);
        } else {
            try {
                return (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
            } catch (Exception e) {
                Log.e(TAG,"Exception",e);
                return String.format(Locale.US, "format:%s, %s", format, e);
            }
        }
    }
}