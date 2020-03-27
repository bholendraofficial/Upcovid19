package com.piyushrai.upcovid19.Activity;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by logimetrix on 1/8/16.
 */
public class AppConstants {

    public static String error_code = "error_code";
    public static String response = "response_string";

    public static String forsucessresponse = "0";
    public static String forfailedresponse = "1";
    public static String Ait = "0";

    public static String newOrder = "0";
    public static String process = "1";
    public static String delivered = "2";
    public static String received = "3";
    public static String decline = "4";

    public static String orderdeliver = "1";
    public static String ordercancel = "2";
    public static String orderprocess = "0";

    public static int orderInsert = 0;
    public static int orderUpdate = 1;

    public static int bold = 0;
    public static int light = 1;
    public static int regular = 2;
    public static int semibold = 3;

    public static int flip = 0;

    public static boolean per_phone;
    public static boolean per_location;
    public static boolean per_storage;
    private static final int JOB_ID = 10000;
    public static final int TRACK_JOB_ID = JOB_ID - 1;


    public static String formatdate(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;
    }

    public static String formatdate1(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date convertedDate = d.parse(fdate);
            datetime = inputFormat.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;
    }

    public static List<String> getPriorityList() {
        List<String> priortyList = new ArrayList<>();
        priortyList.add("Low");
        priortyList.add("Medium");
        priortyList.add("High");

        return priortyList;
    }

    public static List<String> getReminderList() {
        List<String> reminderList = new ArrayList<>();
        reminderList.add("Monthly");
        reminderList.add("Weekly");
        reminderList.add("Daily");
        reminderList.add("Not Required");

        return reminderList;
    }

    public static void setHideSoftKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
