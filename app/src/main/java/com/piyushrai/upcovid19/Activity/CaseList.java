package com.piyushrai.upcovid19.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piyushrai.upcovid19.MySharePrefrence;
import com.piyushrai.upcovid19.R;
import com.piyushrai.upcovid19.Retrofit.ApiClient;
import com.piyushrai.upcovid19.Retrofit.ApiInterface;
import com.piyushrai.upcovid19.Retrofit.ApiResponse;
import com.piyushrai.upcovid19.Retrofit.Apitransaction;
import com.piyushrai.upcovid19.Retrofit.SetLogin;
import com.piyushrai.upcovid19.SetList;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class CaseList extends AppCompatActivity {
    private static final String TAG = "CaseList";
    private RecyclerView m_recycler;
    private MySharePrefrence sharedPreference;
    private ApiInterface apiInterface;
    private String sth,as;
    private Location mCurrentLocation;
    private ImageView logout;
    private EditText mobile,district;
    private ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);
        logout = findViewById(R.id.logout);
        mobile  = findViewById(R.id.mobile);
        district  = findViewById(R.id.district);
        search = findViewById(R.id.search);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CaseList.this, LoginActivity.class));
            }
        });
        apiInterface = ApiClient.getInstance(this).create(ApiInterface.class);
        sharedPreference = MySharePrefrence.getsharedprefInstance(this);
        m_recycler = findViewById(R.id.recyclers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        m_recycler.setLayoutManager(mLayoutManager);
        hitApi();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobile.getText().toString().isEmpty() || district.getText().toString().isEmpty()){
                    Toast.makeText(CaseList.this, "Fill Required Details To Search", Toast.LENGTH_SHORT).show();
                } else {
//                    hitApi();
                }


            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();


        hitApi();
    }


    private void hitApi()
    {
        try {
            if(mCurrentLocation == null){
                if (InternetConnection.isConnected(getApplicationContext()))
                {
                    retrofit2.Call request = apiInterface.setlist(new SetList(
                           sharedPreference.getUserDetails().getApi_token(),
                            sharedPreference.getUserDetails().getUsername().trim()
//                            mobile.getText().toString().trim(),
//                            district.getText().toString().trim()
                    ));

                    Apitransaction.startNetworkService(request, getCaseListResponse);
//             mdialog = ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...", true);
//            dialog.setMessage("Please wait.....");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//            dialog.show();
                }else {
                    Toast.makeText(this, "Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Getting Location Please Wait", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ApiResponse getCaseListResponse = new ApiResponse() {
        @Override
        public void OnResponseAPI(Object object) {
            Log.d(TAG, "OnResponseAPI: "+ new Gson().toJson(object));
            try {
                Log.d(TAG, "OnResponseAPI: " + new Gson().toJson(object));
                JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
                if (jsonObject.getBoolean("flag")) {
                    Type type = new TypeToken<List<CaseModel>>() {
                    }.getType();
                    List<CaseModel> list = new Gson().fromJson(jsonObject.getJSONArray("cases").toString(), type);
                    CaseAdapter adapter = new CaseAdapter(CaseList.this, list);
                    m_recycler.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                dialog.dismiss();
            }
        }

        @Override
        public void OnErrorAPI(String error) {
            Log.d(TAG, "OnErrorAPI: " + error);
            Toast.makeText(CaseList.this, error, Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
        }
    };


}
