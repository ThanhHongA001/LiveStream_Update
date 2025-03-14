package com.example.livestream_update.Ringme.Helper.emoticon;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.text.Spanned;

import androidx.collection.LruCache;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.EmoBitmapDrawable;
import com.vtm.ringme.helper.MessageHelper;
import com.vtm.ringme.utils.Log;

import java.io.InputStream;

public class EmoticonManager {
    private static final String TAG = "EmoticonManager";
    private static final int EMO_CACHE_SIZE = 4 * 1024 * 1024;
    private static EmoticonManager instance = null;
    private ImageGetter imageGetter;
    private Bitmap[] emoticons;
    private double emoWeightRatio = 1.25;
    private int emoWeight;
    private Resources mRes;
    private ApplicationController mApplication;
    private int emoWeightBitmap;
    //cache emo
    private LruCache<String, Object> mEmoticonCache = null;

    public static synchronized EmoticonManager getInstance(Context context) {
        if (instance == null) {
            instance = new EmoticonManager(context);
            Log.i(TAG, "instance == null");
        }
        return instance;
    }

    private EmoticonManager(Context ctx) {
        mApplication = (ApplicationController) ctx.getApplicationContext();
        mRes = mApplication.getResources();
        emoWeight = (int) (mRes.getDimensionPixelSize(R.dimen.kakoak_text_size_level_2) * emoWeightRatio);
        emoWeightBitmap = (int) (mRes.getDimensionPixelSize(R.dimen.kakoak_text_size_level_2_5) * 1.5);
        ///
        mEmoticonCache = new LruCache<>(EMO_CACHE_SIZE);
        readEmoticons();
        imageGetter = new ImageGetter() {
            public Drawable getDrawable(String source) {
                Drawable d = new EmoBitmapDrawable(
                        mRes, emoticons[Integer.valueOf(source) - 1],
                        EmoticonUtils.EMOTICON_TEXTS[Integer.valueOf(source) - 1]);
                d.setBounds(0, 0, emoWeight, emoWeight);
                return d;
            }
        };
    }

    private void readEmoticons() {
        emoticons = new Bitmap[EmoticonUtils.NO_OF_EMOTICONS];
        for (short i = 0; i < EmoticonUtils.NO_OF_EMOTICONS; i++) {
            emoticons[i] = getImageOfEmoticon(EmoticonUtils.EMOTICON_KEYS[i] + ".png");
        }
    }

    public ImageGetter getImageGetter() {
        return imageGetter;
    }

    /**
     * For loading smiles from assets
     *
     * @author cngp_thaodv
     */
    public Bitmap getImageOfEmoticon(String path) {
        AssetManager mngr = mRes.getAssets();
        InputStream in = null;
        String assetPath = "emoticons/" + path;
        try {
            in = mngr.open(assetPath);
        } catch (Exception e) {
            Log.e(TAG,"Exception",e);
        }
        //
        Bitmap bm = getBitmapFromCache(assetPath);
        if (bm == null) {
            // Bitmap temp = BitmapFactory.decodeStream(in, null, null);
            bm = MessageHelper.decodeEmoticonIcon(in, emoWeightBitmap);
            addBitmapToCache(assetPath, bm);
        }
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                Log.e(TAG,"Exception",e);
            }
        }
        return bm;
    }

    /**
     * cache
     */
    private Bitmap getBitmapFromCache(String assetPath) {
        if (assetPath == null) {
            return null;
        }
        if (mEmoticonCache == null) {
            return null;
        }
        return (Bitmap) mEmoticonCache.get(assetPath);
    }

    private void addBitmapToCache(String assetPath, Bitmap bm) {
        if (assetPath == null || bm == null) {
            return;
        }
        if (getBitmapFromCache(assetPath) == null) {
            mEmoticonCache.put(assetPath, bm);
        }
    }

    /**
     * text spanned
     */
    public void addSpannedToEmoticonCache(String key, Spanned spanned) {
        if (key == null || spanned == null) {
            return;
        }
        //        Log.d(TAG, "addSpanned to cache:" + key);
        //        neu chua cache thi moi add
        if (getSpannedFromEmoticonCache(key) == null) {
            mEmoticonCache.put(key, spanned);
        }
    }

    public Spanned getSpannedFromEmoticonCache(String key) {
        if (key == null) {
            return null;
        }
        if (mEmoticonCache == null) {
            return null;
        }
        return (Spanned) mEmoticonCache.get(key);
    }
}