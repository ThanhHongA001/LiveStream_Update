package com.example.livestream_update.Ringme.Base.utils;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

public class ImageBusiness {
    /*
    TODO thông tin chi tiết ảnh các màn hình
    1	Flash hot	-	640x360
    2	MV trang chủ	-	390x220
    3	MV trang danh sách	-	390x220
    4	MV trang chi tiết	-	640x360
    5	Album trang chủ	-	310x310
    6	Album trang danh sách	-	310x310
    7	Top hit trang chủ	-	640x360
    8	Top hit trang danh sách	-	640x360
    9	Top hit trang chi tiết	-	640x360
    10	Bài hát trang chủ	-	103x103
    11	Bài hát trang player	-	310x310
    12	Chủ đề trang chủ	-	390x220
    13	Chủ đề trang danh sách	-	640x360
    14	Chủ đề trang chi tiết	-	640x360
    15	Ca sĩ trang chủ	-	310x310
    16	Ca sĩ trang chi tiết (ảnh ngang)	-	640x360

    image310: ảnh lớn
    image: ảnh nhỎ

    Song:
    image310 <=> image 310
    image <=> image 103
    list_image [
        //ảnh 310 ca sĩ
    ]

    MV:
    image310 <=> image 640x360
    image <=> image 390x220

    Album:
    image310 <=> image gốc
    image <=> image 300x300

    Flash_hot:
    image310 <=> image 640x360
    image <=> image 640x360

    Tophit:
    cover <=> image 640x360

    Topic:
    cover <=> image 640x360

    Category:
    cover <=> image 640x360

    Singer:
    avatar <=> image 310
    cover <=> image 640x360
    **/

    protected static final String TAG = "ImageBusiness";

    public static void setLogo(ImageView image, String url) {
        if (image == null) return;
        if (TextUtils.isEmpty(url)) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            setImage(image, url, 0, 0, null);
        }
    }

    public static void setVideoPlayer(ImageView image, String url) {
        if (image == null) return;
        if (TextUtils.isEmpty(url)) {
            image.setImageResource(R.drawable.rm_df_video_player);
        } else {
            setCoverItem(image, url, R.drawable.rm_df_video_player, R.drawable.rm_df_video_player);
        }
    }

    public static void setImage(ImageView image, String url, int placeholder, int error, Transformation transformation) {
        if (image == null)
            return;
        try {
            Context context = image.getContext();
            int width = image.getWidth();
            int height = image.getHeight();
            if (transformation == null) {
                if (width > 0 && height > 0) {
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(placeholder)
                            .error(error)
                            .override(width, height)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
                            .transition(DrawableTransitionOptions.withCrossFade(500))
                            .into(image);
                } else {
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(placeholder)
                            .error(error)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
                            .transition(DrawableTransitionOptions.withCrossFade(500))
                            .into(image);
                }
            } else {
                if (width > 0 && height > 0) {
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(placeholder)
                            .override(width, height)
                            .error(error)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop()
                            .dontAnimate()
                            .transform(transformation);
                    DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions();
                    transitionOptions.crossFade(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)).withCrossFade(500);
                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
//                            .transition(transitionOptions)
                            .into(image);
                } else {
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(placeholder)
                            .error(error)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop()
                            .dontAnimate()
                            .transform(transformation);
                    DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions();
                    transitionOptions.crossFade(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)).withCrossFade(500);
                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
                            .transition(transitionOptions)
                            .into(image);
                }
            }
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @SuppressLint("CheckResult")
    private static void setCoverItem(ImageView image, String url, int placeholder, int error) {
        if (image == null) return;
        if (TextUtils.isEmpty(url)) {
            if (placeholder > 0) setResource(image, placeholder);
            else if (error > 0) setResource(image, error);
        } else {
            RequestOptions requestOptions = new RequestOptions();
            if (placeholder > 0) requestOptions.placeholder(placeholder);
            if (error > 0) requestOptions.error(error);
            try {
                int width = image.getWidth();
                int height = image.getHeight();
                //todo tam thoi resize theo placeholder de trang cover co resolution > 720
                if (placeholder == R.drawable.rm_df_image_home_21_9) {
                    requestOptions.override(640, 274);
                } else if (placeholder == R.drawable.rm_df_image_home_16_9) {
                    requestOptions.override(640, 360);
                } else if (placeholder == R.drawable.rm_df_image_home) {
                    requestOptions.override(300, 300);
                } else if (width > 0 && height > 0) {
                    requestOptions.override(width, height);
                }
                requestOptions.priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .dontAnimate()
                        .dontTransform()
                        .centerCrop()
                        .clone();
                Glide.with(image.getContext())
                        .asBitmap()
                        .load(url)
                        .apply(requestOptions)
                        .into(image);
            } catch (OutOfMemoryError e) {
                Log.e(TAG, e);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
    }

    public static void setAvatarChannel(final ImageView imageView, String url) {
        if (imageView == null)
            return;
        if (TextUtils.isEmpty(url)) {
            setResource(imageView, R.drawable.rm_df_channel_avatar);
        } else {
            try {
                Context context = imageView.getContext();
                int width = imageView.getWidth();
                int height = imageView.getHeight();
                if (width > 0 && height > 0) {
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.rm_df_channel_avatar)
                            .error(R.drawable.rm_df_channel_avatar)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override(width, height)
                            .dontAnimate()
                            .dontTransform()
                            .fitCenter()
                            .clone();
                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
                            .into(imageView);
                } else {
                    int size = Utilities.dpToPx(context, 120);
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.drawable.rm_df_channel_avatar)
                            .error(R.drawable.rm_df_channel_avatar)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override(size, size)
                            .dontAnimate()
                            .dontTransform()
                            .fitCenter()
                            .clone();
                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
                            .into(imageView);
                }
            } catch (OutOfMemoryError e) {
                Log.e(TAG, e);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
    }



    @SuppressLint("CheckResult")
    public static void setResource(ImageView image, int resource) {
        if (image == null) return;
        try {
            int width = image.getWidth();
            int height = image.getHeight();
            Context context = image.getContext();
            RequestOptions requestOptions = new RequestOptions()
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            if (width > 0 && height > 0) {
                requestOptions.override(width, height);
            }
            Glide.with(context)
                    .load(resource)
                    .apply(requestOptions.clone())
                    .into(image);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }


    public static void setVideoEpisode(ImageView imageView, String url) {
        //setImageTransform(imageView, url, 0, R.drawable.df_image_home_16_9);

        if (imageView == null || url == null) return;
        try {
            Glide.with(ApplicationController.self())
                    .asBitmap()
                    .load(url)
                    .transition(withCrossFade(500))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .transforms(new CenterCrop()
                                    , ApplicationController.self().getRoundedCornersTransformation())
                            .error(R.drawable.rm_df_image_home_16_9)
                    )
                    .into(imageView);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }
}