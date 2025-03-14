package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.Coin;

public class LiveStreamCoinResponse extends BaseResponse {
    @SerializedName("data")
    private Coin currentCoin;

    public Coin getCurrentCoin() {
        return currentCoin;
    }

    public void setCurrentCoin(Coin currentCoin) {
        this.currentCoin = currentCoin;
    }
}
