package com.example.livestream_update.Ringme.Common.utils.image;

import android.widget.ImageView;

import com.vtm.ringme.imageview.CircleImageView;


/**
 * Created by tuanha00 on 3/26/2018.
 */

@Deprecated
public interface ImageUtils {
    void load(String urlImage, int thumbnail, ImageView imageView);

    void load(String imageUrl, ImageView photoView);

    void loadCircle(String urlImage, int thumbnail, ImageView imageView);

    void loadNotCrossFade(String urlImage, int thumbnail, CircleImageView ivChannel);

    ImageLoader loadCircle(String url, int thumb, int size);

    ImageLoader load(String imageUrl, int thumbnailResId, int imageSize, int imageSize1);

}
