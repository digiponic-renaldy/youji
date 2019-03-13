package com.npe.youji.model.api;

import com.google.gson.JsonObject;
import com.npe.youji.model.city.RootCitiesModel;
import com.npe.youji.model.city.RootDistrikModel;
import com.npe.youji.model.order.RootDetailTransaksiModel;
import com.npe.youji.model.order.RootListTransaksiModel;
import com.npe.youji.model.shop.RootProdukModel;
import com.npe.youji.model.shop.menu.RootDetailProdukModel;
import com.npe.youji.model.shop.menu.RootTipeKategoriModel;
import com.npe.youji.model.user.RequestBodyUser;
import com.npe.youji.model.user.RootPelangganModel;
import com.npe.youji.model.user.RootPerbaruiUser;
import com.npe.youji.model.user.RootUserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
//    @GET("products")
//    Call<RootShopItemModel> listProduct();

//    @GET("product_category")
//    Call<RootShopMenuModel> listMenuProduct(@Query("categories_id") int categorys_id);

//    @GET("categories")
//    Call<RootCategoryModel> listCategory();

    @POST("customers")
    Call<RootUserModel> apiUser(@Body RequestBodyUser request);

    @GET("cities")
    Call<RootCitiesModel> listCity();

    @GET("districs")
    Call<RootDistrikModel> listDistrik(@Query("cities_id") int states_id);

//    @POST("orders")
//    Call<RootOrderModel> sendOrder(@Body RequestOrder requestOrder);

    @GET("produk")
    Call<List<RootProdukModel>> listProduk();

    @POST("pelanggan")
    Call<List<RootPelangganModel>> sendPelanggan(@Body JsonObject jsonObject);

    @GET("tipe/detil/kategori")
    Call<List<RootTipeKategoriModel>> listCategory();

    @POST("produk/filter")
    Call<List<RootProdukModel>> listDetailFilterProduk(@Body JsonObject jsonObject);

    @POST("transaksi/data")
    Call<List<RootListTransaksiModel>> listTransaksiUser(@Body JsonObject jsonObject);

    @POST("transaksi/data/detil")
    Call<List<RootDetailTransaksiModel>> getDetailTransaksi(@Body JsonObject jsonObject);

    @POST("pelanggan/perbarui")
    Call<RootPerbaruiUser> updateUser(@Body JsonObject jsonObject);
}
