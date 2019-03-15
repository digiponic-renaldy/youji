package com.npe.youji.activity.shop;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
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

public class ShopAllItem extends AppCompatActivity {
    RecyclerView recyclerView;
    ShimmerRecyclerView shimmerRecyclerView;
    AdapterFilterProduk adapterItem;
    private Retrofit retrofit_local;
    private ShopOperations shopOperations;
    private ApiService service_local;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_all_item);
        getSupportActionBar().setTitle("Rekomendasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inisialisasi
        recyclerView = findViewById(R.id.recycler_allItem_activity);
        shimmerRecyclerView = findViewById(R.id.shimmer_shopAllitem_activity);
        shopOperations = new ShopOperations(getApplicationContext());
        shimmerBehavior();

        initRetrofit();
        getItemProduk_local();
    }

    private void initRetrofit() {
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);
    }

    private void getItemProduk_local() {
        service_local.listProduk().enqueue(new Callback<List<RootProdukModel>>() {
            @Override
            public void onResponse(Call<List<RootProdukModel>> call, Response<List<RootProdukModel>> response) {
                List<RootProdukModel> data = response.body();
                if (data != null) {
                    Log.i("ResponSucc", "Berhasil");
                    joinData();
                    shimmerRecyclerView.hideShimmerAdapter();
                }
            }

            @Override
            public void onFailure(Call<List<RootProdukModel>> call, Throwable t) {
                Log.i("ResponseError", t.getMessage());
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

    private void listItemShop(ArrayList<JoinModel> joinModels) {
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapterItem = new AdapterFilterProduk(getApplicationContext(), joinModels);
        recyclerView.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterFilterProduk.OnItemClickListener() {
            @Override
            public void onItemCickFilter(int position, JoinModel data) {
                adapterItem.detailItem(data);
            }
        });
    }

    private void shimmerBehavior() {
        shimmerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        shimmerRecyclerView.setAdapter(adapterItem);
        shimmerRecyclerView.showShimmerAdapter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        adapterItem.clear();

        onBackPressed();
        return true;
    }
}
