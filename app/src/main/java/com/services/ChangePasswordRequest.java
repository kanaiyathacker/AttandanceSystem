package com.services;

import com.bean.ChangePassword;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by kanaiyalalt on 11/12/2014.
 */
public class ChangePasswordRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {

   private ChangePassword changePassword;

    public ChangePasswordRequest(ChangePassword changePassword) {
        super(Object.class, RestServiceInterface.class);
        this.changePassword = changePassword;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        return getService().changePassword(changePassword);
    }
}
