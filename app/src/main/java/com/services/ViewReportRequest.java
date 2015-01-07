package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

import java.io.Serializable;

/**
 * Created by kanaiyathacker on 10/12/2014.
 */
public class ViewReportRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private String adminId;
    private String orgId;
    private String user;
    private String password;

    public ViewReportRequest(String adminId , String orgId , String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.adminId = adminId;
        this.orgId = orgId;
        this.password = password;
        this.user = user;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {

        return  getService().viewReport(adminId , orgId  , Util.encodeCredentialsForBasicAuthorization(user, password));
    }
}
