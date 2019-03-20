package com.npe.youji.activity.shop;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.npe.youji.R;
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

public class ListNonKategoryShopActivity extends AppCompatActivity {
    RecyclerView rvListAll;
    Retrofit retrofit_local;
    ApiService service_local;
    ShopOperations shopOperations;
    AdapterNonFilterProduk adapter;
    ShimmerRecyclerView shimmerRecyclerView;
    TextView tvNull;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_non_kategory_shop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //inisialisasi
        rvListAll = findViewById(R.id.rvListNonKategori);
        tvNull = findViewById(R.id.tvStokNullListDetailNonFilter);
        shimmerRecyclerView = findViewById(R.id.shimmer_shopFilterNonKategori);
        shopOperations = new ShopOperations(getApplicationContext());
        //shimmer
        shimmerBehavior();
        //retrofit
        initRetrofit();
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            title = extra.getString("TITLE");
            getSupportActionBar().setTitle(title);
            getDataProduk();
        }
    }

    private void getDataProduk() {
        shimmerRecyclerView.showShimmerAdapter();

        service_local.listProduk().enqueue(new Callback<List<RootProdukModel>>() {
            @Override
            public void onResponse(Call<List<RootProdukModel>> call, Response<List<RootProdukModel>> response) {
                List<RootProdukModel> data = response.body();
                if (data != null) {
                    Log.i("ResponSucc", "Berhasil");
                    insertAllDataShopLocal(data);
                    if (checkIsiSqlShop()) {
                        joinData();
                        shimmerRecyclerView.hideShimmerAdapter();
                    }
                }
                if (data.isEmpty()) {
                    tvNull.setVisibility(View.VISIBLE);
                    tvNull.setText("Barang tidak tersedia");
                }
            }

            @Override
            public void onFailure(Call<List<RootProdukModel>> call, Throwable t) {
                Log.i("ErrorGetDataProduk", t.getMessage());
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
        rvListAll.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapter = new AdapterNonFilterProduk(getApplicationContext(), dataItem);
        rvListAll.setAdapter(adapter);
        adapter.setOnClickListener(new AdapterNonFilterProduk.OnItemClickListener() {
            @Override
            public void onItemClick(int position, JoinModel data) {
                adapter.detailItem(data);
            }
        });
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

    private void initRetrofit() {
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);
    }

    private void shimmerBehavior() {
        shimmerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        shimmerRecyclerView.setAdapter(adapter);
        shimmerRecyclerView.showShimmerAdapter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        adapter.clear();
        truncateShop();
        onBackPressed();
        adapter.notifyDataSetChanged();
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
}
