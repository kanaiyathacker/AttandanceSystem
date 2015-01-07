package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

/**
 * Created by kanaiyathacker on 16/12/2014.
 */
public class SendMessageRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    private String adminId;
    private String user;
    private String password;

    public SendMessageRequest(String adminId , String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.user = user;
        this.password = password;
        this.adminId = adminId;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {

        return getService().sendMessage(adminId,  Util.encodeCredentialsForBasicAuthorization(user, password));
    }

}
