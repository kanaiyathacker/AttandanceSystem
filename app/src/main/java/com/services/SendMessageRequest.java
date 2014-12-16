package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by kanaiyathacker on 16/12/2014.
 */
public class SendMessageRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    private String adminId;

    public SendMessageRequest(String adminId) {
        super(Object.class, RestServiceInterface.class);
        this.adminId = adminId;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        return getService().sendMessage(adminId);
    }

}
