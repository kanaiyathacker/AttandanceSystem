package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.barcodescannerfordialogs.DialogScanner;
import com.barcodescannerfordialogs.helpers.CameraFace;
import com.com.bean.Person;
import com.com.services.AttandanceRestService;
import com.com.services.SaveAttandanceRequest;
import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import java.util.Calendar;


public class HomeActivity extends Activity implements DialogScanner.OnQRCodeScanListener ,  OnQRCodeReadListener {

    static final String TAG = Activity.class.getSimpleName();
    private TimePicker timePicker;
    SpiceManager spiceManager = new SpiceManager(AttandanceRestService.class);
    SaveAttandanceRequest saveAttandanceRequest;
    Person person;

    private QRCodeReaderView mydecoderview;
    private ImageView line_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.kanaiya);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        Calendar cal = Calendar.getInstance();
        timePicker.setCurrentHour(cal.get(Calendar.HOUR));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        timePicker.setIs24HourView(true);
//        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
//        mydecoderview.setOnQRCodeReadListener(this);
//
//        line_image = (ImageView) findViewById(R.id.red_line_image);

        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
//        line_image.setAnimation(mAnimation);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchScanBack(View view)
    {
        DialogScanner dialog = DialogScanner.newInstance(CameraFace.BACK);
        dialog.show(getFragmentManager(), "dialogScanner");
//        mydecoderview.getCameraManager().startPreview();
    }

    public void launchScanFront(View view)
    {
        DialogScanner dialog = DialogScanner.newInstance(CameraFace.FRONT);
        dialog.show(getFragmentManager(), "dialogScanner");
    }

    @Override
    public void onQRCodeScan(String contents)
    {
        System.out.println("********** content --- " + contents);
        Gson gson = new Gson();
        Person person = gson.fromJson(contents , Person.class);
        TextView tvName = (TextView)findViewById(R.id.textViewName);
        tvName.setText(person.getfName() + " " + person.getmName() + " " + person.getLname());

        TextView tvCo = (TextView)findViewById(R.id.textViewCo);
        tvCo.setText(person.getCoName());

        TextView tvId = (TextView)findViewById(R.id.textViewId);
        tvId.setText(person.getId());

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        if("12132131".equals(person.getId()))
            imageView.setImageResource(R.drawable.kanaiya);
        else if("12132132".equals(person.getId()))
            imageView.setImageResource(R.drawable.swarnaba);
        else if("12132133".equals(person.getId()))
            imageView.setImageResource(R.drawable.najmul);
        else if("12132134".equals(person.getId()))
            imageView.setImageResource(R.drawable.amit);
        else if("12132135".equals(person.getId()))
            imageView.setImageResource(R.drawable.vikram);

    }

    public void save(View view) {
        mydecoderview.getCameraManager().startPreview();

//        saveAttandanceRequest = new SaveAttandanceRequest("1" )

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }
}
