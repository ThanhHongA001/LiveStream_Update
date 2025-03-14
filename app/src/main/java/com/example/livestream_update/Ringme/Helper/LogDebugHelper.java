package com.example.livestream_update.Ringme.Helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import com.vtm.ringme.model.LogDebugModel;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.LogDebugDatasource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by thanhnt72 on 4/5/2018.
 */

public class LogDebugHelper {

    private static final String TAG = LogDebugHelper.class.getSimpleName();
    public static final String PREF_UPLOAD_LOG_DEBUG_LAST_TIME = "upload_log_debug_last_time";

    public static final String TYPE_WIFI = "WIFI";
    public static final String TYPE_MOBILE = "MOBILE";
    public static final String TYPE_NA = "N/A";


    private final Context context;
    private final SharedPreferences mPref;
    boolean enableLog = true;

    private static LogDebugHelper mInstance;
    private final LogDebugDatasource logDebugDatasource;

    public static synchronized LogDebugHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LogDebugHelper(context);
        }
        return mInstance;
    }

    public static LogDebugHelper getInstance() {
        return mInstance;
    }

    public LogDebugHelper(Context context) {
        this.context = context;
        mPref = context.getSharedPreferences("com.viettel.reeng.app", Context.MODE_PRIVATE);
        getStateEnableLog();
        logDebugDatasource = LogDebugDatasource.getInstance(DBChatLibHelper.getInstance
                (context));
    }

    public void getStateEnableLog() {
//        enableLog = mPref.getBoolean("enable_log_debug", false);
        enableLog = mPref.getBoolean("enable_log_debug", true);
        Log.i(TAG, "getStateEnableLog: " + enableLog);
    }

    public void setStateEnableLog(boolean enableLog) {
        Log.i(TAG, "setStateEnableLog: " + enableLog);
//        this.enableLog = enableLog;
        this.enableLog = true;
        mPref.edit().putBoolean("enable_log_debug", enableLog).apply();
        if (!enableLog) {
            clearDb();
        }
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public void logDebugContent(String content) {
        if (enableLog) {
            Log.i(TAG, "logDebugContent: " + content);
            new InsertLogAsyncTask().execute(content);
        }
    }

    public void logDebugContentNetworkType(String content) {
        if (enableLog) {
            Log.i(TAG, "logDebugContent: " + content);
            String networkType = checkTypeConnection(context);
            logDebugDatasource.insertLogDebug(new LogDebugModel(System.currentTimeMillis(), networkType + " | " + content));
        }
    }


    public static String checkTypeConnection(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NA;
    }

    public void clearDb() {
        logDebugDatasource.deleteTable();
    }

    public void saveDataToFile(SaveFileListener listener) {
        GetFileAsynctask asynctask = new GetFileAsynctask(listener);
        if (Build.VERSION.SDK_INT >= 11) {
            asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asynctask.execute();
        }

    }

    class GetFileAsynctask extends AsyncTask<Void, Void, String> {

        SaveFileListener listener;

        public GetFileAsynctask(SaveFileListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                ArrayList<LogDebugModel> listLog = logDebugDatasource.getAllLogDebug();
                if (listLog != null && !listLog.isEmpty()) {
                    StringBuilder data = new StringBuilder();
                    for (LogDebugModel logModel : listLog) {
                        data.append(logModel.toString());
                        data.append("\n");
                    }
                    String filePath = writeToFile(data.toString());
                    return filePath;
                }
            } catch (OutOfMemoryError ex) {
                Log.e(TAG, "--OutOfMemoryError");
                clearDb();
            } catch (Exception e) {
                Log.e(TAG, "--Exception: " + e);
                clearDb();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onSaveFileSuccess(s);
        }
    }

    private String writeToFile(String data) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Kakoak" + File.separator + ".logs" + File.separator + "logdebug";
        try {
            File myFile = new File(filePath);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            return null;
        }
        return filePath;
    }

    public interface SaveFileListener {
        void onSaveFileSuccess(String filePath);
    }

    public class InsertLogAsyncTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            logDebugDatasource.insertLogDebug(new LogDebugModel(System.currentTimeMillis(), strings[0]));
            return null;
        }
    }
}
