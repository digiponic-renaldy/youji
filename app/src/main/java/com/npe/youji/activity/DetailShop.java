package com.npe.youji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.npe.youji.R;

public class DetailShop extends AppCompatActivity {
    private DatabaseHelper db;
    private long idProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHelper(this);
        Bundle getExtra = new Bundle();
        if(getExtra != null){
            idProduct = getExtra.getInt("ID_PRODUCT");
        }
        Log.d("DATA SQL", db.getCart(idProduct).toString());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
