package com.piyushrai.upcovid19.Retrofit;



import com.piyushrai.upcovid19.SetList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {


    @Headers("Content-Type: application/json")
    @POST("login")
    Call<Object> login(@Body SetLogin login);

    @Headers("Content-Type: application/json")
    @POST("assigned-cases")
    Call<Object> setlist(@Body SetList setlist);

    @Headers("Content-Type: application/json")
    @POST("case-details")
    Call<Object> setcasedetails(@Body Casedetails setcasedetails);

    @Headers("Content-Type: application/json")
    @POST("save-case-details")
    Call<Object> setDetails(@Body SavemDetail setDetails);
}
