package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

/**
 * Created by kanaiyathacker on 16/12/2014.
 */
public class SendMessageRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    private String searchType;
    private String searchId;
    private String user;
    private String password;

    public SendMessageRequest(String searchType , String searchId, String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.searchType = searchId;
        this.searchId = searchType;
        this.password = password;
        this.user = user;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {

        return getService().sendMessage(searchType , searchId,  Util.encodeCredentialsForBasicAuthorization(user, password));
    }

}
