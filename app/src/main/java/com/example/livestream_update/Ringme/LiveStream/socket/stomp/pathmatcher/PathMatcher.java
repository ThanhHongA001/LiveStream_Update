package com.example.livestream_update.Ringme.LiveStream.socket.stomp.pathmatcher;

import com.vtm.ringme.livestream.socket.stomp.dto.StompMessage;

public interface PathMatcher {

    boolean matches(String path, StompMessage msg);
}
