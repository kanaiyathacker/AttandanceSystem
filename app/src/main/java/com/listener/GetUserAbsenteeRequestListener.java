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
        List<String> dateList = (List<String>) map.get("absentList");
        dateList.add("12/12/2014");
//        mapVal.put(new Date("12/12/2014") , R.color.absentee);
        if(dateList != null && !dateList.isEmpty()) {
            for(String  date : dateList) {
                caldroidFragment.setBackgroundResourceForDate(R.color.absentee , Util.convertStringToDate(date));
                caldroidFragment.setTextColorForDate(R.color.WHITE,  Util.convertStringToDate(date));
            }
        }
        caldroidFragment.refreshView();
    }
}
