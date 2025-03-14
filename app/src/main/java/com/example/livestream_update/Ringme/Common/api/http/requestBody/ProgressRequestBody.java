package com.example.livestream_update.Ringme.Common.api.http.requestBody;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.api.http.HttpProgressCallBack;
import com.vtm.ringme.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private static final String TAG = "ProgressRequestBody";

    private final File mFile;
    private HttpCallBack mListener;
    private final int position;
    private final int sum;
    private Handler handler;
    private int progress;
    private String keyCheck;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private int count = 0;

    public ProgressRequestBody(File mFile, HttpCallBack listener, int position, int sum) {
        this.mFile = mFile;
        this.mListener = listener;
        this.position = position;
        this.progress = 0;
        this.sum = sum;
        this.keyCheck = String.valueOf(System.currentTimeMillis());
        if (BuildConfig.DEBUG) this.keyCheck += " - " + mFile.getAbsolutePath();
        if (mListener != null) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 100) {
                        if (mListener instanceof HttpProgressCallBack) {
                            ((HttpProgressCallBack) mListener).onProgressUpdate(ProgressRequestBody.this.position,
                                    ProgressRequestBody.this.sum, progress);
                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, "keyCheck: " + keyCheck
                                        + "\nposition: " + ProgressRequestBody.this.position
                                        + "\nsum: " + ProgressRequestBody.this.sum
                                        + "\nprogress: " + ProgressRequestBody.this.progress
                                );
                            }
                        }
                        if (progress >= 100) {
                            handler.removeMessages(100);
                            handler = null;
                        } else {
                            handler.sendEmptyMessageDelayed(100, 800);
                            count++;
                            if(progress == 0 && count == 5) {
                                handler.removeMessages(100);
                                handler = null;
                                count = 0;
                            }
                        }
                    }
                }
            };
            handler.sendEmptyMessage(100);
        }
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
        return MediaType.parse("image/*");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = contentLength();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                // update progress on UI thread
                uploaded += read;
                sink.write(buffer, 0, read);
                progress = (int) (100 * uploaded / fileLength);

            }
        } finally {
            in.close();
        }
    }

    public void cancel() {
        if (handler != null) {
            handler.removeMessages(100);
            handler = null;
        }
        mListener = null;
    }
//    private class ProgressUpdater implements Runnable {
//        private long mUploaded;
//        private long mTotal;
//
//        ProgressUpdater(long uploaded, long total) {
//            mUploaded = uploaded;
//            mTotal = total;
//        }
//
//        @Override
//        public void run() {
//            if (mListener instanceof HttpProgressCallBack)
//                ((HttpProgressCallBack) mListener).onProgressUpdate(position, sum, (int) (100 * mUploaded / mTotal));
//        }
//    }
}