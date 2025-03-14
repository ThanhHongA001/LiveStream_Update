package com.example.livestream_update.Ringme.Helper;

public class ConfigLocalized {
    //todo domain v2 kakoak
    private static final String DOMAIN_API = "https://freeapikakoak.tls.tl"; //todo các api của app dùng domain này
    public static final String DOMAIN_API_DEV = "https://mytelapi-live.ringme.vn";
    public static final String DOMAIN_VIDEO = "https://videoapikakoak.tls.tl"; //todo các api video dùng domain này
    private static final boolean isDev = true; //todo true là Server Dev, false là Server That
    public static final String DOMAIN_KAKOAK_VIDEO = isDev ? DOMAIN_API_DEV : DOMAIN_VIDEO;
    public static final String DOMAIN_LIVESTREAM = "https://mytelapi-live.ringme.vn";

    public static final String DOMAIN_FILE_V1_TEST = DOMAIN_API;   //test
    public static final String DOMAIN_IMAGE_V1_TEST = DOMAIN_API;
    public static final String DOMAIN_GEN_OTP = DOMAIN_API;
    public static final String DOMAIN_FILE_V1 = DOMAIN_API;
    public static final String DOMAIN_IMAGE_V1 = DOMAIN_API;

    public static final String DOMAIN_MC_VIDEO = "http://pvvip.mocha.com.vn:8080";


    public static final String DOMAIN_GEN_OTP_TEST = DOMAIN_API;   //test
   //test
    public static final String DOMAIN_MC_VIDEO_TEST = "http://pvvip.mocha.com.vn:8088";   //test
         // free 15days
    public static final String DOMAIN_LOG_DEVICE = "pvvip.mocha.com.vn:8080";

}