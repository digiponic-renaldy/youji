package com.npe.youji.model.api;

import com.npe.youji.model.city.RootCitiesModel;
import com.npe.youji.model.city.RootDistrikModel;
import com.npe.youji.model.shop.RootShopItemModel;
import com.npe.youji.model.shop.menu.RootCategoryModel;
import com.npe.youji.model.shop.menu.RootShopMenuModel;
import com.npe.youji.model.user.RequestBodyUser;
import com.npe.youji.model.user.RootUserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("products")
    Call<RootShopItemModel> listProduct();

    @GET("product_category")
    Call<RootShopMenuModel> listMenuProduct(@Query("categories_id") int categorys_id);

    @GET("categories")
    Call<RootCategoryModel> listCategory();

    @POST("customers")
    Call<RootUserModel> apiUser(@Body RequestBodyUser request);

    @GET("cities")
    Call<RootCitiesModel> listCity();

    @GET("districs")
    Call<RootDistrikModel> listDistrik(@Query("cities_id") int states_id);
}
