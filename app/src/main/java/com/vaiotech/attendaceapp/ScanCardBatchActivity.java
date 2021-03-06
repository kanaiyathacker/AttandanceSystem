package com.vaiotech.attendaceapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.SaveAttandanceRequestListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.services.GetServerTimeRequest;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_card_batch)
public class ScanCardBatchActivity extends BaseActivity {

    private static final String TAG = ScanCardBatchActivity.class.getName();

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcAdapter mNfcAdapter;
    private SaveAttandanceRequest saveAttandanceRequest;
    private String cardId;

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

    @InjectView(R.id.counterLableTV) TextView counterLableTV;


    @InjectView(R.id.counterValTV) TextView counterValTV;
    @InjectView(R.id.inBUTTON)  Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;
    @InjectView(R.id.activitySpinner) Spinner activitySpinner;
    private Set<String> cardList;
    private SharedPreferences sharedPreferences;
    boolean isLogin;
    boolean isUserAdmin;

    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent;
    private LocationManager lm;
    private User user;
    private boolean isNFCSupported;
    private Shimmer shimmer;
    @InjectView(R.id.shimmer_tv) ShimmerTextView shimmer_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card_batch);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();
        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        isUserLogedIn();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        seperateDateTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);
        counterLableTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);

        counterValTV.setTypeface(digital);

        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);

        inBUTTON.setEnabled(false);
        outBUTTON.setEnabled(false);

        inBUTTON.setAlpha(.5f);
        outBUTTON.setAlpha(.5f);

        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        adminValueLableTV.setText(user.getfName() + " " +  user.getlName());

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        shimmer = new Shimmer();
        if (mNfcAdapter != null) {
            shimmer.start(shimmer_tv);
            isNFCSupported = true;
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ScanCardBatchActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        } else {
            isNFCSupported = false;
            Toast.makeText(this, "Smart Card Scan Not Supported On This Phone", Toast.LENGTH_SHORT).show();
        }
        cardList = new HashSet<String>();
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

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id =tag.getId();
            long cardId = getReversed(id);
            if(!cardList.contains((""+cardId))) {
                this.cardId = ""+cardId;
                String count = counterValTV.getText().toString();
                int cnt = Integer.parseInt(count);
                ++cnt;
                counterValTV.setText("" + cnt);
                cardList.add(this.cardId);
                if (cnt > 0) {
                    inBUTTON.setEnabled(true);
                    outBUTTON.setEnabled(true);

                    inBUTTON.setAlpha(1f);
                    outBUTTON.setAlpha(1f);
                    shimmer.cancel();
                    shimmer_tv.setText("");
                }
            }
        }
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
//           mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
//        }
        if(isNFCSupported)
            disableForegroundMode();
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                long cardId = getReversed(id);
            this.cardId = ""+cardId;
            String count =counterValTV.getText().toString();
            int cnt = Integer.parseInt(count);
            ++cnt;
            counterValTV.setText(""+cnt);
            cardList.add(this.cardId);
            if(cnt > 0 ) {
                inBUTTON.setEnabled(true);
                outBUTTON.setEnabled(true);

                inBUTTON.setAlpha(1f);
                outBUTTON.setAlpha(1f);
                shimmer.cancel();
                shimmer_tv.setText("");
            }

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

    public void save(View view) {
        onItemSelected(user , activitySpinner);
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        AttandanceTransaction t = buildAttandanceTransaction(type , user , new ArrayList<String>(cardList) , hhET.getText().toString()
                , mmET.getText().toString() , lm);
        saveAttandanceRequest = new SaveAttandanceRequest(t , user.getUserId() , user.getPassword());

        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        String msg = (view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + counterValTV.getText() + "Users noted as " + hhET.getText() + ":" + mmET.getText();
        openDialog(msg);
        if(cardList != null)
            cardList.clear();
        counterValTV.setText("0");
    }

//    public AttandanceTransaction buildAttandanceTransaction(String type) {
//        AttandanceTransaction t = new AttandanceTransaction();
//        t.setAdminId(user.getUserId());
//        t.setCardId(new ArrayList<String>(cardList));
//        t.setDate(Util.convertDateToString(new Date()));
//        t.setTime(hhET.getText() + ":" + mmET.getText());
//        t.setOrgId(user.getCoId());
//        t.setType(type);
//        if(lm != null) {
//            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if(location != null) {
//                t.setLongitude(""+location.getLongitude());
//                t.setLatitude(""+location.getLatitude());
//            }
//        }
//        return t;
//    }

    private void vibrate() {
        Log.d(TAG, "vibrate");

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(500);
    }
}
