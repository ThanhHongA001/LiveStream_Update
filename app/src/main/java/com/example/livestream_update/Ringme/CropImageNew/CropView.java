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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.vtm.ringme.utils.Log;

/**
 * An {@link ImageView} with a fixed viewport and cropping capabilities.
 */
public class CropView extends AppCompatImageView {

    public static final String IMAGE_PATH = "image-path";
    public static final String ORIENTATION_IN_DEGREES = "orientation_in_degrees";
    public static final String RETURN_DATA = "return-data";
    public static final String RETURN_DATA_AS_BITMAP = "data";
    public static final String ACTION_INLINE_DATA = "inline-data";
    public static final String OUTPUT_PATH = "image-output-path";
    public static final String MASK_OVAL = "MASK_OVAL";
    public static final String PHOTO_FULL_SCREEN = "PHOTO_FULL_SCREEN";

    private static final int MAX_TOUCH_POINTS = 2;
    private TouchManager touchManager;

    private Paint viewportPaint = new Paint();
    private Paint bitmapPaint = new Paint();
    private Paint cicleLine = new Paint();

    private Bitmap bitmap;
    private Matrix transform = new Matrix();

    private Extensions extensions;
    private boolean isOval = false;
    private Path ovalPath = new Path();
    private RectF ovalRect = new RectF();

//    private final Paint mOutlinePaint = new Paint();
//    private final Paint mFocusPaint = new Paint();

    private boolean isPhotoFullScreen;

    public CropView(Context context) {
        super(context);
        initCropView(context, null);
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initCropView(context, attrs);
    }

    void initCropView(Context context, AttributeSet attrs) {
        CropViewConfig config = CropViewConfig.from(context, attrs);

        touchManager = new TouchManager(MAX_TOUCH_POINTS, config);

        bitmapPaint.setFilterBitmap(true);
        viewportPaint.setColor(config.getViewportOverlayColor());
        isOval = config.isOval();
        isPhotoFullScreen = config.isPhotoFullScreen();
        Log.i("CropView", "isOval: " + isOval + " isPhotoFull: " + isPhotoFullScreen);
        viewportPaint.setAntiAlias(true);

        cicleLine.setAntiAlias(true);
        cicleLine.setStrokeWidth(3F);
        cicleLine.setStyle(Paint.Style.STROKE);
        cicleLine.setColor(0xFFf26520);
    }

    public void setIsOval(boolean isOval) {
        this.isOval = isOval;
    }

    public void setPhotoFullScreen(boolean isPhotoFullScreen) {
        this.isPhotoFullScreen = isPhotoFullScreen;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap == null) {
            return;
        }

