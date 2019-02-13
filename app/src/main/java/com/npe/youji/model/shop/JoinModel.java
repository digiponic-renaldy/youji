package com.npe.youji.model.shop;

public class JoinModel {
    public int idproduk;
    public String name;
    public int sell_price;
    public String image;
    public int stock;
    public int quantity;

    public JoinModel() {
    }

    public JoinModel(int idproduk, String name, int sell_price, String image, int stock, int quantity) {
        this.idproduk = idproduk;
        this.name = name;
        this.sell_price = sell_price;
        this.image = image;
        this.stock = stock;
        this.quantity = quantity;
    }

    public int getIdproduk() {
        return idproduk;
    }

    public void setIdproduk(int idproduk) {
        this.idproduk = idproduk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSell_price() {
        return sell_price;
    }

    public void setSell_price(int sell_price) {
        this.sell_price = sell_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
