package com.npe.youji.model.city;

import com.google.gson.annotations.SerializedName;

public class DataCitiesModel {
    @SerializedName("id")
    int id;
    @SerializedName("states_id")
    int states_id;
    @SerializedName("name")
    String name;

    public DataCitiesModel() {
    }

    public DataCitiesModel(int id, int states_id, String name) {
        this.id = id;
        this.states_id = states_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStates_id() {
        return states_id;
    }

    public void setStates_id(int states_id) {
        this.states_id = states_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
