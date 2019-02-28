package com.npe.youji.activity.shop;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.JsonObject;
import com.npe.youji.R;
import com.npe.youji.fragment.shop.AdapterShopItem;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.shop.RootProdukModel;

import java.util.ArrayList;
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
    ShopOperations shopOperations;
    AdapterShopItem adapterItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_shop);
        //inisialisasi
        rvListFilter = findViewById(R.id.rvListKategori);
        shopOperations = new ShopOperations(getApplicationContext());
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
        service_local.listDetailFilterProduk(requet).enqueue(new Callback<List<RootProdukModel>>() {
            @Override
            public void onResponse(Call<List<RootProdukModel>> call, Response<List<RootProdukModel>> response) {
                List<RootProdukModel> data = response.body();
                if(data != null){
                    Log.i("ResponSucc", "Berhasil");
                    insertAllDataShopLocal(data);
                    checkIsiSqlShop();
                    joinData();
                }
            }

            @Override
            public void onFailure(Call<List<RootProdukModel>> call, Throwable t) {
                Log.i("ErrorGetFilterProduk", t.getMessage());
            }
        });
    }
    private void joinData() {
        try {
            shopOperations.openDb();
            shopOperations.joinData();

            //insert to adapter
            listItemShop(shopOperations.joinData());
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.d("ERROR JOIN", e.getMessage());
        }
    }

    private void listItemShop(ArrayList<JoinModel> dataItem) {
        Log.d("LIST_DATA_PRODUCT", dataItem.toString());
        rvListFilter.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapterItem = new AdapterShopItem(getApplicationContext(), dataItem);
        rvListFilter.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterShopItem.OnItemClickListener() {
            @Override
            public void onItemCick(int position, JoinModel data) {
                adapterItem.detailItem(data);
            }
        });
    }

    private void checkIsiSqlShop() {
    }

    private void insertAllDataShopLocal(List<RootProdukModel> data) {
    }


}
