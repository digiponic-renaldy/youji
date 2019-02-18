package com.npe.youji.model.user;

public class RequestBodyUser {
    String name;
    String email;

    public RequestBodyUser() {
    }

    public RequestBodyUser(String nama, String email) {
        this.name = nama;
        this.email = email;
    }

    public String getNama() {
        return name;
    }

    public void setNama(String nama) {
        this.name = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
