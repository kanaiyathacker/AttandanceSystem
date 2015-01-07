package com.services;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

import java.io.Serializable;

/**
 * Created by kanaiyathacker on 03/12/2014.
 */
public class GetInfoRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private String searchType;
    private String searchValue;
    private String user;
    private String password;

    public GetInfoRequest(String searchType , String  searchValue  , String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.searchType = searchType;
        this.searchValue = searchValue;
        this.password = password;
        this.user = user;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return getService().getUserInfo(searchType , searchValue ,  Util.encodeCredentialsForBasicAuthorization(user, password));
    }
}
