package com.example.livestream_update.Ringme.Helper;

import com.vtm.ringme.ApplicationController;

public class EncodeHelper {
    public static String getSecurity(String stringToEncode, long timestamp){
        return HttpHelper
                .encryptDataV2(ApplicationController
                        .self(),ApplicationController
                        .self().getJidNumber()
                        + stringToEncode
                        + ApplicationController.self().getToken()
                        + timestamp, ApplicationController.self().getToken());
    }
}

