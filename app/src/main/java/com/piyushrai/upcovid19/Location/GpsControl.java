package com.piyushrai.upcovid19.Location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;


public class GpsControl {

    private LocationManager locationManager;
    private MyLocationListener locListener_first;
    private Location bestLocationResult;
    private Context context;


    public GpsControl(Context context) {
        this.context = context;

    }

    /****************************
     * Only work for gps code is commented for network provider
     *******************************/

    public void startGps() {
        try {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locListener_first = new MyLocationListener(context);
        boolean gps_enabled = false;
        //		boolean network_enabled = false;
        Log.e("Start GPS:", "==========");
        float bestAccuracy = 500.0f;
        List<String> matchingProviders = locationManager.getAllProviders();

        for (String provider : matchingProviders) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                if (accuracy < bestAccuracy) {
                    bestLocationResult = location;
                    bestAccuracy = accuracy;
                }
            }
        }

        Log.e("bestLocationResult", "providerName=" + bestLocationResult + " bestAccuracy=" + bestAccuracy);
        try {
            if (bestLocationResult != null) {
                Log.e("location", "location lat: " + bestLocationResult.getLatitude() + " longi: " + bestLocationResult.getLongitude());
                MyLocationListener.latitude = bestLocationResult.getLatitude();

                MyLocationListener.longitude = bestLocationResult.getLongitude();

            } else {
                Log.e("location", "location It is NULL " + bestLocationResult.getLatitude() + " It is NULL " + bestLocationResult.getLongitude());
                MyLocationListener.latitude = 0;
                MyLocationListener.longitude = 0;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            System.out.println("ex.." + ex);
        }
        /***************      Only work for gps code is commented for network provider *******************************/
        //		try {
        //			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //		}
        //		catch (Exception ex) {
        //			System.out.println("ex........."+ex);
        //		}
        if (!gps_enabled)
        //				&& !network_enabled)
        {
            Toast.makeText(context, "Gps Unavailable", Toast.LENGTH_LONG).show();
        }
        if (gps_enabled) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener_first);

        }

        /****************************       Only work for gps code is commented for network provider *******************************/
        //		if (network_enabled)
        //		{
        //			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener_first);
        //		}

        }catch (Exception e){e.printStackTrace();}
    }


    public void stopGps() {
        if (locationManager != null && locListener_first != null) {
            locationManager.removeUpdates(locListener_first);
        }
    }


    public boolean isGPSenabled() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean bool = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return bool;
    }

    public double getLatitude() {
        if (bestLocationResult != null)
            return bestLocationResult.getLatitude();
        else
            return 0;
    }

    public double getLongitude() {
        if (bestLocationResult != null)
            return bestLocationResult.getLongitude();
        else
            return 0;
    }

    public float getSPeed(){
        if (bestLocationResult != null)
            return bestLocationResult.getSpeed();
        else
            return 0;
    }


    public void gpsAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("GPS Setting");
        alert.setMessage("GPS is disabled. Do you want to go to Settings?");
        alert.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gpsAlert();
                dialog.cancel();
            }
        });
        alert.show();
    }

}
