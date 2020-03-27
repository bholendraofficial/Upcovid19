package com.piyushrai.upcovid19.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.piyushrai.upcovid19.MySharePrefrence;
import com.piyushrai.upcovid19.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class Permissions extends AppCompatActivity {


    private static final int REQUEST_PERMISSION_PHONE_STATE = 2;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 4;
    private static final int REQUEST_PERMISSION_FINE_LOCATION = 5;

    boolean check = true;

    MySharePrefrence shPrfs;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        shPrfs = MySharePrefrence.getsharedprefInstance(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            redirect("");
        } else {
            if (checkPermission()) {
                redirect("");
            } else {
                requestPermission();
            }
        }
    }


    private void redirect(String from) {

        startActivity(new Intent(this, SplashScreen.class));
        finish();
    }


    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(Permissions.this, new String[]{
                    CAMERA,
                    /*  READ_CONTACTS,*/
                    READ_PHONE_STATE,
                    ACCESS_FINE_LOCATION,
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE/*,
                    CALL_PHONE*/}, RequestPermissionCode);
        }

    }

    public boolean checkPermission() {

        int firstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        /*int secondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);*/
        int thirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int fourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int fifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int sixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        /* int seventhPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);*/

        return firstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                /*secondPermissionResult == PackageManager.PERMISSION_GRANTED &&*/
                thirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                fourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                fifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                sixthPermissionResult == PackageManager.PERMISSION_GRANTED/* &&
                seventhPermissionResult == PackageManager.PERMISSION_GRANTED*/;

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0) {
                boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                /*  boolean readContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;*/
                boolean readPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean readFineLocationPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStoragePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalStoragePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                /*     boolean callPhonePermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;*/

                if (cameraPermission /*&& readContactsPermission*/ && readPhoneStatePermission && readFineLocationPermission
                        && readExternalStoragePermission && writeExternalStoragePermission /*&& callPhonePermission*/) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    //CustomToast.initToast(this,"Permission Granted");
                    redirect("");
                } else {
                    if (shouldShowRequestPermissionRationale(CAMERA) /*|| shouldShowRequestPermissionRationale(READ_CONTACTS)*/
                            || shouldShowRequestPermissionRationale(READ_PHONE_STATE) || shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
                            || shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)
                        /* || shouldShowRequestPermissionRationale(CALL_PHONE)*/) {
                        showMessageOKCancel(
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA, /*READ_CONTACTS,*/ READ_PHONE_STATE, ACCESS_FINE_LOCATION,
                                                    READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE/*, CALL_PHONE*/}, RequestPermissionCode);
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Please Click on Permissions and provide all Required permissions.", Toast.LENGTH_LONG).show();
                        //CustomToast.initToast(this,"Please Click on Permissions and provide all Required permissions.");
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                        intent.setData(uri);
                        this.startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Permissions.this)
                .setMessage("You need to allow  permissions")
                .setPositiveButton("OK", okListener)
                /* .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         Permissions.this.finish();
                     }
                 })*/
                .create()
                .show();
    }

    public static boolean getPermissionDetail(Context con) {
        if (ContextCompat.checkSelfPermission(con, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            return false;
        else if (ContextCompat.checkSelfPermission(con, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            return false;
        else if (ContextCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false;
        else return true;
    }

}
