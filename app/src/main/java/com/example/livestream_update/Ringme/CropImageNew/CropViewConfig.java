/*
 * Copyright (C) 2015 Lyft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.livestream_update.Ringme.CropImageNew;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.vtm.R;

class CropViewConfig {

    public static final float DEFAULT_VIEWPORT_RATIO = 0f;
    public static final float DEFAULT_MAXIMUM_SCALE = 10f;
    public static final float DEFAULT_MINIMUM_SCALE = 0f;
    public static final int DEFAULT_IMAGE_QUALITY = 100;
    public static final int DEFAULT_VIEWPORT_OVERLAY_PADDING = 0;
    public static final int DEFAULT_VIEWPORT_OVERLAY_COLOR = 0xC8000000; // Black with 200 alpha
    public static final boolean DEFAULT_IS_OVAL = false;
    public static final boolean DEFAULT_PHOTO_FULL_SCREEN = false;

    private float viewportRatio = DEFAULT_VIEWPORT_RATIO;
    private float maxScale = DEFAULT_MAXIMUM_SCALE;
    private float minScale = DEFAULT_MINIMUM_SCALE;
    private int viewportOverlayPadding = DEFAULT_VIEWPORT_OVERLAY_PADDING;
    private int viewportOverlayColor = DEFAULT_VIEWPORT_OVERLAY_COLOR;
    private boolean isOval = DEFAULT_IS_OVAL;
    private boolean photoFullScreen = DEFAULT_PHOTO_FULL_SCREEN;


    public int getViewportOverlayColor() {
        return viewportOverlayColor;
    }

    void setViewportOverlayColor(int viewportOverlayColor) {
        this.viewportOverlayColor = viewportOverlayColor;
    }

    public int getViewportOverlayPadding() {
        return viewportOverlayPadding;
    }

    void setViewportOverlayPadding(int viewportOverlayPadding) {
        this.viewportOverlayPadding = viewportOverlayPadding;
    }

    public float getViewportRatio() {
        return viewportRatio;
    }

    void setViewportRatio(float viewportRatio) {
        this.viewportRatio = viewportRatio <= 0 ? DEFAULT_VIEWPORT_RATIO : viewportRatio;
    }

    public float getMaxScale() {
        return maxScale;
    }

    void setMaxScale(float maxScale) {
        this.maxScale = maxScale <= 0 ? DEFAULT_MAXIMUM_SCALE : maxScale;
    }

    public float getMinScale() {
        return minScale;
    }

    void setMinScale(float minScale) {
        this.minScale = minScale <= 0 ? DEFAULT_MINIMUM_SCALE : minScale;
    }

    public boolean isOval() {
        return isOval;
    }

    public void setOval(boolean isOval) {
        this.isOval = isOval;
    }

    public void setPhotoFullScreen(boolean photoFullScreen) {
        this.photoFullScreen = photoFullScreen;
    }

    public boolean isPhotoFullScreen() {
        return photoFullScreen;
    }

    public static CropViewConfig from(Context context, AttributeSet attrs) {
        final CropViewConfig cropViewConfig = new CropViewConfig();

        if (attrs == null) {
            return cropViewConfig;
        }

        TypedArray attributes = context.obtainStyledAttributes(
                attrs,
                R.styleable.CropView);

        cropViewConfig.setViewportRatio(
                attributes.getFloat(R.styleable.CropView_cropviewViewportRatio,
                        CropViewConfig.DEFAULT_VIEWPORT_RATIO));

        cropViewConfig.setMaxScale(
                attributes.getFloat(R.styleable.CropView_cropviewMaxScale,
                        CropViewConfig.DEFAULT_MAXIMUM_SCALE));

        cropViewConfig.setMinScale(
                attributes.getFloat(R.styleable.CropView_cropviewMinScale,
                        CropViewConfig.DEFAULT_MINIMUM_SCALE));

        cropViewConfig.setViewportOverlayColor(
                attributes.getColor(R.styleable.CropView_cropviewViewportOverlayColor,
                        CropViewConfig.DEFAULT_VIEWPORT_OVERLAY_COLOR));

        cropViewConfig.setViewportOverlayPadding(
                attributes.getDimensionPixelSize(R.styleable.CropView_cropviewViewportOverlayPadding,
                        CropViewConfig.DEFAULT_VIEWPORT_OVERLAY_PADDING));

        cropViewConfig.setOval(
                attributes.getBoolean(R.styleable.CropView_cropviewIsOval,
                        CropViewConfig.DEFAULT_IS_OVAL));

        cropViewConfig.setPhotoFullScreen(
                attributes.getBoolean(R.styleable.CropView_photoFullScreen,
                        CropViewConfig.DEFAULT_PHOTO_FULL_SCREEN));

        attributes.recycle();

        return cropViewConfig;
    }
}
