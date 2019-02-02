package com.npe.youji.model.shop;

import com.google.gson.annotations.SerializedName;

public class DataShopItemModel {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("weight")
    public int weight;
    @SerializedName("sell_price")
    public int sell_price;
    @SerializedName("stock")
    public int stock;
    @SerializedName("image")
    public String image;
    @SerializedName("category")
    public String category;
    @SerializedName("brands")
    public String brandsl;

    public DataShopItemModel() {
    }

    public DataShopItemModel(int id, String name, String description, int weight, int sell_price, int stock, String image, String category, String brandsl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.sell_price = sell_price;
        this.stock = stock;
        this.image = image;
        this.category = category;
        this.brandsl = brandsl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrandsl() {
        return brandsl;
    }

    public void setBrandsl(String brandsl) {
        this.brandsl = brandsl;
    }
}
