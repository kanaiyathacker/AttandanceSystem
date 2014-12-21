package com.services;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.io.Serializable;

/**
 * Created by kanaiyathacker on 20/12/2014.
 */
public class ContactUsRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

    private String emailId;
    private String contactUsDetails;

    public ContactUsRequest(String emailId, String contactUsDetails) {
        super(Object.class, RestServiceInterface.class);
        this.emailId = emailId;
        this.contactUsDetails = contactUsDetails;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception {
        return  getService().submitEnquiry(emailId, contactUsDetails);
    }
}