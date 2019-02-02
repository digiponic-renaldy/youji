package com.npe.youji.model.shop.menu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootCategoryModel {
    @SerializedName("api_status")
    public int api_status;
    @SerializedName("api_message")
    public String api_message;
    @SerializedName("api_authorization")
    public String api_authorization;
    @SerializedName("data")
    public List<DataCategory> data;

    public RootCategoryModel() {
    }

    public RootCategoryModel(int api_status, String api_message, String api_authorization, List<DataCategory> data) {
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

    public List<DataCategory> getData() {
        return data;
    }

    public void setData(List<DataCategory> data) {
        this.data = data;
    }
}
