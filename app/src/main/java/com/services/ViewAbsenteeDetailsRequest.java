package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by kanaiyalalt on 10/12/2014.
 */
public class ViewAbsenteeDetailsRequest  extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    String adminId;
    String orgId;

    public ViewAbsenteeDetailsRequest(String adminId, String orgId) {
        super(Object.class, RestServiceInterface.class);
        this.adminId = adminId;
        this.orgId = orgId;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return getService().viewAbsenteeDetailsRequest(adminId , orgId);
    }
}
