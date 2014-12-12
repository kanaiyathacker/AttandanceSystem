package com.util;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by kanaiyalalt on 28/11/2014.
 */
public class Util {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static void main(String [] str) {
        System.out.println("Util..... " + convertDateToString(new Date()));
    }

    public static String getEditViewText(EditText editText) {
        return editText.getText().toString();
    }
}
