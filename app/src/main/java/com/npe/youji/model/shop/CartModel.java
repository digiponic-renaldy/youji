package com.npe.youji.model.shop;

public class CartModel {
    public static final String TABLE_NAME = "carts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT_ID = "idProduct";
    public static final String COLUMN_PRODUCT_NAME = "nameProduct";

    private int id;
    private int idProduct;
    private String nameProduct;

    //create table sql query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PRODUCT_ID + " INTEGER,"
                    + COLUMN_PRODUCT_NAME + "TEXT"
                    + ")";
    public CartModel() {
    }

    public CartModel(int id, int idProduct, String nameProduct) {
        this.id = id;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
}
