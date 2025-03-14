package com.example.livestream_update.Ringme.Helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class EmoBitmapDrawable extends BitmapDrawable {
    String s;

    public EmoBitmapDrawable(Resources res, Bitmap bitmap, String s) {
        super(res, bitmap);
        this.s = s;
    }

    public String toString() {
        return s;
    }

}