package com.piyushrai.upcovid19;

public class UserDetail {
    private String name,username,api_token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public UserDetail(String name, String username, String api_token) {
        this.name = name;
        this.username = username;
        this.api_token = api_token;
    }
}
