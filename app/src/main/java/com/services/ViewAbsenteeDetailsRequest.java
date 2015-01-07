package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.util.Util;

/**
 * Created by kanaiyalalt on 10/12/2014.
 */
public class ViewAbsenteeDetailsRequest  extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    String adminId;
    String orgId;
    private String user;
    private String password;

    public ViewAbsenteeDetailsRequest(String adminId, String orgId , String user , String password) {
        super(Object.class, RestServiceInterface.class);
        this.adminId = adminId;
        this.orgId = orgId;
        this.password = password;
        this.user = user;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return getService().viewAbsenteeDetailsRequest(adminId , orgId ,  Util.encodeCredentialsForBasicAuthorization(user, password));
    }
}
