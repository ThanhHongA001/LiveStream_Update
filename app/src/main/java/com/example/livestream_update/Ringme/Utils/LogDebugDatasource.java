package com.example.livestream_update.Ringme.Utils;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vtm.ringme.helper.DBChatLibHelper;
import com.vtm.ringme.model.LogDebugModel;
import com.vtm.ringme.values.LogDebugConstant;

import java.util.ArrayList;

/**
 * Created by thanhnt72 on 4/4/2018.
 */

public class LogDebugDatasource {
    private static String TAG = LogDebugDatasource.class.getSimpleName();
    private SQLiteDatabase databaseRead;
    private SQLiteDatabase databaseWrite;
    private static LogDebugDatasource mInstance;


    public static synchronized LogDebugDatasource getInstance(DBChatLibHelper dbChatLibHelper) {
        if (mInstance == null) {
            mInstance = new LogDebugDatasource(dbChatLibHelper);
        }
        return mInstance;
    }

    private LogDebugDatasource(DBChatLibHelper dbChatLibHelper) {
        databaseRead = dbChatLibHelper.getMyReadableDatabase();
        databaseWrite = dbChatLibHelper.getMyWritableDatabase();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

    public void insertLogDebug(LogDebugModel logDebugModel) {
        try {
            if (databaseWrite == null) {
                return;
            }
            Log.i("LogDebugDatasource", "insert: " + logDebugModel.toString());
            ContentValues values = new ContentValues();
            values.put(LogDebugConstant.CONTENT, logDebugModel.getContent());
            values.put(LogDebugConstant.TIMESTAMP, logDebugModel.getTimeStamp());
            databaseWrite.insert(LogDebugConstant.TABLE, null, values);
        } catch (Exception e) {
            Log.e(TAG, "insertNumber", e);
        }
    }

    // insert list item
    public void insertListLogDebug(ArrayList<LogDebugModel> listLog) {
        if (listLog == null || listLog.isEmpty()) {
            return;
        }
        try {
            databaseWrite.beginTransaction();
            try {
                for (LogDebugModel logDebugModel : listLog) {
                    ContentValues values = new ContentValues();
                    values.put(LogDebugConstant.CONTENT, logDebugModel.getContent());
                    values.put(LogDebugConstant.TIMESTAMP, logDebugModel.getTimeStamp());
                    databaseWrite.insert(LogDebugConstant.TABLE, null, values);
                }
                databaseWrite.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, "Transaction", e);
            } finally {
                databaseWrite.endTransaction();
            }
        } catch (Exception e) {
            Log.e(TAG, "insertListLog", e);
        }
    }

    public ArrayList<LogDebugModel> getAllLogDebug() {
        Cursor cursor = null;
        ArrayList<LogDebugModel> listLog = null;
        try {
            if (databaseRead == null) {
                return null;
            }
            cursor = databaseRead.rawQuery(LogDebugConstant.SELECT_ALL_STATEMENT, null);
            if (cursor != null && cursor.getCount() > 0) {
                listLog = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        long timeStamp = cursor.getLong(1);
                        String content = cursor.getString(2);
                        listLog.add(new LogDebugModel(timeStamp, content));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllLog", e);
        } finally {
            closeCursor(cursor);
        }
        return listLog;
    }

    // del table
    public void deleteTable() {
        try {
            databaseWrite.execSQL(LogDebugConstant.DELETE_ALL_STATEMENT);
        } catch (Exception e) {
            Log.e(TAG, "deleteAllTable", e);
        }
    }
}

