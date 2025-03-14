package com.example.livestream_update.Ringme.Common.utils.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vtm.R;

public class ImageLoaderObject implements ImageLoader, RequestListener<Bitmap> {

    private final RequestBuilder<Bitmap> glide;
    private final int thumb;
    private boolean isCompleted;

    ImageLoaderObject(@NonNull RequestBuilder<Bitmap> request) {
        this.thumb = R.drawable.rm_error;
        this.isCompleted = false;
        this.glide = request.listener(this);
    }

    ImageLoaderObject(int thumb, @NonNull RequestBuilder<Bitmap> request) {
        this.thumb = thumb;
        this.isCompleted = false;
        this.glide = request.listener(this);
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        isCompleted = true;
        return false;
    }

    @Override
    public void into(ImageView imageView) {
        if (glide != null)
            glide.into(imageView);
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public int getThumb() {
        return thumb;
    }

    @Override
    public RequestBuilder<Bitmap> getRequestBuilder() {
        return glide;
    }

}
