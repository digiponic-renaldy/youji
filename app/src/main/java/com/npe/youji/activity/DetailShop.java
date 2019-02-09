package com.npe.youji.activity;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.npe.youji.R;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;

public class DetailShop extends AppCompatActivity {
    private CartOperations cartOperations;
    private CartModel cartModel;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extra = getIntent().getExtras();
        cartOperations = new CartOperations(getApplicationContext());
        if (extra != null) {
            try {
                id = extra.getLong("IDPRODUCT");
                Log.d("IDPRODUCT", String.valueOf(id));
                cartOperations.openDb();
                cartModel = cartOperations.getCart(id);
                Log.d("SQL GET", String.valueOf(cartModel));
                cartOperations.closeDb();
                Log.d("SQL GET", "SUCCESS");
            } catch (SQLException e) {
                Log.d("SQL ERROR", "ERROR GET");
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
