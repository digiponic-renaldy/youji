package com.npe.youji.model.shop;

public class DataShopModel {
    int idproduk;
    String cabang;
    String kode;
    String keterangan;
    String kategori;
    String jenis;
    String satuan;
    int stok;
    int harga;
    String gambar;
    String created_at;
    String updated_at;
    String deleted_at;

    public DataShopModel() {
    }


    public DataShopModel(int idproduk, String cabang, String kode,
                         String keterangan, String kategori,
                         String jenis, String satuan,
                         int stok, int harga,
                         String gambar, String created_at,
                         String updated_at, String deleted_at) {
        this.idproduk = idproduk;
        this.cabang = cabang;
        this.kode = kode;
        this.keterangan = keterangan;
        this.kategori = kategori;
        this.jenis = jenis;
        this.satuan = satuan;
        this.stok = stok;
        this.harga = harga;
        this.gambar = gambar;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public int getIdproduk() {
        return idproduk;
    }

    public void setIdproduk(int idproduk) {
        this.idproduk = idproduk;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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

    public String toString() {
        return "idProduct : " + getIdproduk() + "\n" +
                "Cabang Product : " + getCabang() + "\n" +
                "Kode Product :" + getKode() + "\n" +
                "Keterangan Product :" + getKeterangan() + "\n" +
                "Jenis Product :" + getJenis() + "\n" +
                "Satuan Product :" + getSatuan() + "\n" +
                "Stok Product : " + getStok() + "\n" +
                "Harga Product : " + getHarga() + "\n" +
                "Gambar Product : " + getGambar() + "\n" +
                "Created Product : " + getCreated_at() + "\n" +
                "Updated Product : " + getUpdated_at() + "\n" +
                "Deleted Product : " + getDeleted_at() + "\n";


    }
}
