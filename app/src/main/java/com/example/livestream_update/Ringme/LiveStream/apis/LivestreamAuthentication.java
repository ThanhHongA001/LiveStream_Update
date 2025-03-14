package com.example.livestream_update.Ringme.LiveStream.apis;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.HttpHelper;
import com.vtm.ringme.values.Constants;

import java.util.HashMap;
import java.util.Map;

public class LivestreamAuthentication {
    //todo dataEncrypt == security
    //todo authenApikey == secapi

    static String token = ApplicationController.self().getToken();

    public static Map<String, String> getLivestreamAuth(String data) {
        String security = HttpHelper.encryptDataV2(ApplicationController.self(), data, token);
        Map<String, String> map = new HashMap<>();
        map.put(Constants.Authentication.authenticationKey, ApplicationController.self().getKakoakApi());
        map.put(Constants.Authentication.security, security);
        return map;
    }

}
