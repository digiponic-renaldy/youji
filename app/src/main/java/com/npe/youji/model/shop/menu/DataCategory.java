package com.npe.youji.model.shop.menu;

import com.google.gson.annotations.SerializedName;

public class DataCategory {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("image")
    public String image;

    public DataCategory() {
    }

    public DataCategory(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
