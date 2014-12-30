package com.listener;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.vaiotech.attendaceapp.BaseActivity;

/**
 * Created by kanaiyalalt on 28/11/2014.
 */
public class SaveAttandanceRequestListener implements com.octo.android.robospice.request.listener.RequestListener<Object> {

    BaseActivity baseActivity;
    public SaveAttandanceRequestListener(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {
        System.out.println("SaveAttandanceRequestListener... " + o);
        baseActivity.hideProgressBar();
    }
}
