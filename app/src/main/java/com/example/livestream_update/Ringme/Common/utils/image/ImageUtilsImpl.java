package com.example.livestream_update.Ringme.Common.utils.image;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.imageview.CircleImageView;
import com.vtm.ringme.utils.Utilities;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by tuanha00 on 3/26/2018.
 */

@Deprecated
public class ImageUtilsImpl implements ImageUtils {

    private final RequestManager glide;
    private final ApplicationController application;

    public ImageUtilsImpl(ApplicationController application) {
        this.application = application;
        glide = Glide.with(application);
    }

    @Override
    public void load(String urlImage, int thumbnail, ImageView ivChannel) {
        glide.asBitmap()
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .placeholder(thumbnail)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(urlImage)
                .into(ivChannel);
    }

    @Override
    public void load(String imageUrl, ImageView photoView) {
        glide.asBitmap()
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(imageUrl)
                .into(photoView);
    }

    @Override
    public void loadCircle(String urlImage, int thumbnail, ImageView imageView) {
        glide.asBitmap()
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .placeholder(thumbnail)
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(Utilities.dpToPx(30), 0))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(urlImage)
                .into(imageView);
    }

    @Override
    public void loadNotCrossFade(String urlImage, int thumbnail, CircleImageView ivChannel) {
        glide.asBitmap()
                .apply(new RequestOptions()
                        .placeholder(thumbnail)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(urlImage)
                .into(ivChannel);
    }

    @Override
    public ImageLoader loadCircle(String url, int thumb, int size) {
        RequestBuilder<Bitmap> requestBuilder = glide.asBitmap()
//                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .placeholder(thumb)
                        .transform(new CircleTransform(application))
                        .override(size)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(url);
        return new ImageLoaderObject(requestBuilder);
    }

    @Override
    public ImageLoader load(String url, int thumb, int width, int height) {
        RequestBuilder<Bitmap> requestBuilder = glide.asBitmap()
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .placeholder(thumb)
                        .override(width, height)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(url);
        return new ImageLoaderObject(requestBuilder);
    }
}