        drawBitmap(canvas);
        if (isOval) {
            drawOvalOverlay(canvas);
        } else {
            drawSquareOverlay(canvas);
        }
    }

    private void drawBitmap(Canvas canvas) {
        transform.reset();
        touchManager.applyPositioningAndScale(transform);

        canvas.drawBitmap(bitmap, transform, bitmapPaint);
    }

    private void drawSquareOverlay(Canvas canvas) {
        final int viewportWidth = touchManager.getViewportWidth();
        final int viewportHeight = touchManager.getViewportHeight();
        final int left = (getWidth() - viewportWidth) / 2;
        final int top = (getHeight() - viewportHeight) / 2;

        canvas.drawRect(0, top, left, getHeight() - top, viewportPaint); // left
        canvas.drawRect(0, 0, getWidth(), top, viewportPaint); // top
        canvas.drawRect(getWidth() - left, top, getWidth(), getHeight() - top, viewportPaint); // right
        canvas.drawRect(0, getHeight() - top, getWidth(), getHeight(), viewportPaint); // bottom
    }

    private void drawOvalOverlay(Canvas canvas) {
        final int viewportWidth = touchManager.getViewportWidth();
        final int viewportHeight = touchManager.getViewportHeight();
        final int left = (getWidth() - viewportWidth) / 2;
        final int top = (getHeight() - viewportHeight) / 2;
        final int right = getWidth() - left;
        final int bottom = getHeight() - top;
        ovalRect.left = left;
        ovalRect.top = top;
        ovalRect.right = right;
        ovalRect.bottom = bottom;

        // top left
        ovalPath.reset();
        ovalPath.moveTo(left, getHeight() / 2); // middle of the left side of the circle
        ovalPath.arcTo(ovalRect, 180, 90, false); // draw arc to top
        ovalPath.lineTo(left, top); // move to left corner
        ovalPath.lineTo(left, getHeight() / 2); // move back to origin
        ovalPath.close();
        canvas.drawPath(ovalPath, viewportPaint);

        // top right
        ovalPath.reset();
        ovalPath.moveTo(getWidth() / 2, top);
        ovalPath.arcTo(ovalRect, 270, 90, false);
        ovalPath.lineTo(right, top);
        ovalPath.lineTo(getWidth() / 2, top);
        ovalPath.close();
        canvas.drawPath(ovalPath, viewportPaint);

        // bottom right
        ovalPath.reset();
        ovalPath.moveTo(right, getHeight() / 2);
        ovalPath.arcTo(ovalRect, 0, 90, false);
        ovalPath.lineTo(right, bottom);
        ovalPath.lineTo(right, getHeight() / 2);
        ovalPath.close();
        canvas.drawPath(ovalPath, viewportPaint);

        // bottom left
        ovalPath.reset();
        ovalPath.moveTo(getWidth() / 2, bottom);
        ovalPath.arcTo(ovalRect, 90, 90, false);
        ovalPath.lineTo(left, bottom);
        ovalPath.lineTo(getWidth() / 2, bottom);
        ovalPath.close();
        canvas.drawPath(ovalPath, viewportPaint);

        canvas.drawRect(0, top, left, getHeight() - top, viewportPaint); // left
        canvas.drawRect(0, 0, getWidth(), top, viewportPaint); // top
        canvas.drawRect(getWidth() - left, top, getWidth(), getHeight() - top, viewportPaint); // right
        canvas.drawRect(0, getHeight() - top, getWidth(), getHeight(), viewportPaint); // bottom

        canvas.drawCircle(getWidth()/2, getHeight()/2, viewportWidth/2, cicleLine);


        /*int viewportWidth = touchManager.getViewportWidth();
        int viewportHeight = touchManager.getViewportHeight();
        final int left = (getWidth() - viewportWidth) / 2;
        final int top = (getHeight() - viewportHeight) / 2;

        Path path = new Path();
        Rect viewDrawingRect = new Rect();
        this.getDrawingRect(viewDrawingRect);
        canvas.save();
        path.addCircle(left + (viewportWidth / 2),
                top + (viewportHeight / 2),
                viewportWidth / 2,
                Path.Direction.CW);
        mOutlinePaint.setColor(0xFFf26520);

        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.drawRect(viewDrawingRect, mFocusPaint);

        canvas.restore();
        canvas.drawPath(path, mOutlinePaint);*/
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetTouchManager();
    }

    /**
     * Returns the native aspect ratio of the image.
     *
     * @return The native aspect ratio of the image.
     */
    public float getImageRatio() {
        Bitmap bitmap = getImageBitmap();
        return bitmap != null ? (float) bitmap.getWidth() / (float) bitmap.getHeight() : 0f;
    }

    /**
     * Returns the aspect ratio of the viewport and crop rect.
     *
     * @return The current viewport aspect ratio.
     */
    public float getViewportRatio() {
        return touchManager.getAspectRatio();
    }

    /**
     * Sets the aspect ratio of the viewport and crop rect.  Defaults to
     * the native aspect ratio if <code>ratio == 0</code>.
     *
     * @param ratio The new aspect ratio of the viewport.
     */
    public void setViewportRatio(float ratio) {
        if (Float.compare(ratio, 0) == 0) {
            ratio = getImageRatio();
        }
        touchManager.setAspectRatio(ratio);
        resetTouchManager();
        invalidate();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        final Bitmap bitmap = resId > 0
                ? BitmapFactory.decodeResource(getResources(), resId)
                : null;
        setImageBitmap(bitmap);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        final Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        } else if (drawable != null) {
            bitmap = Utils.asBitmap(drawable, getWidth(), getHeight());
        } else {
            bitmap = null;
        }

        setImageBitmap(bitmap);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        extensions().load(uri);
    }

    @Override
    public void setImageBitmap(@Nullable Bitmap bitmap) {
        this.bitmap = bitmap;
        resetTouchManager();
        invalidate();
    }

    /**
     * @return Current working Bitmap or <code>null</code> if none has been set yet.
     */
    @Nullable
    public Bitmap getImageBitmap() {
        return bitmap;
    }

    private void resetTouchManager() {
        final boolean invalidBitmap = bitmap == null;
        final int bitmapWidth = invalidBitmap ? 0 : bitmap.getWidth();
        final int bitmapHeight = invalidBitmap ? 0 : bitmap.getHeight();
        touchManager.resetFor(bitmapWidth, bitmapHeight, getWidth(), getHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);

        touchManager.onEvent(event);
        invalidate();
        return true;
    }

    /**
     * Performs synchronous image cropping based on configuration.
     *
     * @return A {@link Bitmap} cropped based on viewport and user panning and zooming or <code>null</code> if no {@link Bitmap} has been
     * provided.
     */
    @Nullable
    public Bitmap crop() {
        if (bitmap == null) {
            return null;
        }

        final Bitmap src = bitmap;
        final Bitmap.Config srcConfig = src.getConfig();
        final Bitmap.Config config = srcConfig == null ? Bitmap.Config.ARGB_8888 : srcConfig;
        final int viewportHeight = touchManager.getViewportHeight();
        final int viewportWidth = touchManager.getViewportWidth();

        final Bitmap dst = Bitmap.createBitmap(viewportWidth, viewportHeight, config);

        Canvas canvas = new Canvas(dst);
        final int left = (getRight() - viewportWidth) / 2;
        final int top = (getBottom() - viewportHeight) / 2;
        canvas.translate(-left, -top);

        drawBitmap(canvas);

        return dst;
    }

    /**
     * Obtain current viewport width.
     *
     * @return Current viewport width.
     * <p>Note: It might be 0 if layout pass has not been completed.</p>
     */
    public int getViewportWidth() {
        return touchManager.getViewportWidth();
    }

    /**
     * Obtain current viewport height.
     *
     * @return Current viewport height.
     * <p>Note: It might be 0 if layout pass has not been completed.</p>
     */
    public int getViewportHeight() {
        return touchManager.getViewportHeight();
    }

    /**
     * Offers common utility extensions.
     *
     * @return Extensions object used to perform chained calls.
     */
    public Extensions extensions() {
        if (extensions == null) {
            extensions = new Extensions(this);
        }
        return extensions;
    }

    /**
     * Optional extensions to perform common actions involving a {@link CropView}
     */
    public static class Extensions {

        private final CropView cropView;

        Extensions(CropView cropView) {
            this.cropView = cropView;
        }

        /**
         * Load a {@link Bitmap} using an automatically resolved {@link BitmapLoader} which will attempt to scale image to fill view.
         *
         * @param model Model used by {@link BitmapLoader} to load desired {@link Bitmap}
         */
        public void load(@Nullable Object model) {
            new CropViewExtensions.LoadRequest(cropView)
                    .load(model);
        }

        /**
         * @param bitmapLoader {@link BitmapLoader} used to load desired {@link Bitmap}
         */
        public CropViewExtensions.LoadRequest using(@Nullable BitmapLoader bitmapLoader) {
            return new CropViewExtensions.LoadRequest(cropView).using(bitmapLoader);
        }

        /**
         * Perform an asynchronous crop request.
         * <p/>
         * <ul>
         * </ul>
         */
        public CropViewExtensions.CropRequest crop() {
            return new CropViewExtensions.CropRequest(cropView);
        }

        /**
         * Perform a pick image request using {@link Activity#startActivityForResult(Intent, int)}.
         */
        public void pickUsing(@NonNull Activity activity, int requestCode) {
            CropViewExtensions.pickUsing(activity, requestCode);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isPhotoFullScreen) {
            int width = getMeasuredWidth();
            setMeasuredDimension(width, width);
        }
    }
}
