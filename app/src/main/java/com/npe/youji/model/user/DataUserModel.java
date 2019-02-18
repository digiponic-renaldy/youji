package com.npe.youji.model.user;

import com.google.gson.annotations.SerializedName;

public class DataUserModel {
    @SerializedName("id")
    int id;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("updated_at")
    String updated_at;
    @SerializedName("deleted_at")
    String deleted_at;
    @SerializedName("name")
    String name;
    @SerializedName("email")
    String email;
    @SerializedName("address")
    String address;
    @SerializedName("phone")
    String phone;
    @SerializedName("phone_other")
    String phone_other;
    @SerializedName("stated_id")
    String stated_id;
    @SerializedName("cities_id")
    String cities_id;
    @SerializedName("districs_id")
    String districs_id;

    public DataUserModel() {
    }

    public DataUserModel(int id, String created_at, String updated_at, String deleted_at, String name, String email, String address, String phone, String phone_other, String stated_id, String cities_id, String districs_id) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.phone_other = phone_other;
        this.stated_id = stated_id;
        this.cities_id = cities_id;
        this.districs_id = districs_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_other() {
        return phone_other;
    }

    public void setPhone_other(String phone_other) {
        this.phone_other = phone_other;
    }

    public String getStated_id() {
        return stated_id;
    }

    public void setStated_id(String stated_id) {
        this.stated_id = stated_id;
    }

    public String getCities_id() {
        return cities_id;
    }

    public void setCities_id(String cities_id) {
        this.cities_id = cities_id;
    }

    public String getDistrics_id() {
        return districs_id;
    }

    public void setDistrics_id(String districs_id) {
        this.districs_id = districs_id;
    }
}
