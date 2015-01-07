package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

import java.io.Serializable;

/**
 * Created by kanaiyathacker on 21/12/2014.
 */
public class GetUserAbsenteeRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private String searchType;
    private String searchId;
    private String cardId;
    private String month;
    private String year;
    private String user;
    private String password;

    public GetUserAbsenteeRequest(String searchType , String searchId , String cardId , String month , String year
            , String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.searchType = searchType;
        this.searchId = searchId;
        this.cardId = cardId;
        this.month = month;
        this.year = year;
        this.password = password;
        this.user = user;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return  getService().getUserAbsenteeReport(searchType , searchId ,cardId , month , year , Util.encodeCredentialsForBasicAuthorization(user, password));
    }
}
