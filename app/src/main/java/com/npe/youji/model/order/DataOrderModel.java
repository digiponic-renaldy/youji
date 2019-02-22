package com.npe.youji.model.order;

import com.google.gson.annotations.SerializedName;

public class DataOrderModel {
    @SerializedName("customers_id")
    String customers_id;
    @SerializedName("total")
    String total;
    @SerializedName("discount")
    String discount;
    @SerializedName("shipping_date")
    String shipping_date;
    @SerializedName("status")
    String status;
    @SerializedName("districs")
    String districs;
    @SerializedName("ordering_user")
    String ordering_user;
    @SerializedName("ordering_email")
    String ordering_email;
    @SerializedName("order_number")
    String order_number;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("id")
    int id;

    public DataOrderModel() {
    }

    public DataOrderModel(String customers_id, String total, String discount, String shipping_date, String status, String districs, String ordering_user, String ordering_email, String order_number, String created_at, int id) {
        this.customers_id = customers_id;
        this.total = total;
        this.discount = discount;
        this.shipping_date = shipping_date;
        this.status = status;
        this.districs = districs;
        this.ordering_user = ordering_user;
        this.ordering_email = ordering_email;
        this.order_number = order_number;
        this.created_at = created_at;
        this.id = id;
    }

    public String getCustomers_id() {
        return customers_id;
    }

    public void setCustomers_id(String customers_id) {
        this.customers_id = customers_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShipping_date() {
        return shipping_date;
    }

    public void setShipping_date(String shipping_date) {
        this.shipping_date = shipping_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistrics() {
        return districs;
    }

    public void setDistrics(String districs) {
        this.districs = districs;
    }

    public String getOrdering_user() {
        return ordering_user;
    }

    public void setOrdering_user(String ordering_user) {
        this.ordering_user = ordering_user;
    }

    public String getOrdering_email() {
        return ordering_email;
    }

    public void setOrdering_email(String ordering_email) {
        this.ordering_email = ordering_email;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
