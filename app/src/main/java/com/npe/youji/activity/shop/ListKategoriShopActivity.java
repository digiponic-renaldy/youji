package com.npe.youji.activity.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.JsonObject;
import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.shop.menu.RootFilterProdukModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListKategoriShopActivity extends AppCompatActivity {
    RecyclerView rvListFilter;
    String kategori;
    Retrofit retrofit_local;
    ApiService service_local;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_shop);
        //inisialisasi
        rvListFilter = findViewById(R.id.rvListKategori);
        //retorgfit
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);

        Bundle extra = getIntent().getExtras();
        if(extra!= null){
            kategori = extra.getString("KATEGORI");
            getDataFilter(kategori);
        }

        Log.i("KategoriList", kategori);
    }

    private void getDataFilter(String kategori) {
        JsonObject requet = new JsonObject();
        requet.addProperty("kategori", kategori);
        service_local.listDetailFilterProduk(requet).enqueue(new Callback<List<RootFilterProdukModel>>() {
            @Override
            public void onResponse(Call<List<RootFilterProdukModel>> call, Response<List<RootFilterProdukModel>> response) {
                List<RootFilterProdukModel> data = response.body();
                if(data != null){
                    Log.i("DataFilter", String.valueOf(data));
                    insertAllData(data);
                }
            }

            @Override
            public void onFailure(Call<List<RootFilterProdukModel>> call, Throwable t) {
                Log.i("ErrorGetFilterData",t.getMessage());
            }
        });
    }

    private void insertAllData(List<RootFilterProdukModel> data) {
    }


}
