package com.npe.youji.model.inbox;

import com.google.gson.annotations.SerializedName;

public class RootInboxModel {
    @SerializedName("id")
    int id;
    @SerializedName("pesan")
    String pesan;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("subjek")
    String subjek;

    public RootInboxModel() {
    }

    public RootInboxModel(int id, String pesan, String created_at, String subjek) {
        this.id = id;
        this.pesan = pesan;
        this.created_at = created_at;
        this.subjek = subjek;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSubjek() {
        return subjek;
    }

    public void setSubjek(String subjek) {
        this.subjek = subjek;
    }
}
