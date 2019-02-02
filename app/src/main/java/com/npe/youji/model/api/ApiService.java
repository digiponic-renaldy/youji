package com.npe.youji.model.api;

import com.npe.youji.model.RootShopItemModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products")
    Call<RootShopItemModel> listProduct();
}
