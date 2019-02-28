package com.npe.youji.activity.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.npe.youji.R;

public class ListKategoriShopActivity extends AppCompatActivity {
    RecyclerView rvListFilter;
    String kategori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_shop);
        //inisialisasi
        rvListFilter = findViewById(R.id.rvListKategori);


        Bundle extra = getIntent().getExtras();
        if(extra!= null){
            kategori = extra.getString("KATEGORI");
        }

        Log.i("KategoriList", kategori);
    }


}
