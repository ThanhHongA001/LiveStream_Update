/*
 * Copyright (C) 2015 Lyft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.livestream_update.Ringme.CropImageNew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Surface;

import androidx.annotation.Nullable;

import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.utils.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Utils {

    public static void checkArg(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void checkNotNull(Object object, String msg) {
        if (object == null) {
            throw new NullPointerException(msg);
        }
    }

    public static Bitmap asBitmap(Drawable drawable, int minWidth, int minHeight) {
        final Rect tmpRect = new Rect();
        drawable.copyBounds(tmpRect);
        if (tmpRect.isEmpty()) {
            tmpRect.set(0, 0, Math.max(minWidth, drawable.getIntrinsicWidth()), Math.max(minHeight, drawable.getIntrinsicHeight()));
            drawable.setBounds(tmpRect);
        }
        Bitmap bitmap = Bitmap.createBitmap(tmpRect.width(), tmpRect.height(), Bitmap.Config.ARGB_8888);
        drawable.draw(new Canvas(bitmap));
        return bitmap;
    }

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final String TAG = "scissors.Utils";

    public static Future<Void> flushToFile(final Bitmap bitmap,
                                           final Bitmap.CompressFormat format,
                                           final int quality,
                                           final File file) {

        return EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;

                try {
                    file.getParentFile().mkdirs();
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(format, quality, outputStream);
                    outputStream.flush();
                } catch (final Throwable throwable) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "Error attempting to save bitmap.", throwable);
                    }
                } finally {
                    closeQuietly(outputStream);
                }
            }
        }, null);
    }

    public static Future<Void> flushToStream(final Bitmap bitmap,
                                             final Bitmap.CompressFormat format,
                                             final int quality,
                                             final OutputStream outputStream,
                                             final boolean closeWhenDone) {

        return EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap.compress(format, quality, outputStream);
                    outputStream.flush();
                } catch (final Throwable throwable) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "Error attempting to save bitmap.", throwable);
                    }
                } finally {
                    if (closeWhenDone) {
                        closeQuietly(outputStream);
                    }
                }
            }
        }, null);
    }

    private static void closeQuietly(@Nullable OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error attempting to close stream.", e);
        }
    }

    public static Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        if (src == null) {
            return null;
        }

        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

    public static void startBackgroundJob(MonitoredActivity activity,
                                          String title, String message, Runnable job, Handler handler) {
        // Make the progress dialog uncancelable, so that we can gurantee
        // the thread will be done before the activity getting destroyed.
        ProgressDialog dialog = ProgressDialog.show(
                activity, title, message, true, false);
        new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
    }

    private static class BackgroundJob
            extends MonitoredActivity.LifeCycleAdapter implements Runnable {
        private final MonitoredActivity mActivity;
        private final ProgressDialog mDialog;
        private final Runnable mJob;
        private final Handler mHandler;
        private final Runnable mCleanupRunner = new Runnable() {
            public void run() {

                mActivity.removeLifeCycleListener(BackgroundJob.this);
                if (mDialog.getWindow() != null) mDialog.dismiss();
            }
        };

        public BackgroundJob(MonitoredActivity activity, Runnable job,
                             ProgressDialog dialog, Handler handler) {
            mActivity = activity;
            mDialog = dialog;
            mJob = job;
            mActivity.addLifeCycleListener(this);
            mHandler = handler;
        }

        public void run() {
            try {
                mJob.run();
            } finally {
                mHandler.post(mCleanupRunner);
            }
        }


        @Override
        public void onActivityDestroyed(MonitoredActivity activity) {
            // We get here only when the onDestroyed being called before
            // the mCleanupRunner. So, run it now and remove it from the queue
            mCleanupRunner.run();
            mHandler.removeCallbacks(mCleanupRunner);
        }

        @Override
        public void onActivityStopped(MonitoredActivity activity) {
            mDialog.hide();
        }

        @Override
        public void onActivityStarted(MonitoredActivity activity) {
            mDialog.show();
        }
    }

    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            Log.e(TAG, "Throwable", t);
        }
    }

    public static int getOrientationInDegree(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }
}
