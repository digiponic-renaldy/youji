package com.npe.youji.model.city;

import com.google.gson.annotations.SerializedName;

public class RootDistrikModel {
    @SerializedName("id")
    int id;
    @SerializedName("kode_kota")
    int kode_kota;
    @SerializedName("kode")
    String kode;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;
    @SerializedName("deleted_at")
    String deleted_at;

    public RootDistrikModel() {
    }

    public RootDistrikModel(int id, int kode_kota, String kode, String keterangan, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.kode_kota = kode_kota;
        this.kode = kode;
        this.keterangan = keterangan;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKode_kota() {
        return kode_kota;
    }

    public void setKode_kota(int kode_kota) {
        this.kode_kota = kode_kota;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
