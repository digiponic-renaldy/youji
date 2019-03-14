package com.npe.youji.model.user;

public class UserModel {
    int id;
    String nama;
    String email;

    String fullname;
    String alamat;
    String notelp;

    public UserModel() {
    }


    public UserModel(String nama, String email, String fullname, String alamat, String notelp) {
        this.nama = nama;
        this.email = email;
        this.fullname = fullname;
        this.alamat = alamat;
        this.notelp = notelp;
    }

    public UserModel(int id, String nama, String email, String fullname, String alamat, String notelp) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.fullname = fullname;
        this.alamat = alamat;
        this.notelp = notelp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }
}
