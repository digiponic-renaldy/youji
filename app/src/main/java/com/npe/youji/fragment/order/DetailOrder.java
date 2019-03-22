package com.npe.youji.fragment.order;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.order.DataListDetailTransaksiModel;
import com.npe.youji.model.order.RootDetailTransaksiModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailOrder extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    String kode;
    Retrofit retrofit;
    ApiService service;
    RecyclerView rvbarang;
    TextView tvNamaUser, tvNomorTransaksi, tvStatus, tvHargaTransaksi, tvMetodeTransaksi;
    AdapterTransaksiDetail adapter;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    Locale locale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Detail");
        //insialisasi
        tvNamaUser = findViewById(R.id.tvNamaPembeliDetailTransaksi);
        tvNomorTransaksi = findViewById(R.id.tvNomorTransaksiDetailTransaksi);
        tvStatus = findViewById(R.id.tvStatusDetailTransaksi);
        tvHargaTransaksi = findViewById(R.id.tvHargaDetailTransaksi);
        tvMetodeTransaksi = findViewById(R.id.tvMetodeDetailTransaksi);
        swipeRefreshLayout = findViewById(R.id.swipeDetailOrder);
        rvbarang = findViewById(R.id.rvDetailTransaksi);
        locale = new Locale("in", "ID");
        //retrofit
        initRetrofit();
        //dialog
        dialogWait();
        //swipe
        swipeRefreshLayout.setOnRefreshListener(this);
        //get kode
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            kode = extra.getString("KODE");
//            Toast.makeText(getApplicationContext(), extra.getString("KODE"), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataOrderDetail(kode);
                }
            }, 2000);
        }


    }

    private void dialogWait() {
        progressDialog = new ProgressDialog(this, R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    private void getDataOrderDetail(String kode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("kode", kode);
        service.getDetailTransaksi(jsonObject).enqueue(new Callback<List<RootDetailTransaksiModel>>() {
            @Override
            public void onResponse(Call<List<RootDetailTransaksiModel>> call, Response<List<RootDetailTransaksiModel>> response) {
                List<RootDetailTransaksiModel> data = response.body();
                if (data != null) {
                    initDataDetail(data);
                }
            }

            @Override
            public void onFailure(Call<List<RootDetailTransaksiModel>> call, Throwable t) {
                Log.i("ErrorGetData", t.getMessage());
            }
        });
    }

    private void initDataDetail(List<RootDetailTransaksiModel> data) {
        tvNamaUser.setText(String.valueOf(data.get(0).getCustomer()));
        tvNomorTransaksi.setText(String.valueOf(data.get(0).getKode()));
        //format harga
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setMaximumFractionDigits(3);
        tvHargaTransaksi.setText(String.valueOf(numberFormat.format(data.get(0).getGrand_total())));
        tvMetodeTransaksi.setText(String.valueOf(data.get(0).getMetode_pembayaran()));
        tvStatus.setText(String.valueOf(data.get(0).getStatus()));

        listBarang(data.get(0).getPenjualan_detail());
    }

    private void listBarang(List<DataListDetailTransaksiModel> penjualan_detail) {
        rvbarang.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //adapter
        adapter = new AdapterTransaksiDetail(getApplicationContext(), penjualan_detail);
        rvbarang.setAdapter(adapter);

        //dismiss progress dialog
        progressDialog.dismiss();
        //swipe dismiss
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initRetrofit() {
        retrofit = NetworkClient.getRetrofitClientLocal();
        service = retrofit.create(ApiService.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initRetrofit();
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            kode = extra.getString("KODE");
//            Toast.makeText(getApplicationContext(), extra.getString("KODE"), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataOrderDetail(kode);
                }
            }, 2000);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        onResume();
    }
}
