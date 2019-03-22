package com.npe.youji.model.api;

import com.google.gson.JsonObject;
import com.npe.youji.model.city.RootCityModel;
import com.npe.youji.model.city.RootDistrikModel;
import com.npe.youji.model.inbox.RootInboxModel;
import com.npe.youji.model.order.RootDetailTransaksiModel;
import com.npe.youji.model.order.RootListTransaksiModel;
import com.npe.youji.model.shop.RootProdukFilter;
import com.npe.youji.model.shop.RootProdukModel;
import com.npe.youji.model.shop.menu.RootTipeKategoriModel;
import com.npe.youji.model.user.RootPelangganModel;
import com.npe.youji.model.user.RootPerbaruiUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("area")
    Call<List<RootCityModel>> listCity(@Body JsonObject jsonObject);

    @POST("area")
    Call<List<RootDistrikModel>> listDistrik(@Body JsonObject jsonObject);

    @GET("produk")
    Call<List<RootProdukModel>> listProduk();

    @POST("pelanggan")
    Call<List<RootPelangganModel>> sendPelanggan(@Body JsonObject jsonObject);

    @GET("tipe/detil/kategori")
    Call<List<RootTipeKategoriModel>> listCategory();

    @POST("produk/filter")
    Call<List<RootProdukFilter>> listDetailFilterProduk(@Body JsonObject jsonObject);

    @POST("transaksi/data")
    Call<List<RootListTransaksiModel>> listTransaksiUser(@Body JsonObject jsonObject);

    @POST("transaksi/data/detil")
    Call<List<RootDetailTransaksiModel>> getDetailTransaksi(@Body JsonObject jsonObject);

    @POST("pelanggan/perbarui")
    Call<RootPerbaruiUser> updateUser(@Body JsonObject jsonObject);

    @GET("pesan")
    Call<List<RootInboxModel>> getPesan();

}
