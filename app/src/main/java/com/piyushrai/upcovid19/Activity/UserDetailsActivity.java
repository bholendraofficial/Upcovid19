package com.piyushrai.upcovid19.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.piyushrai.upcovid19.Adapter.QuestionAnswerAdapter;
import com.piyushrai.upcovid19.BuildConfig;
import com.piyushrai.upcovid19.Location.GpsControl;
import com.piyushrai.upcovid19.Model.AnswersItem;
import com.piyushrai.upcovid19.Model.QuestionsItem;
import com.piyushrai.upcovid19.MySharePrefrence;
import com.piyushrai.upcovid19.R;
import com.piyushrai.upcovid19.Retrofit.ApiClient;
import com.piyushrai.upcovid19.Retrofit.ApiInterface;
import com.piyushrai.upcovid19.Retrofit.ApiResponse;
import com.piyushrai.upcovid19.Retrofit.Apitransaction;
import com.piyushrai.upcovid19.Retrofit.Casedetails;
import com.piyushrai.upcovid19.Retrofit.SavemDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.piyushrai.upcovid19.Activity.AppConstants.response;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "UserDetailsActivity";


    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private Spinner gspiner;
    String[] country = {"Male", "Female", "Other"};
    private RadioButton radioMale, radioFemale;
    private boolean a = true;
    private EditText et_mobilenumber, et_name, m_date, et_address, et_passportno, filldetails, et_placestay, et_landline, et_countryvisited, et_dateofDeparture, et_age, et_district, et_passengerhistory;
    final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private String date_n;
    private RadioGroup radio_group;
    private static String gender = "";
    private Button submit;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates;
    private GpsControl gpsControl;
    private String caseid;
    private ApiInterface apiInterface;
    private MySharePrefrence sharedPreference;
    private TextView question;
    private Spinner qspinner;
    private HorizontalScrollView myLinearLayout;
    private TextView[] myTextViews;
    private String[] option = {"Yes", "No", "Mild", "Low"};
    private String m_gender;
    private LinearLayout lineer;
    private List<AnswerList> sku;
    private List<AnswerList> msku;
    private JSONArray jsonArray1;
    private String case_id, ques_id, ans_id, m_ans;
    private ImageView logout;
    private String QuestionAnsDetails;
    public  JSONObject jsonObjectAnswer=new JSONObject();
    private boolean selected;
    private LinearLayout.LayoutParams params;
    private String user_id;
    private RecyclerView recycler_v;
    private AnswerList dataa;
    private QustnAnsDeailst bp;
    private JSONObject optionsObj;
    private View v;
    private List<View> allViewInstance = new ArrayList<View>();
    private JSONObject elem;
    private  RecyclerView rv_recyclerview;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private List<QuestionsItem> answerList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        rv_recyclerview=(RecyclerView)findViewById(R.id.rv_recyclerview);

        questionAnswerAdapter = new QuestionAnswerAdapter(this,answerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_recyclerview.setLayoutManager(mLayoutManager);
        rv_recyclerview.setItemAnimator(new DefaultItemAnimator());
        rv_recyclerview.setAdapter(questionAnswerAdapter);

        caseid = getIntent().getStringExtra("caseid");
        apiInterface = ApiClient.getInstance(this).create(ApiInterface.class);
        sharedPreference = MySharePrefrence.getsharedprefInstance(this);
        hitApi();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mSettingsClient = LocationServices.getSettingsClient(getApplicationContext());
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity((Activity) UserDetailsActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        initv();


    }

    private LinearLayout.LayoutParams getParams(int width, int height) throws Exception {
        params = new LinearLayout.LayoutParams(width, height);
        //params.weight=weight;
        params.gravity = Gravity.CENTER;
        params.setMargins(5, 0, 5, 0);

        return params;

    }

    private void getqns(AnswerList dataa) {
        try {
            boolean isOk = false;
            QuestionAnsDetails = "";
            if (lineer.getChildCount() >= 1) {
                List<QustnAnsDeailst> listBp = new ArrayList<>();
//            for (int i = 0; i < lineer.getChildCount(); i++) {
//            for (int i = 0; i < jsonArray1.length(); i++) {
//                View view = lineer.getChildAt(i);
                for (int i = 0; i < lineer.getChildCount(); i++) {
                    View v = lineer.getChildAt(i);
                 bp = new QustnAnsDeailst();

                for (int j = 0; j < ((ViewGroup) v).getChildCount(); j++) {


                    if (!selected) {
                        isOk = false;
                        Toast.makeText(getApplicationContext(), "Please select all answers ", Toast.LENGTH_SHORT).show();

                    } else {
                        isOk = true;
//                    bp.setQuestion_id(((TextView) m_question).getText().toString());
                        bp.setQuestion_id(dataa.getQuestion_id());
                        bp.setAnswer(dataa.getId());

                    }

//

                }}
                listBp.add(bp);

//        }
                QuestionAnsDetails = new Gson().toJson(listBp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }}


    private void hitApi() {
        {
            try {
                if (mCurrentLocation == null) {
                    if (InternetConnection.isConnected(getApplicationContext())) {


                        retrofit2.Call request = apiInterface.setcasedetails(new Casedetails(
                                sharedPreference.getUserDetails().getApi_token(), caseid

                        ));
                        Apitransaction.startNetworkService(request, getCaseDetailsResponse);
//             mdialog = ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...", true);
//            dialog.setMessage("Please wait.....");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//            dialog.show();
                    } else {
                        Toast.makeText(this, "Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Getting Location Please Wait", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private LinearLayout.LayoutParams getChildParams(int width, int height) {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    ApiResponse getCaseDetailsResponse = new ApiResponse() {
        @Override
        public void OnResponseAPI(Object object) {
            Log.d(TAG, "OnResponseAPI: " + new Gson().toJson(object));
            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
                if (jsonObject.getBoolean("flag")) {
                    JSONObject name = jsonObject.getJSONObject("cases");
                    et_name.setText(name.getString("full_name"));
                    ;
                    et_mobilenumber.setText(name.getString("mobile"));
                    ;
                    et_address.setText(name.getString("address"));
                    et_age.setText(name.getString("age"));
                    et_address.setText(name.getString("address"));
                    m_date.setText(name.getString("track_date"));
                    et_district.setText(name.getString("district"));
                    et_passportno.setText(name.getString("passport_number"));
                    et_dateofDeparture.setText(name.getString("departure_date"));
                    et_countryvisited.setText(name.getString("countries_visited"));
                    et_passengerhistory.setText(name.getString("passenger_history"));
                    m_gender = name.getString("gender");
                    if (m_gender.equalsIgnoreCase("male")) {
                        radioMale.performClick();
                    } else if (m_gender.equalsIgnoreCase("female")) {
                        radioFemale.isChecked();
                    }
                    String age = name.getString("age");
                    case_id = name.getString("case_id");
                    user_id = name.getString("user_id");

//                    String district = name.getString("district");
//                    String passport_number = name.getString("passport_number");
//                    String departure_date = name.getString("departure_date");
//                    String countries_visited = name.getString("countries_visited");
//                    String passenger_history = name.getString("passenger_history");
                    JSONArray jsonQuestions = name.getJSONArray("questions");
                    if (jsonQuestions != null) {
                        for (int i=0;i<jsonQuestions.length();i++)
                        {
                            answerList.add(new QuestionsItem(jsonQuestions.getJSONObject(i)));
                        }
                        questionAnswerAdapter.notifyDataSetChanged();
//                        for (int i = 0; i < jsonArray1.length(); i++) {
//                            elem = jsonArray1.getJSONObject(i);
//                            final LinearLayout customLL = new LinearLayout(getApplicationContext());
//                            customLL.setOrientation(LinearLayout.HORIZONTAL);
//                            customLL.setLayoutParams(getChildParams());
//                            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
//                            TextView tv = new TextView(getApplicationContext());
//
//                            // final LinearLayout custom_view = new LinearLayout(context);
//                            //   custom_view.setOrientation(LinearLayout.VERTICAL);
//
////                            for(int l=0; l<jsonArray1.length()-1; l++) {
//                            tv = new TextView(getApplicationContext());
//                            tv.setTextSize(15);
//                            tv.setLayoutParams(getParams(700/*ViewGroup.LayoutParams.MATCH_PARENT*/, ViewGroup.LayoutParams.MATCH_PARENT));
////                                tv.setId(l);
//                            tv.setText(/*(l + 1) + */ elem.getString("question"));
//
//                            lineer.addView(tv);
//
//
////                                question.setText(elem.getString("question"));
//                            if (elem != null) {
//                                JSONArray prods = elem.getJSONArray("answers");
//                                if (prods != null) {
////                                        for (int j = 0; j < prods.length(); j++) {
////                                            JSONObject innerElem = prods.getJSONObject(j);
////                                            if (innerElem != null) {
////                                                int cat_id = innerElem.getInt("id");
////                                                int pos = innerElem.getInt("question_id");
//                                    Spinner tvs = new Spinner(getApplicationContext());
////                                                for (int lw = 0; lw < prods.length() - 1; lw++) {
//                                    tvs = new Spinner(getApplicationContext());
//                                    tvs.setBackgroundColor(Color.MAGENTA);
//                                    tvs.setFocusable(true);
//                                    tvs.setLayoutParams(getParams(700/*ViewGroup.LayoutParams.MATCH_PARENT*/, ViewGroup.LayoutParams.MATCH_PARENT));
//                                    Type type = new TypeToken<List<AnswerList>>() {
//                                    }.getType();
//                                    sku = new Gson().fromJson(elem.getString("answers"), type);
//
//                                    ArrayAdapter<AnswerList> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sku);
//                                    tvs.setAdapter(adapter);
//                                    if (lineer.getChildCount() >= 1) {
//                                        lineer.removeViewAt(lineer.getChildCount() - 1);
//                                    }
//                                    customLL.addView(tv);
//                                    customLL.addView(tvs);
//                                    lineer.addView(customLL);
//
////                                    for (int k = 0; k < lineer.getChildCount(); k++) {
//                                    final Spinner finalTvs = tvs;
//                                    tvs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                                        @Override
//                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                            AnswerList dataa = (AnswerList) parent.getItemAtPosition(position);
////                                                AnswerList dataa = (AnswerList) parent.getSelectedItem(position);
//                                            if (!dataa.getId().equalsIgnoreCase("select")) {
//
//
//
////                                                    getqns(dataa);
//
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onNothingSelected(AdapterView<?> parent) {
//
//                                        }
//                                    });
////                                        listBp.add(dataa);
//////                                                            bp.setAnswer();
////                                        QuestionAnsDetails = new Gson().toJson(listBp);
////                                    }
////                                                }
//
//
////                                        }
//                                }
//                            }
////                            }
//                        }
                    }
//                    Type type = new TypeToken<List<QuestionModel>>() {
//                    }.getType();
//                    List<QuestionModel> list = new Gson().fromJson(jsonArray1.getJSONArray("questions").toString(), type);

//                    for (int i = 0; i < jsonArray1.length(); i++) {
//                        JSONObject json = jsonArray1.getJSONObject(i);
//                        Iterator<String> keys = json.keys();
//
//                        while (keys.hasNext()) {
//                            String key = keys.next();
//                            System.out.println("Key :" + key + "  Value :" + json.get(key));
//                        }
//
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private LinearLayout.LayoutParams getChildParams() {
            return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private LinearLayout.LayoutParams getParams(int width, int height/*, int weight*/) {
            params = new LinearLayout.LayoutParams(width, height/*, weight*/);
//        params.weight = weight;
            params.gravity = Gravity.CENTER;
            params.setMargins(5, 0, 5, 0);

            return params;
        }

        @Override
        public void OnErrorAPI(String error) {
            Log.d(TAG, "OnErrorAPI: " + error);
            Toast.makeText(UserDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
        }
    };

    private void getQAdetails() {
        if (lineer.getChildCount() >= 1) {
            List<QustnAnsDeailst> list_initiate_typr = new ArrayList<>();
            for (int i = 0; i < lineer.getChildCount(); i++) {
                View view = lineer.getChildAt(i);
                QustnAnsDeailst initiateType = new QustnAnsDeailst();// initialize obj for JSON
                for (int j = 0; j < ((ViewGroup) view).getChildCount() - 1; j++) {

                    View v = ((ViewGroup) view).getChildAt(j);

                    if (v.getTag() instanceof AnswerList) {
                        initiateType.setAnswer(((AnswerList) v.getTag()).getAnswer());
                        initiateType.setQuestion_id(((AnswerList) v.getTag()).getQuestion_id());
                    }}
                list_initiate_typr.add(initiateType);
            }}
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void initv() {
        radioMale = findViewById(R.id.radioMale);
        lineer = findViewById(R.id.lineer);
        radioFemale = findViewById(R.id.radioFemale);
        et_address = findViewById(R.id.et_address);
        et_mobilenumber = findViewById(R.id.et_mobilenumber);
//        question = findViewById(R.id.question);
        gpsControl = new GpsControl(this);
        et_name = findViewById(R.id.et_name);
        et_passportno = findViewById(R.id.et_passportno);
        et_placestay = findViewById(R.id.et_placestay);
        filldetails = findViewById(R.id.filldetails);
        et_landline = findViewById(R.id.et_landline);
        et_district = findViewById(R.id.et_district);
        et_age = findViewById(R.id.et_farmer_age);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
            }
        });
        et_passengerhistory = findViewById(R.id.et_passengerhistory);
        et_countryvisited = findViewById(R.id.et_countryvisited);
        et_dateofDeparture = findViewById(R.id.et_dateofDeparture);
        m_date = findViewById(R.id.date);
        radio_group = findViewById(R.id.radio_group);
//        qspinner = findViewById(R.id.qspinner);

//        qspinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, option);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
//        qspinner.setAdapter(aa);

        submit = findViewById(R.id.submit);
//        double lat = gpsControl.getLatitude();
//        double lang = gpsControl.getLongitude();
        submit.setOnClickListener(this);
        date_n = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        m_date.setText(date_n);
        m_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioMale:
                        gender = "Male";
                        break;
                    case R.id.radioFemale:
                        gender = "Female";
                        break;
                }
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

//        if(gender.equalsIgnoreCase("male")){
//            radioFemale.performClick();
//        }
//        else if(gender.equalsIgnoreCase("female")){
//            radioMale.isChecked();
//        }


    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        m_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                try {
                    getQAdetails();
//                    getdetails( v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                getqns();
                JSONArray jsonArray=new JSONArray();
                for (int i=0;i<answerList.size();i++)
                {
                    List<AnswersItem> answer = answerList.get(i).getAnswers();
                    boolean isYes=answer.get(0).isSelected();
                    if (isYes)
                    {
                        try {
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.putOpt("question_id",answer.get(0).getQuestionId());
                            jsonObject.putOpt("answer_id",answer.get(0).getId());
                            jsonArray.put(jsonObject);
                        }catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                }
                QuestionAnsDetails=jsonArray.toString();
                hitSubmitApi();
                break;
        }
    }

    private void hitSubmitApi() {
        {
            try {
                if (gpsControl == null || gpsControl.getLatitude() < 1 || gpsControl.getLongitude() < 1) {
                    if (gpsControl != null) {
                        gpsControl.startGps();

                    }
                    Toast.makeText(UserDetailsActivity.this, "Please Wait  we are getting your location !!", Toast.LENGTH_SHORT).show();
                }
//                if(mCurrentLocation == null){
                if (InternetConnection.isConnected(getApplicationContext())) {
                    retrofit2.Call request = apiInterface.setDetails(new SavemDetail(
                            sharedPreference.getUserDetails().getApi_token(),
                            caseid,
                            user_id,
                            gpsControl.getLatitude(),
                            gpsControl.getLongitude(),
                            m_date.getText().toString().trim(),
                            QuestionAnsDetails,
                            filldetails.getText().toString().trim()


                    ));

                    Apitransaction.startNetworkService(request, getCaseSaveResponse);
//             mdialog = ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...", true);
//            dialog.setMessage("Please wait.....");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//            dialog.show();
                } else {
                    Toast.makeText(this, "Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();
                }


//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    ApiResponse getCaseSaveResponse = new ApiResponse() {
        @Override
        public void OnResponseAPI(Object object) {
            try {
                Log.d(TAG, "OnResponseAPI: " + new Gson().toJson(object));
                JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
                if (jsonObject.getBoolean("flag")) {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }

        @Override
        public void OnErrorAPI(String error) {
            Log.d(TAG, "OnErrorAPI: " + error);
        }
    };

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Activity) UserDetailsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        // Toast.makeText(context.getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener((Activity) UserDetailsActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(UserDetailsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                // Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), option[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
