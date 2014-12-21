package com.listener;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.ContactUsActivity;

/**
 * Created by kanaiyathacker on 20/12/2014.
 */
public class ContactUsRequestListener implements RequestListener<Object> {

    ContactUsActivity contactUsActivity;
    public ContactUsRequestListener(ContactUsActivity contactUsActivity) {
        this.contactUsActivity = contactUsActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {

    }
}
