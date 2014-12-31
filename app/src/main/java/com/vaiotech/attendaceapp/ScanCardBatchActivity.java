package com.vaiotech.attendaceapp;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.bean.UserMappingBean;
import com.google.gson.Gson;
import com.listener.SaveAttandanceRequestListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import org.ndeftools.Message;
import org.ndeftools.Record;
import org.ndeftools.externaltype.AndroidApplicationRecord;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_card_batch)
public class ScanCardBatchActivity extends BaseActivity {

    private static final String TAG = ScanCardBatchActivity.class.getName();

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private SaveAttandanceRequest saveAttandanceRequest;
    private String cardId;
    private NdefMessage mNdefPushMessage;

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
    private Location location;
    private User user;
    private boolean isNFCSupported;
    private Shimmer shimmer;
    @InjectView(R.id.shimmer_tv) ShimmerTextView shimmer_tv;

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
        setContentView(R.layout.activity_scan_card_batch);
        new PushRequest().execute();
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

        if (mNfcAdapter != null) {
            isNFCSupported = true;
//            Toast.makeText(this, "Smart Card Scan Not Supported", Toast.LENGTH_SHORT).show();
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ScanCardBatchActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        } else {
            isNFCSupported = false;
            Toast.makeText(this, "Smart Card Scan Not Supported In This Phone", Toast.LENGTH_SHORT).show();
        }
        shimmer = new Shimmer();
        shimmer.start(shimmer_tv);
        cardList = new HashSet<String>();
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
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type));
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        openDialog(view);
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setCardId(new ArrayList<String>(cardList));
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

        alertDialogBuilder.setMessage((view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + counterValTV.getText() + "Users noted as " + hhET.getText() + ":" + mmET.getText());
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
        if(cardList != null)
            cardList.clear();
        counterValTV.setText("0");
    }

    private void vibrate() {
        Log.d(TAG, "vibrate");

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_card_batch, menu);
        menu.getItem(0).setTitle(isLogin ? "Log Out" : "Log In");
        return true;
    }

}
