package com.example.livestream_update.Ringme.Listener;

import android.view.View;

public interface RecyclerClickListener {
    void onClick(View v, int pos, Object object);

    void onLongClick(View v, int pos,Object object);
}