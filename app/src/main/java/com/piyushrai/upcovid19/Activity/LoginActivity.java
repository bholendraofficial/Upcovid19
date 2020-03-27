package com.piyushrai.upcovid19.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.piyushrai.upcovid19.MySharePrefrence;
import com.piyushrai.upcovid19.R;
import com.piyushrai.upcovid19.Retrofit.ApiClient;
import com.piyushrai.upcovid19.Retrofit.ApiInterface;
import com.piyushrai.upcovid19.Retrofit.ApiResponse;
import com.piyushrai.upcovid19.Retrofit.Apitransaction;
import com.piyushrai.upcovid19.Retrofit.SetLogin;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int STORAGE_PERMISSION_CODE = 55;
    private MySharePrefrence sharedPreference;
    private ApiInterface apiInterface;
    private EditText username, password;
    private ImageButton button;
    private Button download;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static String file_url = "http://covid-19.logimetrix.info/assets/mannual/mannual.pptx";
    private String downloadPdfUrl = "http://covid-19.logimetrix.info/assets/mannual/mannual.pptx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mrequestPermission();


        initview();


    }

    private void mrequestPermission() {
        if (isReadStorageAllowed()) {
            //If permission is already having then showing the toast
            Toast.makeText(LoginActivity.this, " ", Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))&&(ActivityCompat.shouldShowRequestPermissionRationale(this,WRITE_EXTERNAL_STORAGE))) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void initview() {
        apiInterface = ApiClient.getInstance(this).create(ApiInterface.class);
        sharedPreference = MySharePrefrence.getsharedprefInstance(this);

        button = findViewById(R.id.signin);
        download = findViewById(R.id.download);

        username = findViewById(R.id.user);
        password = findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();


//                Intent intent = new Intent(Login.this,MainActivity.class);
//                startActivity(intent);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hitApiForExcelDownload();
//                new DownloadTask(LoginActivity.this,  downloadPdfUrl);

            }
        });
    }


    private void hitApiForExcelDownload() {

        String URL = "http://covid-19.logimetrix.info/assets/mannual/mannual.pptx";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd" );

//        try {
//            URL += "?date_from=" + targetFormat.format(originalFormat.parse(date1.getText().toString()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        URL += "?date_from=" + formatter1.parse(date1.getText().toString());

//        try {
//            URL += "&date_to=" + targetFormat.format(originalFormat.parse(date2.getText().toString()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        URL += "&LoginID=" + getAllUserData.getLoginID();
//        URL += "&date_to=" + date2.getText().toString();
        Log.d(TAG, "hitApiForExcelDownload: "+URL);
//
        new DownloadFile().execute(URL);

    }
    private class  DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(LoginActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setTitle("File Location");
            this.progressDialog.setMessage("Get File in Mannual Folder");
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_"+"AppManual" + fileName +".pptx";

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }



    private void login() {
        try {
            if (validation()) {
                if (InternetConnection.isConnected(getApplicationContext())) {
                    retrofit2.Call request = apiInterface.login(new SetLogin(
                            username.getText().toString().trim(),
                            password.getText().toString().trim()
                    ));

                    Apitransaction.startNetworkService(request, loginResponse);
//             mdialog = ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...", true);
//            dialog.setMessage("Please wait.....");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//            dialog.show();
                } else {
                    Toast.makeText(this, "Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Fill Required data", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    ApiResponse loginResponse = new ApiResponse() {
        @Override
        public void OnResponseAPI(Object object) {
            try {
                Log.d(TAG, "OnResponseAPI: " + new Gson().toJson(object));
                JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
                if (jsonObject.getBoolean("flag")) {

                    sharedPreference.setUserDetails(jsonObject.getString("user"));
                    sharedPreference.setUserID(username.getText().toString().trim());
                    sharedPreference.setLoggedIn(true);

                    username.setText("");
                    password.setText("");
                    startActivity(new Intent(LoginActivity.this, CaseList.class));
                    finish();

                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 100);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                mdialog.dismiss();
            }
        }

        @Override
        public void OnErrorAPI(String error) {
            Log.d(TAG, "OnErrorAPI: " + error);
//            mdialog.dismiss();
        }
    };

    private boolean validation() {
        if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Email ID", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




}


