package com.piyushrai.upcovid19.Retrofit;

public class SavemDetail {
    private String api_token;
    private String case_id;
    private String user_id;
    private double lat;
    private double lng;
    private String date;
    private String survey_details;
    private String description;

    public SavemDetail(String api_token, String case_id, String user_id, double lat, double lng, String date, String survey_details, String description) {
        this.api_token = api_token;
        this.case_id = case_id;
        this.user_id = user_id;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
        this.survey_details = survey_details;
        this.description = description;
    }
}
