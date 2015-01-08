package com.services;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class AttandanceRestService extends RetrofitGsonSpiceService {
    private final static String BASE_URL = "http://kanserviceslive.elasticbeanstalk.com";
//    private final static String BASE_URL = "http://192.168.0.3:8080/DeviceTokenApplicaion-0.0.1-SNAPSHOT";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(RestServiceInterface.class);

    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }
}
