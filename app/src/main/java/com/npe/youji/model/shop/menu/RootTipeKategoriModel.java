package com.npe.youji.model.shop.menu;

import com.google.gson.annotations.SerializedName;

public class RootTipeKategoriModel {
    @SerializedName("id")
    int id;
    @SerializedName("kode")
    String kode;
    @SerializedName("kode_tipe")
    int kode_tipe;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("gambar")
    String gambar;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;
    @SerializedName("deleted_at")
    String deleted_at;

    public RootTipeKategoriModel() {
    }

    public RootTipeKategoriModel(int id, String kode, int kode_tipe, String keterangan, String gambar, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.kode = kode;
        this.kode_tipe = kode_tipe;
        this.keterangan = keterangan;
        this.gambar = gambar;
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

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public int getKode_tipe() {
        return kode_tipe;
    }

    public void setKode_tipe(int kode_tipe) {
        this.kode_tipe = kode_tipe;
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

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
