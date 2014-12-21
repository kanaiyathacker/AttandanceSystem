package com.listener;

import com.google.gson.internal.LinkedTreeMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.util.Util;
import com.vaiotech.attendaceapp.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kanaiyathacker on 21/12/2014.
 */
public class GetUserAbsenteeRequestListener implements RequestListener<Object> {

    private CaldroidFragment caldroidFragment;

    public GetUserAbsenteeRequestListener(CaldroidFragment caldroidFragment) {
        this.caldroidFragment = caldroidFragment;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {
        LinkedTreeMap map = (LinkedTreeMap) o;
        List dateList = (List) map.get("absentList");
        caldroidFragment.setBackgroundResourceForDate(R.color.absentee, Util.convertStringToDate("12/12/2014"));
        caldroidFragment.refreshView();
//        caldroidFragment.setTextColorForDate(R.color.white, greenDate);
    }
}
