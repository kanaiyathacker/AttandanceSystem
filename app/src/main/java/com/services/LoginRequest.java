package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

import java.util.Objects;

/**
 * Created by kanaiyalalt on 14/11/2014.
 */
public class LoginRequest  extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    String loginID;
    String password;
    String deviceId;


    public LoginRequest(String loginID , String password , String deviceId) {
        super(Object.class, RestServiceInterface.class);
        this.loginID = loginID;
        this.password = password;
        this.deviceId = deviceId;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception{
        return getService().login(deviceId , Util.encodeCredentialsForBasicAuthorization(loginID, password));
    }

}
