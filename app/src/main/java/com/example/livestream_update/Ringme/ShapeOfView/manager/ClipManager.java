package com.example.livestream_update.Ringme.ShapeOfView.manager;

import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ClipManager {

    @NonNull
    Path createMask(int width, int height);

    @Nullable
    Path getShadowConvexPath();

    void setupClipLayout(int width, int height);

    Paint getPaint();

    boolean requiresBitmap();
}
