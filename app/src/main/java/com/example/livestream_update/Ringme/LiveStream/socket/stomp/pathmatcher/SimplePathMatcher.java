package com.example.livestream_update.Ringme.LiveStream.socket.stomp.pathmatcher;

import com.vtm.ringme.livestream.socket.stomp.dto.StompHeader;
import com.vtm.ringme.livestream.socket.stomp.dto.StompMessage;

public class SimplePathMatcher implements PathMatcher {

    @Override
    public boolean matches(String path, StompMessage msg) {
        String dest = msg.findHeader(StompHeader.DESTINATION);
        if (dest == null) return false;
        else return path.equals(dest);
    }
}
