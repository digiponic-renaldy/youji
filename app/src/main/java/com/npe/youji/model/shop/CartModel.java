package com.npe.youji.model.shop;

public class CartModel {
    private long idcart;
    private long idProduct;
    private String nameProduct;
    private long stokProduct;
    private long quantity;

    public CartModel() {
    }

    public CartModel(long idProduct, String nameProduct, long stokProduct, long quantity) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.stokProduct = stokProduct;
        this.quantity = quantity;
    }

    public CartModel(long idcart, long idProduct, String nameProduct, long stokProduct, long quantity) {
        this.idcart = idcart;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.stokProduct = stokProduct;
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getStokProduct() {
        return stokProduct;
    }

    public void setStokProduct(long stokProduct) {
        this.stokProduct = stokProduct;
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
                "Nama Product : "+getNameProduct() + "\n"+
                "Stok Product : "+getStokProduct() + "\n"+
                "Quantity : "+getQuantity();


    }
}
