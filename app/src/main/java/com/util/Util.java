package com.util;
import android.graphics.Bitmap;
import android.graphics.Color;
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

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String convertDateTimeToString(long dateTime) {
        Date date = new Date(dateTime);
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }

    public static void main(String [] str) {
        System.out.println("Util..... " + convertDateTimeToString(Calendar.getInstance().getTimeInMillis()));
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
            BitMatrix bitMatrix = writer.encode(gson.toJson(user), BarcodeFormat.QR_CODE, 512 , 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            retVal = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            retVal = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
}
