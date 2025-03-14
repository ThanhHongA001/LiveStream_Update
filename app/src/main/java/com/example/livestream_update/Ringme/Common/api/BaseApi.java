package com.example.livestream_update.Ringme.Common.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.common.api.http.Http;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.utils.Utilities;

public class BaseApi {
    public static final String RINGME_API = "sec-api";
    public static final String UUID = "uuid";

    protected final ApplicationController application;
    protected final Gson gson;

    public BaseApi(ApplicationController app) {
        application = app;
        gson = app.getGson();
    }

    protected Http.Builder configBuilder(@NonNull Http.Builder builder) {
        builder.putHeader(RINGME_API, getReengAccountBusiness().getKakoakApi());
        builder.putHeader("languageCode", getReengAccountBusiness().getCurrentLanguage());
        builder.putHeader("countryCode", getReengAccountBusiness().getRegionCode());
        builder.putHeader(UUID, Utilities.getUuidApp());
        return builder;
    }

    protected Http.Builder get(String url) {
        Http.Builder builder = Http.get(url);
        builder.setContext(application);
        return configBuilder(builder);
    }

    protected Http.Builder post(String url) {
        Http.Builder builder = Http.post(url);
        builder.setContext(application);
        return configBuilder(builder);
    }


    protected Http.Builder get(String baseUrl, String url) {
        Http.Builder builder = Http.get(application, baseUrl, url);
        return configBuilder(builder);
    }

    protected Http.Builder post(String baseUrl, String url) {
        Http.Builder builder = Http.post(application, baseUrl, url);
        return configBuilder(builder);
    }


    protected Http.Builder get(String baseUrl, String url, String tag) {
        Http.Builder builder = Http.get(application, baseUrl, url);
        return configBuilder(builder);
    }

    protected Http.Builder postByRequestBody(String baseUrl, String url){
        Http.Builder builder = Http.postByRequestBody(application, baseUrl, url);
        return configBuilder(builder);
    }


    protected ApplicationController getReengAccountBusiness() {
        return ApplicationController.self();
    }


    public void logApp(String eventName, String actionName, String timeUsing, HttpCallBack httpCallBack){

    }

}
