package com.npe.youji.activity.shop;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.JsonObject;
import com.npe.youji.MainActivity;
import com.npe.youji.R;
import com.npe.youji.fragment.shop.AdapterShopItem;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.DataShopModel;
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
    AdapterFilterProduk adapterItem;
    ShimmerRecyclerView shimmerRecyclerShopFilter;
    TextView tvKategoriNull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_shop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inisialisasi
        rvListFilter = findViewById(R.id.rvListKategori);
        tvKategoriNull = findViewById(R.id.tvKetegoriNull);
        shimmerRecyclerShopFilter = findViewById(R.id.shimmer_shopFilter);
        shopOperations = new ShopOperations(getApplicationContext());

        //shimmer
        shimmerBehavior();
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

    private void shimmerBehavior() {
        shimmerRecyclerShopFilter.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        shimmerRecyclerShopFilter.setAdapter(adapterItem);
        shimmerRecyclerShopFilter.showShimmerAdapter();
    }

    private void getDataFilter(final String kategori) {
        shimmerRecyclerShopFilter.showShimmerAdapter();

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
                    shimmerRecyclerShopFilter.hideShimmerAdapter();
                }
                if(data.isEmpty()) {
                    Log.i("DataNullKategori", kategori);
                    tvKategoriNull.setVisibility(View.VISIBLE);
                    tvKategoriNull.setText("Barang dengan Kategori "+kategori + " \n tidak tersedia");
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
        adapterItem = new AdapterFilterProduk(getApplicationContext(), dataItem);
        rvListFilter.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterFilterProduk.OnItemClickListener() {

            @Override
            public void onItemCickFilter(int position, JoinModel data) {
                adapterItem.detailItem(data);
            }
        });
    }

    private void checkIsiSqlShop() {
        try {
            shopOperations.openDb();
            shopOperations.getAllShop();
            Log.i("CheckAllDataShop", String.valueOf(shopOperations.getAllShop()));
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("CheckErrorAll", e.getMessage());
        }
    }

    private void insertAllDataShopLocal(List<RootProdukModel> dataItem) {
        String imgUrl = "https://i.imgur.com/kTRJDky.png";
        for (int i = 0; i < dataItem.size(); i++) {
            if (dataItem.get(i).getGambar() == null) {
                dataItem.get(i).setGambar(imgUrl);
            }
            Log.i("DataItemSize", String.valueOf(dataItem.size()));

            Log.i("DataProduk", String.valueOf(dataItem.get(i).getStok()));
            DataShopModel data = new DataShopModel(
                    dataItem.get(i).getId(),
                    dataItem.get(i).getCabang(),
                    dataItem.get(i).getKode(),
                    dataItem.get(i).getKeterangan(),
                    dataItem.get(i).getKategori(),
                    dataItem.get(i).getJenis(),
                    dataItem.get(i).getSatuan(),
                    dataItem.get(i).getStok(),
                    dataItem.get(i).getHarga(),
                    dataItem.get(i).getGambar(),
                    dataItem.get(i).getCreated_at(),
                    dataItem.get(i).getUpdated_at(),
                    dataItem.get(i).getDeleted_at());
            try {
                shopOperations.openDb();
                shopOperations.insertShop(data);
                Log.i("DataNamaProdukInsert", data.getKeterangan());
                shopOperations.closeDb();
                Log.i("INSERTSQL", "SUCCESS");
            } catch (SQLException e) {
                Log.i("ERRORINSERT", e.getMessage() + " ERROR");
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                toMain();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        truncateShop();
        startActivity(intent);
    }

    private void truncateShop() {
        try{
            shopOperations.openDb();
            shopOperations.deleteRecord();
            shopOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorTruncateFilter", e.getMessage());
        }
    }
}
