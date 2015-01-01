package com.services;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.inject.Singleton;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.io.Serializable;

import roboguice.inject.ContextSingleton;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class SaveAttandanceRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private AttandanceTransaction attandanceTransaction;

    public SaveAttandanceRequest(AttandanceTransaction attandanceTransaction) {
        super(Object.class, RestServiceInterface.class);
        this.attandanceTransaction = attandanceTransaction;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception{
        return getService().save(attandanceTransaction);
    }
}
