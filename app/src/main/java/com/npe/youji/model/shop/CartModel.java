package com.npe.youji.model.shop;

public class CartModel {
    private int idProduct;
    private int quantity;

    public CartModel() {
    }

    public CartModel(int idProduct, int quantity) {
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
