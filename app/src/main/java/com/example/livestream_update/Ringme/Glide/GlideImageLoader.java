package com.example.livestream_update.Ringme.Glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.vtm.ringme.ApplicationController;

public class GlideImageLoader {

    private ImageView mImageView;
    private SimpleImageLoadingListener mSimpleListener;
    private ImageLoadingListener mListener;

    public GlideImageLoader(ImageView imageView, SimpleImageLoadingListener listener) {
        mImageView = imageView;
        mSimpleListener = listener;
    }

    public GlideImageLoader(ImageLoadingListener listener) {
        mListener = listener;
    }

    public void load(final String url, RequestOptions options) {
        if (url == null || options == null || mImageView == null) return;

        if (mSimpleListener != null) mSimpleListener.onLoadingStarted();

        Glide.with(ApplicationController.self())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (mSimpleListener != null) mSimpleListener.onLoadingFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (mSimpleListener != null) mSimpleListener.onLoadingComplete();
                        return false;
                    }
                }).apply(options).into(mImageView);
    }

    public void loadBitmap(final String url, RequestOptions options) {
        if (url == null || options == null) return;

        if (mListener != null) mListener.onLoadingStarted();

        Glide.with(ApplicationController.self().getApplicationContext())
                .asBitmap()
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (mListener != null) mListener.onLoadingFailed(url, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (mListener != null) mListener.onLoadingComplete(resource);
                        return false;
                    }
                }).apply(options).submit();
    }

    public void loadBitmap(final String url) {
        if (url == null) return;

        if (mListener != null) mListener.onLoadingStarted();

        Glide.with(ApplicationController.self().getApplicationContext())
                .asBitmap()
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (mListener != null) mListener.onLoadingFailed(url, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (mListener != null) mListener.onLoadingComplete(resource);
                        return false;
                    }
                }).submit();
    }

    public interface SimpleImageLoadingListener {
        void onLoadingStarted();

        void onLoadingFailed(GlideException e);

        void onLoadingComplete();
    }

    public interface ImageLoadingListener {
        void onLoadingStarted();

        void onLoadingFailed(String imageUri, GlideException e);

        void onLoadingComplete(Bitmap loadedImage);
    }
}
