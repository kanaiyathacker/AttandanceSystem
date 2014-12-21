package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.io.Serializable;

/**
 * Created by kanaiyathacker on 21/12/2014.
 */
public class GetUserAbsenteeRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private String cardId;
    private String month;
    private String year;

    public GetUserAbsenteeRequest(String cardId , String month , String year) {
        super(Object.class, RestServiceInterface.class);
        this.cardId = cardId;
        this.month = month;
        this.year = year;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return  getService().getUserAbsenteeReport(cardId , month , year);
    }
}
