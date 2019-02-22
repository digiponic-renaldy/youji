package com.npe.youji.activity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.activity.checkout.CheckoutActivity;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.JoinModel;


public class DetailShop extends AppCompatActivity {
    private CartOperations cartOperations;
    private UserOperations userOperations;
    private CartModel cartModel;
    private Button btnAddtoCart;
    private Button btnCheckout;
    private ImageButton btnAdd, btnMinus;
    private TextView textQuantity;
    private LinearLayout layoutCart;
    private long stok;
    private long id;
    private int quantity = 0;
    private String jsonString;
    private Gson gson;
    private JoinModel dataItem;
    //show data
    private TextView namaBarang, hargaBarang, descBarang;
    private ImageView imgBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inisialisasi
        userOperations = new UserOperations(getApplicationContext());
        btnAddtoCart = findViewById(R.id.btn_addToCart_detailItem);
        btnCheckout = findViewById(R.id.btn_checkout);
        layoutCart = findViewById(R.id.layout_addToCart_detailItem);
        btnAdd = findViewById(R.id.btn_addCart_detailItem);
        btnMinus = findViewById(R.id.btn_minusCart_detailItem);
        textQuantity = findViewById(R.id.tv_jumlahBarang_detailItem);
        namaBarang = findViewById(R.id.tv_namaBarang_detailBarang);
        hargaBarang = findViewById(R.id.tv_hargaBarang_detailBarang);
        descBarang = findViewById(R.id.tv_desc_detailBarang);
        imgBarang = findViewById(R.id.imgToolbar);


        Bundle extra = getIntent().getExtras();
        cartOperations = new CartOperations(getApplicationContext());
        if (extra != null) {
            jsonString = extra.getString("DATA");
            gson = new Gson();
            dataItem = gson.fromJson(jsonString, JoinModel.class);
            initData(dataItem);
            Log.i("DataItemDetailShop", String.valueOf(dataItem.getQuantity()));
        }

        if (dataItem.getQuantity() > 0) {
            showLayoutCart();
            displayIfexist();
        }

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayoutCart();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUser()) {
                    toCheckout();
                } else {
                    toLogin();
                }
            }
        });
    }

    private void displayIfexist() {
        textQuantity.setText(String.valueOf(dataItem.getQuantity()));
    }

    private void showLayoutCart() {
        btnAddtoCart.setVisibility(View.GONE);
        layoutCart.setVisibility(View.VISIBLE);
        btnCheckout.setVisibility(View.VISIBLE);

        if (dataItem.getQuantity() == 0) {
            insertFirst(1);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity();
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusQuantity();
            }
        });
    }

    private void insertFirst(int i) {
        try {
            cartOperations.openDb();
            CartModel cartModel = new CartModel(dataItem.getIdproduk(), i);
            cartOperations.insertCart(cartModel);
            cartOperations.closeDb();
            Log.i("InsertFirst", "Masuk");
        } catch (SQLException e) {
            Log.i("ErrorInsertFirst", e.getMessage());
        }
    }

    private void minusQuantity() {
        String strQuantity = String.valueOf(textQuantity.getText());
        Log.i("STRquantityCheck", strQuantity);
        int quantity = Integer.parseInt(strQuantity);
        quantity = quantity - 1;
        if (quantity <= 0) {
            layoutCart.setVisibility(View.GONE);
            btnAddtoCart.setVisibility(View.VISIBLE);
        } else {
            displayText(quantity);
        }
    }

    private void addQuantity() {
        String strQuantity = String.valueOf(textQuantity.getText());
        Log.i("STRquantityCheck", strQuantity);
        int quantity = Integer.parseInt(strQuantity);
        quantity = quantity + 1;
        if (quantity > dataItem.getStock()) {
            btnAdd.setVisibility(View.GONE);
        } else {
            displayText(quantity);
        }
    }

    private void displayText(int quantity) {
        updateQuantity(quantity);
        textQuantity.setText(String.valueOf(quantity));
    }

    private void updateQuantity(int quantity) {
        try {
            cartOperations.openDb();
            CartModel cartModel = new CartModel(dataItem.getIdproduk(), quantity);
            cartOperations.updateCart(cartModel);
            cartOperations.closeDb();
            Log.i("UpdateCartCheckout", "Masuk");
        } catch (SQLException e) {
            Log.i("ErrorUpdateCart", e.getMessage());
        }
    }

    private void toCheckout() {
        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void toLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private boolean checkUser() {
        boolean cek = false;
        try {
            userOperations.openDb();
            cek = userOperations.checkRecordUser();
            userOperations.closeDb();
            Log.i("CheckUser", String.valueOf(cek));
        } catch (SQLException e) {
            Log.i("ErrorCheckUser", e.getMessage());
        }
        return cek;
    }

    private void initData(JoinModel dataItem) {
        if (dataItem != null) {
            namaBarang.setText(String.valueOf(dataItem.getName()));
            hargaBarang.setText("Rp " + String.valueOf(dataItem.getSell_price()));
            //descBarang.setText(String.valueOf(dataItem.getDescription()));
            this.stok = dataItem.getStock();
            //toolbar
            Glide.with(this)
                    .load(dataItem.getImage())
                    .into(imgBarang);
            getSupportActionBar().setTitle(dataItem.getName());
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
