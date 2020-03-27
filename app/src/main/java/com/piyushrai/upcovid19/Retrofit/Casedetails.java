package com.piyushrai.upcovid19.Retrofit;

public class Casedetails {
    private String api_token;
    private String case_id;

    public Casedetails(String api_token, String case_id) {
        this.api_token = api_token;
        this.case_id = case_id;
    }
}
