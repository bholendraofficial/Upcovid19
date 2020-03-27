package com.piyushrai.upcovid19.Activity;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by logimetrix on 1/8/16.
 */
public class Validation {

    private static final String email_regax="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String name_regax="[A-Za-z_ ]+";
    static boolean result;

    public static boolean isNameValidation(EditText view,String msg){
        result=false;
        view.setError(null);
        Pattern pattern=Pattern.compile(name_regax);
        if(!TextUtils.isEmpty(view.getText().toString()) && pattern.matcher(view.getText().toString()).matches())
            result=true;
        else
            view.setError(msg);
        return result;
    }

    public static boolean isEmailValidation(EditText view,String msg){
        result=false;
        view.setError(null);
        if(!TextUtils.isEmpty(view.getText().toString()) && view.getText().toString().matches(email_regax))
            result=true;
        else
            view.setError(msg);
        return result;
    }

    public static boolean isPhoneValidation(EditText view,String msg){
        result=false;
        view.setError(null);
        if(!TextUtils.isEmpty(view.getText().toString()) && view.getText().toString().length()==10)
            result=true;
        else
            view.setError(msg);
        return result;
    }

    public static boolean isBlankField(EditText view,String msg){
        result=false;
        view.setError(null);
        if(!TextUtils.isEmpty(view.getText().toString()))
            result=true;
        else
            view.setError(msg);
        return result;
    }

}
