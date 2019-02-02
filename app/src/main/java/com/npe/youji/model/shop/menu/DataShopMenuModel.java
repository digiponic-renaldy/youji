package com.npe.youji.model.shop.menu;

import com.google.gson.annotations.SerializedName;

public class DataShopMenuModel {
    @SerializedName("id")
    public int id;
    @SerializedName("categories_id")
    public int categories_id;
    @SerializedName("suppliers_id")
    public String suppliers_id;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("weight")
    public int weight;
    @SerializedName("buy_price")
    public String buy_price;
    @SerializedName("sell_price")
    public int sell_price;
    @SerializedName("stock")
    public int stock;
    @SerializedName("brands_id")
    public  int brands_id;
    @SerializedName("sku")
    public String sku;
    @SerializedName("image")
    public String image;
    @SerializedName("categories")
    public String categories;
    @SerializedName("brands")
    public String brands;

    public DataShopMenuModel() {
    }

    public DataShopMenuModel(int id, int categories_id, String suppliers_id, String name, String description, int weight, String buy_price, int sell_price, int stock, int brands_id, String sku, String image, String categories, String brands) {
        this.id = id;
        this.categories_id = categories_id;
        this.suppliers_id = suppliers_id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.stock = stock;
        this.brands_id = brands_id;
        this.sku = sku;
        this.image = image;
        this.categories = categories;
        this.brands = brands;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(int categories_id) {
        this.categories_id = categories_id;
    }

    public String getSuppliers_id() {
        return suppliers_id;
    }

    public void setSuppliers_id(String suppliers_id) {
        this.suppliers_id = suppliers_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public int getSell_price() {
        return sell_price;
    }

    public void setSell_price(int sell_price) {
        this.sell_price = sell_price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getBrands_id() {
        return brands_id;
    }

    public void setBrands_id(int brands_id) {
        this.brands_id = brands_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }
}
