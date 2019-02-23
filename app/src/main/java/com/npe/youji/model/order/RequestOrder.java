package com.npe.youji.model.order;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestOrder {
    int customers_id;
    int total;
    int discount;
    int grand_total;
    String shipping_date;
    int status;
    int districs;
    String ordering_user;
    ArrayList<JSONObject> order_detail;
    String ordering_email;

    public RequestOrder() {
    }

    public RequestOrder(int customers_id, int total, int discount, int grand_total, String shipping_date, int status, int districs, String ordering_user, ArrayList<JSONObject> order_detail, String ordering_email) {
        this.customers_id = customers_id;
        this.total = total;
        this.discount = discount;
        this.grand_total = grand_total;
        this.shipping_date = shipping_date;
        this.status = status;
        this.districs = districs;
        this.ordering_user = ordering_user;
        this.order_detail = order_detail;
        this.ordering_email = ordering_email;
    }

    public int getCustomers_id() {
        return customers_id;
    }

    public void setCustomers_id(int customers_id) {
        this.customers_id = customers_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(int grand_total) {
        this.grand_total = grand_total;
    }

    public String getShipping_date() {
        return shipping_date;
    }

    public void setShipping_date(String shipping_date) {
        this.shipping_date = shipping_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDistrics() {
        return districs;
    }

    public void setDistrics(int districs) {
        this.districs = districs;
    }

    public String getOrdering_user() {
        return ordering_user;
    }

    public void setOrdering_user(String ordering_user) {
        this.ordering_user = ordering_user;
    }

    public ArrayList<JSONObject> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(ArrayList<JSONObject> order_detail) {
        this.order_detail = order_detail;
    }

    public String getOrdering_email() {
        return ordering_email;
    }

    public void setOrdering_email(String ordering_email) {
        this.ordering_email = ordering_email;
    }
}
