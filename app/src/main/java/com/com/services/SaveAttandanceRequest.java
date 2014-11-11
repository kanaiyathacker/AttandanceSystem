package com.com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class SaveAttandanceRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {


    public SaveAttandanceRequest() {
        super(Object.class, RestServiceInterface.class);
    }
    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception{
        return null;
    }
}
