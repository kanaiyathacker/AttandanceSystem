package com.services;

import com.bean.ChangePassword;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.util.Util;

/**
 * Created by kanaiyalalt on 11/12/2014.
 */
public class ChangePasswordRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {

   private ChangePassword changePassword;
   private String user;
   private String password;

    public ChangePasswordRequest(ChangePassword changePassword , String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.changePassword = changePassword;
        this.user = user;
        this.password = password;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {

        return getService().changePassword(changePassword ,  Util.encodeCredentialsForBasicAuthorization(user , password));
    }
}
