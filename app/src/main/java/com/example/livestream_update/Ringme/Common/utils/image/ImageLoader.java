package com.example.livestream_update.Ringme.Common.utils.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;

public interface ImageLoader {
    void into(ImageView imageView);

    boolean isCompleted();

    int getThumb();

    RequestBuilder<Bitmap> getRequestBuilder();
}
