package com.piyushrai.upcovid19;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MySharePrefrence {
    private static final String LOCAL_STORAGE = "com.piyushrai.upcovid19.sharedPreference";
    private static MySharePrefrence mySharedPrfs;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    Context context;

    MySharePrefrence(Context context) {
        this.sharedPreferences = context.getSharedPreferences(LOCAL_STORAGE, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }
    public static MySharePrefrence getsharedprefInstance(Context con) {
        if (mySharedPrfs == null)
            mySharedPrfs = new MySharePrefrence(con);
        return mySharedPrfs;
    }
    public void setUserDetails(String userDetails) {
        editor.putString("USER_DETAILS", userDetails)
                .commit();
    }

    public void setUserID(String id){
        editor.putString("USER_ID", id)
                .commit();
    }
    public String getUserID() {
        return sharedPreferences.getString("USER_ID", "empty");
    }
    public UserDetail getUserDetails() {
        return new Gson().fromJson(sharedPreferences.getString("USER_DETAILS", null), UserDetail.class);
    }
    public void setTeacherDetails(String s) {
        editor.putString("TEACHER_DETAILS", s)
                .commit();
    }
    public void setLoggedIn(boolean status) {
        editor.putBoolean("isLoggedIn", status)
                .commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
