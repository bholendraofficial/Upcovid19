package com.piyushrai.upcovid19.Activity;

import android.os.Environment;

public class CheckForSDCard {
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
