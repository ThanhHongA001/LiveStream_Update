package com.example.livestream_update.Ringme.LiveStream.widget;

import android.os.CountDownTimer;

public abstract class CountUpTimer extends CountDownTimer {
    private final long duration;

    public CountUpTimer(long duration, long interval) {
        super(duration, interval);
        this.duration = duration;
    }

    public abstract void onTick(int second);

    @Override
    public void onTick(long msUntilFinished) {
        int second = (int) ((duration - msUntilFinished));
        onTick(second);
    }

    @Override
    public void onFinish() {
        onTick(duration);
        cancel();
    }
}
