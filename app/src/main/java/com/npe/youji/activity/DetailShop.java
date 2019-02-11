package com.npe.youji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.DataShopItemModel;


public class DetailShop extends AppCompatActivity {
    private CartOperations cartOperations;
    private CartModel cartModel;
    private Button btnAddtoCart;
    private Button btnCheckout;
    private ImageButton btnAdd, btnMinus;
    private TextView textQuantity;
    private LinearLayout layoutCart;
    private long stok;
    private long id;
    private String jsonString;
    private Gson gson;
    private DataShopItemModel dataItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inisialisasi
        btnAddtoCart = findViewById(R.id.btn_addToCart_detailItem);
        btnCheckout = findViewById(R.id.btn_checkout);
        layoutCart = findViewById(R.id.layout_addToCart_detailItem);
        btnAdd = findViewById(R.id.btn_addCart_detailItem);
        btnMinus = findViewById(R.id.btn_minusCart_detailItem);
        textQuantity = findViewById(R.id.tv_jumlahBarang_detailItem);

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayoutCart();
            }
        });

        Bundle extra = getIntent().getExtras();
        cartOperations = new CartOperations(getApplicationContext());
        if (extra != null) {
            /*try {
                id = extra.getLong("IDPRODUCT");
                Log.d("IDPRODUCT", String.valueOf(id));
                cartOperations.openDb();
                cartModel = cartOperations.getCart(id);
                Log.d("SQL GET", String.valueOf(cartModel));
                stok = cartModel.getStokProduct();
                cartOperations.closeDb();
                Log.d("SQL GET", "SUCCESS");
            } catch (SQLException e) {
                Log.d("SQL ERROR", "ERROR GET");
            }*/
            jsonString = extra.getString("DATA");
            gson = new Gson();
            dataItem = gson.fromJson(jsonString, DataShopItemModel.class);
        }


    }

    private void showLayoutCart() {
        btnAddtoCart.setVisibility(View.GONE);
        layoutCart.setVisibility(View.VISIBLE);
        btnCheckout.setVisibility(View.VISIBLE);
        if (layoutCart.getVisibility() == View.VISIBLE) {

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
