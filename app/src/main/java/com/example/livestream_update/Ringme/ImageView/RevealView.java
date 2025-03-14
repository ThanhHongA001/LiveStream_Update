package com.example.livestream_update.Ringme.ImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by thanhnt72 on 9/29/2017.
 */
public class RevealView extends AppCompatImageView {

    private Bitmap secondBitmap;
    private float mAnimationPercentage;
    private Path mPath;
    private Paint mPaint;

    public RevealView(Context context) {
        super(context);
        init();
    }

    public RevealView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPercentage(0);

        mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        mPath = new Path();
    }

    public void setPercentage(int p) {
        if (p > 100) {
            p = 100;
        }
        mAnimationPercentage = p / 100f;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getDrawable() != null) {
            Bitmap mRegularBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            if (mRegularBitmap != null && secondBitmap != null) {
                //First draw your regular image
                canvas.drawBitmap(mRegularBitmap, 0, 0, null);

                //Then clip the canvas depending on the animation percentage, using a Path
                mPath.reset();
                mPath.moveTo((float) canvas.getWidth() * mAnimationPercentage, 0.0f);
                mPath.lineTo((float) canvas.getWidth() * mAnimationPercentage, canvas.getHeight());
                mPath.lineTo(canvas.getWidth(), canvas.getHeight());
                mPath.lineTo(canvas.getWidth(), 0.0f);
                mPath.close();

                canvas.drawPath(mPath, mPaint);
                canvas.clipPath(mPath);

                //Then draw the gray bitmap on top
                canvas.drawBitmap(secondBitmap, 0.0f, 0.0f, null);
            }
        }
    }

    public void setSecondBitmap(Bitmap secondBitmap, int width, int height) {
        this.secondBitmap = Bitmap.createScaledBitmap(secondBitmap, width, height, false);
    }

    public void setSecondBitmap(Bitmap secondBitmap) {
        this.secondBitmap = secondBitmap;
    }
}