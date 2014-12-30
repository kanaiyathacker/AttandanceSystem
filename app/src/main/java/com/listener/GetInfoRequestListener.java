package com.listener;

import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.vaiotech.attendaceapp.BaseActivity;
import com.vaiotech.attendaceapp.ManualEntryActivity;
import com.vaiotech.attendaceapp.R;

import static java.lang.String.valueOf;
/**
 * Created by kanaiyathacker on 03/12/2014.
 */
public class GetInfoRequestListener implements com.octo.android.robospice.request.listener.RequestListener<Object> {

    private BaseActivity baseActivity;

    public GetInfoRequestListener(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {
        LinkedTreeMap map = (LinkedTreeMap) o;
        String name = valueOf(map.get("fName")) + " " +valueOf(map.get("mName")) + " " + valueOf(map.get("lName"));
        String contactNo = valueOf(map.get("contactNumber"));
        TextView nameValueTV = (TextView)baseActivity.findViewById(R.id.userNameValueTV);
        nameValueTV.setText(name);
        if(name != null && name.trim().length() > 0) {
            baseActivity.findViewById(R.id.inBUTTON).setEnabled(true);
            baseActivity.findViewById(R.id.inBUTTON).setAlpha(1f);
            baseActivity.findViewById(R.id.outBUTTON).setEnabled(true);
            baseActivity.findViewById(R.id.outBUTTON).setAlpha(1f);

            if(baseActivity instanceof ManualEntryActivity) {
                ((ManualEntryActivity)baseActivity).setCardId(""+map.get("cardId"));
            }
        }
        baseActivity.hideProgressBar();
    }
}
