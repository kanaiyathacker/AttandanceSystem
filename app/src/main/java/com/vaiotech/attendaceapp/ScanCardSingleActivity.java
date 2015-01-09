package com.vaiotech.attendaceapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.GetInfoRequestListener;
import com.listener.SaveAttandanceRequestListener;
import com.listener.ViewReportRequestListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.services.GetInfoRequest;
import com.services.GetServerTimeRequest;
import com.services.SaveAttandanceRequest;
import com.services.ViewReportRequest;
import com.util.Util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_card_single)
public class ScanCardSingleActivity extends BaseActivity {
    private static final String TAG = ScanCardSingleActivity.class.getName();

    @InjectView(R.id.idLableTV) TextView idLableTV;
    @InjectView(R.id.idValueTV) TextView idValueTV;

    @InjectView(R.id.userNameLableTV) TextView userNameLableTV;
    @InjectView(R.id.userNameValueTV) TextView userNameValueTV;


    @InjectView(R.id.adminNameLableTV) TextView adminNameLableTV;
    @InjectView(R.id.adminValueLableTV) TextView adminValueLableTV;
    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.seperateDateTV) TextView seperateDateTV;
    @InjectView(R.id.seperateDateMMTV) TextView seperateDateMMTV;
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

    private String cardId;
    private SaveAttandanceRequest saveAttandanceRequest;
    private GetInfoRequest getInfoRequest;
    private LocationManager lm;
    private User user;

    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;
    private boolean isNFCSupported;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card_single);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();
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
        seperateDateMMTV.setTypeface(digital);
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

        shimmer = new Shimmer();
        if (mNfcAdapter != null) {
              shimmer.start(shimmer_tv);
            isNFCSupported= true;
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ScanCardSingleActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        } else {
            Toast.makeText(this, "Smart Card Scan Not Supported On This Phone", Toast.LENGTH_SHORT).show();
        }

        buildActivitySpinner(user , activitySpinner);
    }

    public void enableForegroundMode() {
        Log.d(TAG, "enableForegroundMode");

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    public void disableForegroundMode() {
        Log.d(TAG, "disableForegroundMode");
        nfcAdapter.disableForegroundDispatch(this);
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
//        if (mNfcAdapter != null) {
//            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
//            mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
//        }
        if(isNFCSupported)
            enableForegroundMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mNfcAdapter != null) {
//            mNfcAdapter.disableForegroundDispatch(this);
//            mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
//        }
        if(isNFCSupported)
            disableForegroundMode();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id =tag.getId();
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

//    @Override
//    public void onNewIntent(Intent intent) {
//        setIntent(intent);
//        resolveIntent(intent);
//    }

    public void save(View view) {
        onItemSelected(user , activitySpinner);
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        AttandanceTransaction t = buildAttandanceTransaction(type , user , Arrays.asList(cardId) , hhET.getText().toString()
                , mmET.getText().toString() , lm);
        saveAttandanceRequest = new SaveAttandanceRequest(t , user.getUserId() , user.getPassword());
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        String msg = (view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + cardId + " noted as " + hhET.getText() + ":" + mmET.getText();
        openDialog(msg);
        cardId = null;
    }

    public void getInfo(View view) {
        showProgressBar();
        getInfoRequest = new GetInfoRequest("CARD_ID" , cardId , user.getUserId() , user.getPassword());
        spiceManager.execute(getInfoRequest, new GetInfoRequestListener(this));
    }

}
