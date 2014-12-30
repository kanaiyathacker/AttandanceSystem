package com.listener;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.internal.LinkedTreeMap;
import com.listcomponent.Item;
import com.listcomponent.ItemAdapter;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.R;
import com.vaiotech.attendaceapp.ViewReportActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanaiyalalt on 10/12/2014.
 */
public class ViewAbsenteeDetailsRequestListener implements RequestListener<Object> {

    private ViewReportActivity viewReportActivity;

    public ViewAbsenteeDetailsRequestListener(ViewReportActivity viewReportActivity) {
        this.viewReportActivity = viewReportActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {

        LinkedTreeMap map = (LinkedTreeMap)o;
        List<LinkedTreeMap> listMap = (List<LinkedTreeMap>) map.get("absentList");
        List<Item> result = new ArrayList<Item>();
        result.add(new Item("User Id" , "Name"));
        for(LinkedTreeMap currVal : listMap) {
            String name = currVal.get("fName") + " " + currVal.get("lName");
            result.add(new Item(currVal.get("userId").toString() , name));
        }

        ListView listView = (ListView)viewReportActivity.findViewById(R.id.absentResultLV);
        ItemAdapter adapter = new ItemAdapter(viewReportActivity , R.layout.list_item, result);
        listView.setAdapter(adapter);
        viewReportActivity.hideProgressBar();
    }
}
