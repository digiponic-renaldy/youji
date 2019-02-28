package com.npe.youji.model.order;

import com.google.gson.annotations.SerializedName;

public class RootDetailTransaksiModel {
    @SerializedName("id")
    int id;
    @SerializedName("id_penjualan")
    int id_penjualan;
    @SerializedName("kode_penjualan")
    String kode_penjualan;
    @SerializedName("id_produk")
    int id_produk;
    @SerializedName("kode_produk")
    String kode_produk;
    @SerializedName("nama_produk")
    String nama_produk;
    @SerializedName("satuan")
    String satuan;
    @SerializedName("kuantitas")
    int kuantitas;
    @SerializedName("harga")
    int harga;
    @SerializedName("diskon")
    int diskon;
    @SerializedName("diskon_tipe")
    String diskon_tipe;
    @SerializedName("subtotal")
    int subtotal;
    @SerializedName("grand_total")
    int grand_total;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;
    @SerializedName("deleted_at")
    String deleted_at;

    public RootDetailTransaksiModel() {
    }

    public RootDetailTransaksiModel(int id, int id_penjualan, String kode_penjualan, int id_produk, String kode_produk, String nama_produk, String satuan, int kuantitas, int harga, int diskon, String diskon_tipe, int subtotal, int grand_total, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.id_penjualan = id_penjualan;
        this.kode_penjualan = kode_penjualan;
        this.id_produk = id_produk;
        this.kode_produk = kode_produk;
        this.nama_produk = nama_produk;
        this.satuan = satuan;
        this.kuantitas = kuantitas;
        this.harga = harga;
        this.diskon = diskon;
        this.diskon_tipe = diskon_tipe;
        this.subtotal = subtotal;
        this.grand_total = grand_total;
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

    public int getId_penjualan() {
        return id_penjualan;
    }

    public void setId_penjualan(int id_penjualan) {
        this.id_penjualan = id_penjualan;
    }

    public String getKode_penjualan() {
        return kode_penjualan;
    }

    public void setKode_penjualan(String kode_penjualan) {
        this.kode_penjualan = kode_penjualan;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public String getKode_produk() {
        return kode_produk;
    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
    }

    public String getDiskon_tipe() {
        return diskon_tipe;
    }

    public void setDiskon_tipe(String diskon_tipe) {
        this.diskon_tipe = diskon_tipe;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
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
}
