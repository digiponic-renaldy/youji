package com.npe.youji.activity;

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
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.DataShopItemModel;
import com.npe.youji.model.shop.JoinModel;


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

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showLayoutCart();
            }
        });

        Bundle extra = getIntent().getExtras();
        cartOperations = new CartOperations(getApplicationContext());
        if (extra != null) {
            jsonString = extra.getString("DATA");
            gson = new Gson();
            dataItem = gson.fromJson(jsonString, JoinModel.class);
            //sql check
            /*if (chekSql(dataItem.id)) {
                try {
                    cartOperations.openDb();
                    cartModel = cartOperations.getCart(dataItem.id);
                    cartOperations.closeDb();
                    Log.d("DATA_CART _MODEL", String.valueOf(cartModel));
                    showLayoutCart();
                    displayQuantity(String.valueOf(cartModel.getQuantity()));
                } catch (SQLException e) {
                    Log.d("ERROR_GET_QUANTITY", e.getMessage() + " ERROR");
                }
            }*/
            initData(dataItem);
        }
    }

    /*private Boolean chekSql(int id) {
        boolean hasRecord = false;
        try {
            cartOperations.openDb();
            hasRecord = cartOperations.checkRecordCart(id);
            cartOperations.closeDb();
        } catch (SQLException e) {
            Log.d("ERROR SQL", e.getMessage() + " ERROR");
        }
        return hasRecord;
    }*/

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

    /*private void showLayoutCart() {
        btnAddtoCart.setVisibility(View.GONE);
        layoutCart.setVisibility(View.VISIBLE);
        btnCheckout.setVisibility(View.VISIBLE);
        if (layoutCart.getVisibility() == View.VISIBLE) {
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

    }

    private void minusQuantity() {
        this.quantity = this.quantity - 1;
        if (this.quantity <= 0) {
            this.quantity = 0;
            layoutCart.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
            btnAddtoCart.setVisibility(View.VISIBLE);
            deleteCart(cartModel.getIdProduct());
        } else {
            String textQuantity = String.valueOf(this.quantity);
            displayQuantity(textQuantity);
        }
    }

    private void deleteCart(long idProduct) {
        try {
            cartOperations.openDb();
            cartOperations.deleteRow(String.valueOf(idProduct));
            cartOperations.closeDb();
            Log.d("SUCCESS DELETE ROW CART", "SUCCESS");
        } catch (SQLException e) {
            Log.d("ERROR DELETE CART", "ERROR " + e.getMessage());
        }
    }*/

    /*private void displayQuantity(String Quantity) {
        if (chekSql(dataItem.getId())) {
            updateRowCart();
        } else {
            insertRowCart(Quantity);
        }
        textQuantity.setText(Quantity);
    }*/

    /*private void insertRowCart(String quantity) {
        try{
            cartOperations.openDb();
            cartModel = new CartModel(dataItem.getId(), dataItem.getName(), dataItem.getStock(), Long.parseLong(quantity));
            cartOperations.insertCart(cartModel);
            cartOperations.closeDb();
            Log.d("INSERT ROW CART DETAIL", "SUCCESS");
        }catch (SQLException e){
            Log.d("ERROR INSERT ROW CART", "ERROR "+e.getMessage());
        }
    }

    private void updateRowCart() {
        try {
            cartOperations.openDb();
            cartOperations.updateCart(cartModel);
            cartOperations.closeDb();
            Log.d("SUCCESS UPDATE","SUCCESS");
        } catch (SQLException e) {
            Log.d("ERROR UPDATE ROW CART", "ERROR " + e.getMessage());
        }
    }*/

    /*private void addQuantity() {
        this.quantity = this.quantity + 1;
        if (this.quantity > stok) {
            btnAdd.setClickable(false);
        } else {
            String textQuantity = String.valueOf(this.quantity);
            displayQuantity(textQuantity);
        }
    }*/

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
