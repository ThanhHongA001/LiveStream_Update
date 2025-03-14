package com.example.livestream_update.Ringme.Helper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vtm.ringme.values.LogDebugConstant;

/**
 * Created by thanhnt72 on 4/5/2018.
 */

public class DBChatLibHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LogDebug.db";

    private static SQLiteDatabase mWritableDb;
    private static SQLiteDatabase mReadableDb;
    private static DBChatLibHelper mInstance;

    public DBChatLibHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static synchronized DBChatLibHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBChatLibHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LogDebugConstant.CREATE_STATEMENT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static synchronized SQLiteDatabase getMyWritableDatabase() {
        if ((mWritableDb == null) || (!mWritableDb.isOpen())) {
            mWritableDb = mInstance.getWritableDatabase();
        }
        return mWritableDb;
    }

    public static synchronized SQLiteDatabase getMyReadableDatabase() {
        if ((mReadableDb == null) || (!mReadableDb.isOpen())) {
            mReadableDb = mInstance.getReadableDatabase();
        }
        return mReadableDb;
    }
}
