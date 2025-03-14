package com.example.livestream_update.Ringme.Common.utils.image;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.imageview.CircleImageView;

import java.io.File;
import java.util.Random;

public class ImageManager {
    private static final String TAG = "ImageManager";

    public static final int[] thumbs = {R.drawable.rm_error
            , R.drawable.rm_error_2
            , R.drawable.rm_error_3
            , R.drawable.rm_error_4
            , R.drawable.rm_error_5
            , R.drawable.rm_error_6
            , R.drawable.rm_error_7
            , R.drawable.rm_error_8
            , R.drawable.rm_error_9
            , R.drawable.rm_error_10
            , R.drawable.rm_error_11};

    @Deprecated
    public static Builder with() {
        return new Builder();
    }

    @Deprecated
    public static void showImage(File file, ImageView imageView, Transformation<Bitmap>... transformations) {
        Glide.with(imageView)
                .asBitmap()
                .load(file)
                .transition(withCrossFade(500))
                .apply(new RequestOptions().transforms(transformations))
                .into(imageView);
    }

    @Deprecated
    public static void showImage(String url, ImageView ivChannel) {
        ImageManager
                .with()
                .setUrl(url)
                .setWithCrossFade(true)
                .setTransformations(new CenterCrop())
                .build()
                .into(ivChannel);
    }

