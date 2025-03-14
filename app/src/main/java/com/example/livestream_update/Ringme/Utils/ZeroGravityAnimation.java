package com.example.livestream_update.Ringme.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.values.Direction;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class ZeroGravityAnimation extends AsyncTask<String, Void, Bitmap> {

    private static final int RANDOM_DURATION = -1;

    private static final String AVATAR_FULL = "/api/thumbnail/download-orginal?v=%1$s&ac=%2$s&t=%3$s&u=%4$s";
    private ApplicationController app = ApplicationController.self();

    private Direction mOriginationDirection = Direction.RANDOM;
    private Direction mDestinationDirection = Direction.RANDOM;
    private int mDuration = RANDOM_DURATION;
    private int mCount = 1;
    private int mImageResId;
    private float mScalingFactor = 1f;
    private Animation.AnimationListener mAnimationListener;
    private Bitmap avtBitmap;
    private Context context;

    public ZeroGravityAnimation(Context context) {
        this.context = context;
    }

    /**
     * Sets the orignal direction. The animation will originate from the given direction.
     */
    public ZeroGravityAnimation setOriginationDirection(Direction direction) {
        this.mOriginationDirection = direction;
        return this;
    }

    /**
     * Sets the animation destination direction. The translate animation will proceed towards the given direction.
     *
     * @param direction
     * @return
     */
    public ZeroGravityAnimation setDestinationDirection(Direction direction) {
        this.mDestinationDirection = direction;
        return this;
    }

    /**
     * Will take a random time duriation for the animation
     *
     * @return
     */
    public ZeroGravityAnimation setRandomDuration() {
        return setDuration(RANDOM_DURATION);
    }

    /**
     * Sets the time duration in millseconds for animation to proceed.
     *
     * @param duration
     * @return
     */
    public ZeroGravityAnimation setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * Sets the image reference id for drawing the image
     *
     * @param resId
     * @return
     */
    public ZeroGravityAnimation setImage(int resId) {
        this.mImageResId = resId;
        return this;
    }

    /**
     * Sets the image scaling value.
     *
     * @param scale
     * @return
     */
    public ZeroGravityAnimation setScalingFactor(float scale) {
        this.mScalingFactor = scale;
        return this;
    }

    public ZeroGravityAnimation setAnimationListener(Animation.AnimationListener listener) {
        this.mAnimationListener = listener;
        return this;
    }

    public ZeroGravityAnimation setCount(int count) {
        this.mCount = count;
        return this;
    }


    /**
     * Starts the Zero gravity animation by creating an OTT and attach it to th given ViewGroup
     *
     * @param activity
     * @param ottParent
     */
    public void play(AppCompatActivity activity, ViewGroup ottParent, boolean isUserClick) throws IOException {

        DirectionGenerator generator = new DirectionGenerator();

        if (mCount > 0) {

            for (int i = 0; i < mCount; i++) {

                final int iDupe = i;

                Direction origin = mOriginationDirection == Direction.RANDOM ? generator.getRandomDirection() : mOriginationDirection;
                Direction destination = mDestinationDirection == Direction.RANDOM ? generator.getRandomDirection(origin) : mDestinationDirection;

                int startingPoints[] = generator.getPointsInDirection(activity, origin, isUserClick);
                int endPoints[] = generator.getPointsInDirection(activity,destination, isUserClick);
                Bitmap bitmap;
                if (isUserClick) {
                    bitmap = avtBitmap;
                } else bitmap = BitmapFactory.decodeResource(activity.getResources(), mImageResId);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * mScalingFactor), (int) (bitmap.getHeight() * mScalingFactor), false);

                switch (origin) {
                    case LEFT:
                        startingPoints[0] -= scaledBitmap.getWidth();
                        break;

                    case RIGHT:
                        startingPoints[0] += scaledBitmap.getWidth();
                        break;

                    case TOP:
                        startingPoints[1] -= scaledBitmap.getHeight();
                        break;
                    case BOTTOM:
                        startingPoints[1] += scaledBitmap.getHeight();
                        break;
                }

                switch (destination) {
                    case LEFT:
                        endPoints[0] -= scaledBitmap.getWidth();
                        break;

                    case RIGHT:
                        endPoints[0] += scaledBitmap.getWidth();
                        break;

                    case TOP:
                        endPoints[1] -= scaledBitmap.getHeight();
                        break;
                    case BOTTOM:
                        endPoints[1] += scaledBitmap.getHeight();
                        break;
                }


                final OverTheTopLayer layer = new OverTheTopLayer();
                FrameLayout ottLayout = layer.with(activity)
                        .scale(mScalingFactor)
                        .attachTo(ottParent)
                        .setBitmap(scaledBitmap, startingPoints)
                        .create();


                int deltaX = endPoints[0] - startingPoints[0];
                int deltaY = endPoints[1] - startingPoints[1];

//                int duration = mDuration;
                int duration = 3000;
                if (isUserClick) duration = 1000;

                TranslateAnimation animation = new TranslateAnimation(0, deltaX, 0, deltaY);
                animation.setDuration(duration);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        if (iDupe == 0) {
                            if (mAnimationListener != null) {
                                mAnimationListener.onAnimationStart(animation);
                            }
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layer.destroy();

                        if (iDupe == (mCount - 1)) {
                            if (mAnimationListener != null) {
                                mAnimationListener.onAnimationEnd(animation);
                                if (isUserClick) {
                                    try {
                                        playContinue(activity, ottParent, startingPoints[0], endPoints[1]);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if (!isUserClick) {
                    layer.applyAnimation(animation);
                } else {
                    layer.applyPopAnimation(animation);
                }

            }
        } else {

            Log.e(ZeroGravityAnimation.class.getSimpleName(), "Count was not provided, animation was not started");
        }
    }

    public void playContinue(Activity activity, ViewGroup ottParent, int startX, int startY) throws IOException {

        DirectionGenerator generator = new DirectionGenerator();

        if (mCount > 0) {

            for (int i = 0; i < mCount; i++) {


                final int iDupe = i;

                Direction origin = mOriginationDirection == Direction.RANDOM ? generator.getRandomDirection() : mOriginationDirection;
                Direction destination = mDestinationDirection == Direction.RANDOM ? generator.getRandomDirection(origin) : mDestinationDirection;

                int startingPoints[] = generator.getPointsInDirection(activity, origin, false);
                int endPoints[] = generator.getPointsInDirection(activity,destination, false);
                startingPoints[0] = startX;
                startingPoints[1] = startY;

                Bitmap  bitmap = BitmapFactory.decodeResource(activity.getResources(), mImageResId);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.25f), (int) (bitmap.getHeight() * 0.25f), false);

                switch (origin) {
                    case LEFT:
                        startingPoints[0] -= scaledBitmap.getWidth();
                        break;

                    case RIGHT:
                        startingPoints[0] += scaledBitmap.getWidth();
                        break;

                    case TOP:
                        startingPoints[1] -= scaledBitmap.getHeight();
                        break;
                    case BOTTOM:
//                        startingPoints[1] += scaledBitmap.getHeight();
                        break;
                }

                switch (destination) {
                    case LEFT:
                        endPoints[0] -= scaledBitmap.getWidth();
                        break;

                    case RIGHT:
                        endPoints[0] += scaledBitmap.getWidth();
                        break;

                    case TOP:
                        endPoints[1] -= scaledBitmap.getHeight();
                        break;
                    case BOTTOM:
                        endPoints[1] += scaledBitmap.getHeight();
                        break;
                }


                final OverTheTopLayer layer = new OverTheTopLayer();

                FrameLayout ottLayout = layer.with(activity)
                        .scale(mScalingFactor)
                        .attachTo(ottParent)
                        .setBitmap(scaledBitmap, startingPoints)
                        .create();


                switch (origin) {
                    case LEFT:

                }

                int deltaX = endPoints[0] - startingPoints[0];
                int deltaY = endPoints[1] - startingPoints[1];

//                int duration = mDuration;
                int duration = 2000;

                TranslateAnimation animation = new TranslateAnimation(0, deltaX, 0, deltaY);
                animation.setDuration(duration);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        if (iDupe == 0) {
                            if (mAnimationListener != null) {
                                mAnimationListener.onAnimationStart(animation);
                            }
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layer.destroy();

                        if (iDupe == (mCount - 1)) {
                            if (mAnimationListener != null) {
                                mAnimationListener.onAnimationEnd(animation);
                            }
                        }
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layer.applyContinueAnimation(animation);

            }
        } else {

            Log.e(ZeroGravityAnimation.class.getSimpleName(), "Count was not provided, animation was not started");
        }
    }

    /**
     * Takes the content view as view parent for laying the animation objects and starts the animation.
     *
     * @param activity - activity on which the zero gravity animation should take place.
     */
    public void play(AppCompatActivity activity) throws IOException {

        play(activity, null, false);

    }

    private Bitmap getCircleFromBitmap(Bitmap bitmapimg) {
        if (bitmapimg == null) return null;
        int w = bitmapimg.getWidth();
        int h = bitmapimg.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 20, h + 20, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 10, (h / 2) + 10, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmapimg, 10, 10, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.parseColor("#E5FFCC"));
        p.setStrokeWidth(3);
        c.drawCircle((w / 2) + 10, (h / 2) + 10, radius, p);

        return output;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            if(!TextUtils.isEmpty(strings[0])) {
                bitmap = getCircleFromBitmap(BitmapFactory.decodeStream(getInputStream(strings[0])));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("reactError", "onFunctionClick: " + e);
        }
        if (bitmap == null) {
            bitmap = getCircleFromBitmap(BitmapFactory.decodeResource(app.getResources(), R.drawable.rm_ic_avatar_default, options));
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        avtBitmap = bitmap;
    }

    private TrustManager[] getWrappedTrustManagers() {
//        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
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
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
//            KeyStore keyStore = KeyStore.getInstance("BKS");
//            keyStore.load(null, null);
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getWrappedTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            return HttpsURLConnection.getDefaultSSLSocketFactory();
        }
    }

    private InputStream getInputStream(String urlPath) {
        try {
            URL url = new URL(urlPath);
            String token = "rbkY34HnL...";
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(getSSLSocketFactory());
            urlConnection.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.connect();
            InputStream inputStream;
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getErrorStream();
            } else {
                inputStream = urlConnection.getInputStream();
            }
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
