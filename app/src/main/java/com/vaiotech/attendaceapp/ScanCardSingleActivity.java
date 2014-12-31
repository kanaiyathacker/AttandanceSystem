package com.vaiotech.attendaceapp;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.bean.UserMappingBean;
import com.google.gson.Gson;
import com.listener.GetInfoRequestListener;
import com.listener.SaveAttandanceRequestListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.services.GetInfoRequest;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_card_single)
public class ScanCardSingleActivity extends BaseActivity {

    @InjectView(R.id.idLableTV) TextView idLableTV;
    @InjectView(R.id.idValueTV) TextView idValueTV;

    @InjectView(R.id.userNameLableTV) TextView userNameLableTV;
    @InjectView(R.id.userNameValueTV) TextView userNameValueTV;


    @InjectView(R.id.adminNameLableTV) TextView adminNameLableTV;
    @InjectView(R.id.adminValueLableTV) TextView adminValueLableTV;
    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.seperateDateTV) TextView seperateDateTV;
    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.hhET) EditText hhET;
    @InjectView(R.id.mmET) EditText mmET;

    @InjectView(R.id.dateTV) TextView dateTV;
    @InjectView(R.id.ddET) EditText ddET;
    @InjectView(R.id.MMET) EditText MMET;
    @InjectView(R.id.yyET) EditText yyET;
    @InjectView(R.id.inBUTTON) Button inBUTTON;

    @InjectView(R.id.outBUTTON) Button outBUTTON;
    @InjectView(R.id.getInfoBUTTON) Button getInfoBUTTON;
    private NfcAdapter mNfcAdapter;

    @InjectView(R.id.shimmer_tv) ShimmerTextView shimmer_tv;
    @InjectView(R.id.activitySpinner) Spinner activitySpinner;
    private Shimmer shimmer;

    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private String cardId;
    private NdefMessage mNdefPushMessage;
    private SaveAttandanceRequest saveAttandanceRequest;
    private GetInfoRequest getInfoRequest;
    private LocationManager lm;
    private Location location;
    private User user;

    private class PushRequest extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... server) {
            String result = null;
            try {
                result = Util.query("3.in.pool.ntp.org");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String result) {
            Date dateTime = Util.convertStringToDate(result, Util.NTC_DATETIME_FORMAT);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTime);
            cal.set(Calendar.AM_PM, Calendar.PM);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            hhET.setText(""+hour);
            mmET.setText(""+(min < 10 ? "0"+ min : min));

            int date = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);

            ddET.setText("" + date);
            MMET.setText("" + month);
            yyET.setText("" + year);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card_single);
        new PushRequest().execute();
        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        isUserLogedIn();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        idLableTV.setTypeface(font);
        idValueTV.setTypeface(font);

        userNameLableTV.setTypeface(font);
        userNameValueTV.setTypeface(font);

        adminNameLableTV.setTypeface(font);
        adminValueLableTV.setTypeface(font);

        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        seperateDateTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);
        getInfoBUTTON.setTypeface(font);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        inBUTTON.setEnabled(false);
        outBUTTON.setEnabled(false);
        getInfoBUTTON.setEnabled(false);

        inBUTTON.setAlpha(.5f);
        outBUTTON.setAlpha(.5f);
        getInfoBUTTON.setAlpha(.5f);

        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        adminValueLableTV.setText(user.getfName() + " " +  user.getlName());

        if (mNfcAdapter != null) {
//            Toast.makeText(this, "Read an NFC tag", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "This phone is not NFC enabled", Toast.LENGTH_SHORT).show();
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ScanCardSingleActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });
        shimmer = new Shimmer();
        shimmer.start(shimmer_tv);
        buildActivitySpinner();
    }

    private void buildActivitySpinner() {
        List<UserMappingBean> userMappingList = user.getUserMappingList();
        List<String> list = new ArrayList<String>();

        Map<String, String> branches = user.getBranchs();
        Map<String, String> departs = user.getDeparts();

        for(UserMappingBean curr : userMappingList) {
            String branch = curr.getBranchId();
            String depart = curr.getDepartId();
            if(branch != null && depart != null && branch.length()> 0 && depart.length() > 0) {
                String val = branches.get(branch) + " - " + departs.get(depart);
                list.add(val);
            }
            if(branch != null && depart.length() > 0 && depart == null ) {
                String val = branches.get(branch);
                list.add(val);
            }
        }
        if(list.isEmpty()) {
            list.add("Add Org");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list);
        activitySpinner.setAdapter(adapter);
//        activitySpinner
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
            mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            long cardId = getReversed(id);
            this.cardId = ""+cardId;
            idValueTV.setText(this.cardId);

            inBUTTON.setEnabled(true);
            outBUTTON.setEnabled(true);
            getInfoBUTTON.setEnabled(true);


            inBUTTON.setAlpha(1f);
            outBUTTON.setAlpha(1f);
            getInfoBUTTON.setAlpha(1f);
            shimmer.cancel();
            shimmer_tv.setText("");
        }
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    public void save(View view) {
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type));
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        openDialog(view);
    }

    public void getInfo(View view) {
        showProgressBar();
        getInfoRequest = new GetInfoRequest("CARD_ID" , cardId);
        spiceManager.execute(getInfoRequest, new GetInfoRequestListener(this));
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setCardId(Arrays.asList(cardId));
        t.setDate(Util.convertDateToString(new Date()));
        t.setTime(hhET.getText() + ":" + mmET.getText());
        t.setOrgId(user.getCoId());
        t.setType(type);
        if(lm != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                t.setLongitude(""+location.getLongitude());
                t.setLatitude(""+location.getLatitude());
            }
        }
        return t;
    }

    public void openDialog(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage((view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + cardId + " noted as " + hhET.getText() + ":" + mmET.getText());
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        cardId = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_card_single, menu);
        menu.getItem(0).setTitle(isLogin ? "Log Out" : "Log In");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ("Log Out" == item.getTitle()) {
            sharedPreferences.edit().remove("USER_DETAILS").commit();
            isUserLogedIn();
            item.setTitle("Log In");
        } else {
            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
