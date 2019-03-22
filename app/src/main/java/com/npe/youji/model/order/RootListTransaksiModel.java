package com.npe.youji.model.order;

import com.google.gson.annotations.SerializedName;

public class RootListTransaksiModel{
    @SerializedName("id")
    int id;
    @SerializedName("kode")
    String kode;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("tanggal")
    String tanggal;
    @SerializedName("subtotal")
    int subtotal;
    @SerializedName("pajak")
    int pajak;
    @SerializedName("diskon_tipe")
    String diskon_tipe;
    @SerializedName("diskon")
    int diskon;
    @SerializedName("grand_total")
    int grand_total;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;
    @SerializedName("deleted_at")
    String deleted_at;
    @SerializedName("users_id")
    String users_id;
    @SerializedName("customers_id")
    int customers_id;
    @SerializedName("status")
    String status;
    @SerializedName("metode_pembayaran")
    String metode_pembayaran;
    @SerializedName("id_cabang")
    String id_cabang;
    @SerializedName("platfrom")
    String platfrom;
    @SerializedName("status_gambar")
    String status_gambar;

    public RootListTransaksiModel() {
    }

    public RootListTransaksiModel(int id, String kode, String keterangan, String tanggal, int subtotal, int pajak, String diskon_tipe, int diskon, int grand_total, String created_at, String updated_at, String deleted_at, String users_id, int customers_id, String status, String metode_pembayaran, String id_cabang, String platfrom, String status_gambar) {
        this.id = id;
        this.kode = kode;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.subtotal = subtotal;
        this.pajak = pajak;
        this.diskon_tipe = diskon_tipe;
        this.diskon = diskon;
        this.grand_total = grand_total;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.users_id = users_id;
        this.customers_id = customers_id;
        this.status = status;
        this.metode_pembayaran = metode_pembayaran;
        this.id_cabang = id_cabang;
        this.platfrom = platfrom;
        this.status_gambar = status_gambar;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public String getId_cabang() {
        return id_cabang;
    }

    public void setId_cabang(String id_cabang) {
        this.id_cabang = id_cabang;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }

    public String getStatus_gambar() {
        return status_gambar;
    }

    public void setStatus_gambar(String status_gambar) {
        this.status_gambar = status_gambar;
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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getPajak() {
        return pajak;
    }

    public void setPajak(int pajak) {
        this.pajak = pajak;
    }

    public String getDiskon_tipe() {
        return diskon_tipe;
    }

    public void setDiskon_tipe(String diskon_tipe) {
        this.diskon_tipe = diskon_tipe;
    }

    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
    }

    public int getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(int grand_total) {
        this.grand_total = grand_total;
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

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }

    public int getCustomers_id() {
        return customers_id;
    }

    public void setCustomers_id(int customers_id) {
        this.customers_id = customers_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
