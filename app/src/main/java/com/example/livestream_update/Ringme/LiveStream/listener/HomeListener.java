package com.example.livestream_update.Ringme.LiveStream.listener;




public interface HomeListener {
    void onClickNextButton(String type);

    void onClickNotifyMe(String id, long time, int positon, boolean notify);
}
