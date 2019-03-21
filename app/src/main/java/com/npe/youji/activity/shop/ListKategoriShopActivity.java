package com.npe.youji.activity.shop;

import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.JsonObject;
import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.DataShopModel;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.shop.RootProdukFilter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListKategoriShopActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rvListFilter;
    String kategori;
    Retrofit retrofit_local;
    ApiService service_local;
    ShopOperations shopOperations;
    AdapterFilterProduk adapterItem;
    ShimmerRecyclerView shimmerRecyclerShopFilter;
    TextView tvKategoriNull;
    SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = findViewById(R.id.swipeMainListKategori);
        //swipe
        swipeRefreshLayout.setOnRefreshListener(this);
        //shimmer
        shimmerBehavior();
        //retorgfit
        initRetrofit();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            kategori = extra.getString("KATEGORI");

            Log.i("Kategori", kategori);

            getSupportActionBar().setTitle(kategori);
            getDataFilter(kategori);
        }

    }

    private void initRetrofit() {
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);
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

        service_local.listDetailFilterProduk(requet).enqueue(new Callback<List<RootProdukFilter>>() {
            @Override
            public void onResponse(Call<List<RootProdukFilter>> call, Response<List<RootProdukFilter>> response) {
                List<RootProdukFilter> data = response.body();
                if (data != null) {
                    Log.i("ResponSucc", "Berhasil");
                    insertAllDataShopLocal(data);
                    if (checkIsiSqlShop()) {
                        joinData();
                        shimmerRecyclerShopFilter.hideShimmerAdapter();
                    }
                }
                if (data.isEmpty()) {
                    Log.i("DataNullKategori", kategori);
                    tvKategoriNull.setVisibility(View.VISIBLE);
                    tvKategoriNull.setText("Barang dengan Kategori " + kategori + " \n tidak tersedia");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<RootProdukFilter>> call, Throwable t) {
                Log.i("ErrorGetFilterProduk", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
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
        //hide swipe
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean checkIsiSqlShop() {
        boolean valid = false;
        try {
            shopOperations.openDb();
            valid = shopOperations.checkRecordShop();
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("CheckErrorAll", e.getMessage());
        }
        return valid;
    }

    private void insertAllDataShopLocal(List<RootProdukFilter> dataItem) {
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
                    dataItem.get(i).getDeskripsi(),
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
    public boolean onSupportNavigateUp() {
        adapterItem.clear();
        truncateShop();
        onBackPressed();
        adapterItem.notifyDataSetChanged();
        return true;
    }

    private void truncateShop() {
        try {
            shopOperations.openDb();
            shopOperations.deleteRecord();
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("ErrorTruncateFilter", e.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRetrofit();
                Bundle extra = getIntent().getExtras();
                if (extra != null) {
                    kategori = extra.getString("KATEGORI");

                    Log.i("Kategori", kategori);

                    getSupportActionBar().setTitle(kategori);
                    getDataFilter(kategori);
                }
            }
        }, 2000);
    }
}
