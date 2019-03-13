package com.npe.youji.model.user;

import com.google.gson.annotations.SerializedName;

public class RootPerbaruiUser {
    @SerializedName("status")
    int status;
    @SerializedName("message")
    String message;

    public RootPerbaruiUser() {
    }

    public RootPerbaruiUser(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
