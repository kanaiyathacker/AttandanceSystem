package com.services;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.inject.Singleton;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

import java.io.Serializable;

import roboguice.inject.ContextSingleton;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class SaveAttandanceRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private AttandanceTransaction attandanceTransaction;
    String loginID;
    String password;

    public SaveAttandanceRequest(AttandanceTransaction attandanceTransaction , String loginID , String password) {
        super(Object.class, RestServiceInterface.class);
        this.attandanceTransaction = attandanceTransaction;
        this.loginID = loginID;
        this.password = password;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception{
        return getService().save(attandanceTransaction, Util.encodeCredentialsForBasicAuthorization(loginID ,password
        ));
    }
}
