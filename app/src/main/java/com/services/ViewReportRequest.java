package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.io.Serializable;

/**
 * Created by kanaiyathacker on 10/12/2014.
 */
public class ViewReportRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private String adminId;

    public ViewReportRequest(String adminId) {
        super(Object.class, RestServiceInterface.class);
        this.adminId = adminId;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return getService().viewReport(adminId);
    }
}
