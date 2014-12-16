package com.listener;

import android.view.View;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.R;
import com.vaiotech.attendaceapp.ViewReportActivity;

/**
 * Created by kanaiyathacker on 16/12/2014.
 */
public class SendMessageRequestListener implements RequestListener<Object> {

    private ViewReportActivity viewReportActivity;

    public SendMessageRequestListener(ViewReportActivity viewReportActivity) {
        this.viewReportActivity = viewReportActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {
        ProgressBar progressBar = (ProgressBar) viewReportActivity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
