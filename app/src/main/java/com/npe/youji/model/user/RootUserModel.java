package com.npe.youji.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootUserModel {
    @SerializedName("api_status")
    int api_status;
    @SerializedName("api_message")
    String api_message;
    @SerializedName("api_authorization")
    String api_authorization;
    @SerializedName("data")
    List<DataUserModel> data;

    public RootUserModel() {
    }

    public RootUserModel(int api_status, String api_message, String api_authorization, List<DataUserModel> data) {
        this.api_status = api_status;
        this.api_message = api_message;
        this.api_authorization = api_authorization;
        this.data = data;
    }

    public int getApi_status() {
        return api_status;
    }

    public void setApi_status(int api_status) {
        this.api_status = api_status;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    public String getApi_authorization() {
        return api_authorization;
    }

    public void setApi_authorization(String api_authorization) {
        this.api_authorization = api_authorization;
    }

    public List<DataUserModel> getData() {
        return data;
    }

    public void setData(List<DataUserModel> data) {
        this.data = data;
    }
}
