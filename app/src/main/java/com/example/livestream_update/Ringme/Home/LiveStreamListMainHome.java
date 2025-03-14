package com.example.livestream_update.Ringme.Home;

import com.vtm.ringme.model.livestream.LivestreamModel;

import java.util.List;

public class LiveStreamListMainHome {
    private String name;
    private List<LivestreamModel> liveStreamTestList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LivestreamModel> getLiveStreamTestList() {
        return liveStreamTestList;
    }

    public void setLiveStreamTestList(List<LivestreamModel> liveStreamTestList) {
        this.liveStreamTestList = liveStreamTestList;
    }

    public LiveStreamListMainHome(String name, List<LivestreamModel> liveStreamTestList) {
        this.name = name;
        this.liveStreamTestList = liveStreamTestList;
    }
}
