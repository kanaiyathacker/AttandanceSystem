package com.listener;

import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.R;
import com.vaiotech.attendaceapp.ViewReportActivity;

/**
 * Created by kanaiyalalt on 10/12/2014.
 */
public class ViewReportRequestListener implements RequestListener<Object> {

    private ViewReportActivity viewReportActivity;

    public ViewReportRequestListener(ViewReportActivity viewReportActivity) {
        this.viewReportActivity = viewReportActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {

        TextView totalStrengthValueTV = (TextView) viewReportActivity.findViewById(R.id.totalStrengthValueTV);
        TextView absentValueTV = (TextView) viewReportActivity.findViewById(R.id.absentValueTV);

        LinkedTreeMap map = (LinkedTreeMap)o;
        totalStrengthValueTV.setText(map.get("totalStrength").toString());
        absentValueTV.setText(map.get("totalAbsent").toString());
        viewReportActivity.hideProgressBar();
    }
}
