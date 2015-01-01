package com.util;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.services.LoginRequest;
import com.vaiotech.attendaceapp.R;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by kanaiyalalt on 28/11/2014.
 */
public class Util {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATETIME_FORMAT = "dd/MM/yyyy hh:mm:ss";
    public static final String NTC_DATETIME_FORMAT = "EEE, MMM dd yyyy HH:mm:sss";


    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static Date convertStringToDate(String date) {
        Date retVal = null;
        SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
        try {
            retVal = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static Date convertStringToDate(String date , String dateFormat) {
        Date retVal = null;
        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        try {
            retVal = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static String convertDateTimeToString(long dateTime) {
        Date date = new Date(dateTime);
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }

    public static void main(String [] str) {
        SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date = f.parse("01/12/2014");
            System.out.println("Util..... " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static String getEditViewText(EditText editText) {
        return editText.getText().toString();
    }

    public static String getTextViewText(TextView textView) {
        return textView.getText().toString();
    }

    public static Bitmap generateQRImage(User user , long dateTime) {
        Bitmap retVal = null;
        Gson gson = new Gson();
        user.setPassword(null);
        user.setDateTime(convertDateTimeToString(dateTime));
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(gson.toJson(user), BarcodeFormat.QR_CODE, 450 , 450);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            retVal = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    retVal.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static String query(String ntpServerHostname) throws IOException, SocketException {
        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 10 seconds
        client.setDefaultTimeout(10000);

        TimeInfo info = null;
        try {
            client.open();

            InetAddress hostAddr = InetAddress.getByName(ntpServerHostname);
            Log.d("Query", "Trying to get time from " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());

            info = client.getTime(hostAddr);
        } finally {
            client.close();
        }

        // compute offset/delay if not already done
        info.computeDetails();

        return info.getMessage().getTransmitTimeStamp().toDateString();
    }

    public static String paddingZero(int value) {
        return ""+(value < 10 ? "0"+ value : value);

    }

    public static String trimToEmpty(Object value) {
        return value != null ? String.valueOf(value) : "";
    }
}
