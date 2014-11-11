package com.com.services;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class AttandanceRestService extends RetrofitGsonSpiceService {
    private final static String BASE_URL = "http://mypanchang.com/rssfeeddata.php";

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
