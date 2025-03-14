package com.example.livestream_update.Ringme.Common.utils.player;

import com.vtm.ringme.ApplicationController;

import java.util.HashMap;
import java.util.Map;

public class RingMePlayerUtil {

    private static RingMePlayerUtil ourInstance;

    public static RingMePlayerUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new RingMePlayerUtil();
        return ourInstance;
    }

    public static RingMePlayer getPlayer(String tag) {
        return getInstance().providePlayerBy(tag);
    }

    public static void removePlayer(String tag) {
        getInstance().removerPlayerBy(tag);
    }

    private final Map<String, RingMePlayer> playerMap;
    private final ApplicationController mApp;

    private RingMePlayerUtil() {
        mApp = ApplicationController.self();
        playerMap = new HashMap<>();
    }

    public void removerPlayerBy(String tag) {
        RingMePlayer player = playerMap.remove(tag);
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
        }
    }

    public RingMePlayer providePlayerBy(String tag) {
        RingMePlayer player = playerMap.get(tag);
        if (player == null && mApp != null) {
            player = new RingMePlayer(mApp);
            playerMap.put(tag, player);
        }
        return player;
    }
}
