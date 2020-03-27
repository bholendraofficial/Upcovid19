package com.piyushrai.upcovid19.Retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Apitransaction {
    private static final String TAG = "APITransaction";

    public static void startNetworkService(Call request, final ApiResponse apiResponse) {
        request.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));
                apiResponse.OnResponseAPI(response.body());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d("onResponse", "error :" + t.getMessage());
                apiResponse.OnErrorAPI(t.getMessage());
            }
        });
    }
}
