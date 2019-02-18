package com.npe.youji.model.user;

public class RequestBodyUser {
    String nama;
    String email;

    public RequestBodyUser() {
    }

    public RequestBodyUser(String nama, String email) {
        this.nama = nama;
        this.email = email;
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
}