    @Deprecated
    public static void showImageGameIQ(String url, ImageView ivBg) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.rm_bg_gameiq_default))
                .into(ivBg);
    }

    @Deprecated
    public static void showImageRounded(String url, ImageView ivChannel) {
        ImageManager
                .with()
                .setUrl(url)
                .setWithCrossFade(true)
                .setTransformations(new CenterCrop(), ApplicationController.self().getRoundedCornersTransformation())
                .build()
                .into(ivChannel);
    }

    @Deprecated
    public static void showImageNotCenterCrop(String url, ImageView imageView) {
        ImageManager
                .with()
                .setUrl(url)
                .setWithCrossFade(true)
                .build()
                .into(imageView);
    }

    @Deprecated
    public static void showImageWithThumb(String url, String thumb, ImageView imageView) {
        ImageLoader imageLoader = showImageWithThumb(url, thumb);
        if (imageLoader != null)
            imageLoader.into(imageView);
    }

    @Deprecated
    public static ImageLoader showImageWithThumb(String url, String thumb, int thumbError) {
        String imageThump;
        if (TextUtils.isEmpty(thumb))
            imageThump = url;
        else
            imageThump = thumb;

        return ImageManager
                .with()
                .setUrl(url)
                .setWithCrossFade(true)
                .setThumbError(thumbError)
                .setImageThump(imageThump)
                .setTransformations(new CenterCrop())
                .build()
                .provideImageLoader();
    }

    @Deprecated
    public static ImageLoader showImageWithThumb(String url, String thumb) {
        String imageThump;
        if (TextUtils.isEmpty(thumb))
            imageThump = url;
        else
            imageThump = thumb;

        return ImageManager
                .with()
                .setUrl(url)
                .setImageThump(imageThump)
                .setTransformations(new CenterCrop())
                .setWithCrossFade(true)
                .build()
                .provideImageLoader();
    }

    @Deprecated
    public static void showImage(String url, CircleImageView ivChannel) {
        ImageManager
                .with()
                .setUrl(url)
                .setTransformations(new CenterCrop())
                .build()
                .into(ivChannel);
    }

    public static void showImageLocal(String url, ImageView imageView) {
        if (ApplicationController.self() == null) return;
        Glide.with(ApplicationController.self())
                .load(new File(url))
                .into(imageView);
    }

    public static void showImageV2(String url, ImageView imageView) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().transforms(new CenterCrop()))
                .into(imageView);
    }

    public static void showImageNormalV3(String url, ImageView imageView) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(500))
                .into(imageView);
    }

    public static void showImageNormalV2(String url, ImageView imageView) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(500))
                .apply(new RequestOptions().transforms(new CenterCrop()))
                .into(imageView);
    }

    public static void showImageNormalV2(String url, ImageView imageView, Transformation<Bitmap>... transformations) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(500))
                .apply(new RequestOptions().transforms(transformations))
                .into(imageView);
    }

    public static void showImageNormalV2(String url, String thumb, ImageView imageView) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(500))
                .thumbnail(Glide.with(ApplicationController.self()).asBitmap().load(thumb).apply(new RequestOptions()
                        .transforms(new CenterCrop())))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transforms(new CenterCrop()))
                .into(imageView);
    }

    public static void showImageCircleV2(String url, ImageView imageView) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(500))
                .apply(new RequestOptions().transforms(new CenterCrop(), new CircleCrop()))
                .into(imageView);
    }

    public static void showImageRoundV2(String url, ImageView imageView) {
        Glide.with(ApplicationController.self())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(500))
                .apply(new RequestOptions().placeholder(R.color.df_placeholder)
                        .error(R.color.df_placeholder).transforms(new CenterCrop(), ApplicationController.self().getRoundedCornersTransformation()))
                .into(imageView);
    }

    public static void clear(ImageView imageView) {
        Glide.with(ApplicationController.self()).clear(imageView);
    }

    public static class Builder {

        private Transformation<Bitmap>[] transformations = null;
        private boolean withCrossFade = false;
        private int thumbError = 0;
        private int width = 0;
        private int height = 0;
        private String url = "";
        private String imageThump = "";

        public Builder() {
        }

        public Builder setTransformations(Transformation<Bitmap>... transformations) {
            this.transformations = transformations;
            return this;
        }

        public Builder setWithCrossFade(boolean withCrossFade) {
            this.withCrossFade = withCrossFade;
            return this;
        }

        public Builder setThumbError(int thumbError) {
            this.thumbError = thumbError;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setImageThump(String imageThump) {
            this.imageThump = imageThump;
            return this;
        }

        public ImageManager build() {
            return new ImageManager(this);
        }
    }

    private static class Singleton {

        private final RequestManager glide;
        private final Random random = new Random();

        private static Singleton ourInstance;

        public static Singleton getInstance() {
            if (ourInstance == null)
                ourInstance = new Singleton();
            return ourInstance;
        }

        Singleton() {
            glide = Glide.with(ApplicationController.self());
        }
    }

    private final Transformation<Bitmap>[] transformations;

    private final boolean withCrossFade;
    private int thumbError;
    private final int width;
    private final int height;

    private final String url;

    private final String imageThump;

    private ImageManager(Builder builder) {

        transformations = builder.transformations;

        withCrossFade = builder.withCrossFade;
        thumbError = builder.thumbError;
        width = builder.width;
        height = builder.height;

        url = builder.url;
        imageThump = builder.imageThump;
    }

    public void into(ImageView imageView) {
        ImageLoader imageLoader = provideImageLoader();
        if (imageLoader != null)
            imageLoader.into(imageView);
    }

    public ImageLoader provideImageLoader() {
        RequestOptions mRequestOptions = new RequestOptions();
        RequestBuilder<Bitmap> requestBuilder;

        RequestManager glide = Singleton.getInstance().glide;

        if (glide == null)
            return null;
        else {
            if (withCrossFade)
                requestBuilder = glide.asBitmap().transition(withCrossFade());
            else
                requestBuilder = glide.asBitmap();

            mRequestOptions = mRequestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            if (height != 0 && width != 0)
                mRequestOptions = mRequestOptions.override(width, height);
            if (thumbError == 0 || !isExits(thumbError))
                thumbError = provideThumbError();
            if (thumbError != 0)
                mRequestOptions = mRequestOptions.error(thumbError);
            if (transformations != null)
                mRequestOptions = mRequestOptions.transforms(transformations);
            if (!TextUtils.isEmpty(imageThump))
                requestBuilder = requestBuilder.thumbnail(glide.asBitmap().load(imageThump));

            requestBuilder = requestBuilder.apply(mRequestOptions).load(url);

            return new ImageLoaderObject(thumbError, requestBuilder);
        }
    }

    public int provideThumbError() {
        Random random = Singleton.getInstance().random;
        if (random == null)
            return provideThumbError(Math.min(thumbs.length - 1, 0));
        else
            return provideThumbError(random.nextInt(thumbs.length - 1));
    }

    public int provideThumbError(int position) {
        if (position < 0 || position > thumbs.length - 1)
            return thumbs[0];
        return thumbs[position];
    }

    private boolean isExits(int resId) {
        if (ApplicationController.self() == null)
            return false;
        try {
            ApplicationController.self().getResources().getResourceName(resId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
