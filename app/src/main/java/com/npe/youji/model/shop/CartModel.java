package com.npe.youji.model.shop;

public class CartModel {
    private long idcart;
    private long idProduct;
    private String nameProduct;

    public CartModel() {
    }

    public CartModel(long idProduct, String nameProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
    }

    public CartModel(long idcart, long idProduct, String nameProduct) {
        this.idcart = idcart;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
    }


    public long getIdcart() {
        return idcart;
    }

    public void setIdcart(long idcart) {
        this.idcart = idcart;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String toString(){
        return "Cart id: "+getIdcart()+ "\n" +
                "idProduct : "+getIdProduct() + "\n" +
                "Nama Product : "+getNameProduct() + "\n";


    }
}
