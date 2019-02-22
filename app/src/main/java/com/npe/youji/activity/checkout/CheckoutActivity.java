package com.npe.youji.activity.checkout;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.npe.youji.R;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.user.UserModel;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private CartOperations cartOperations;
    private UserOperations userOperations;
    private ShopOperations shopOperations;

    private ArrayList<JoinModel> dataitem;
    private List<UserModel> userModels;
    private AdapterCheckout adapter;

    private RecyclerView recyclerView;
    TextView tvNamaUser,tvEmailUser, tvTanggal, tvSubtotal,tvDiskon, tvTotal;
    EditText etAlamat, etNotelp;
    int idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        //inisialisasi
        shopOperations = new ShopOperations(getApplicationContext());
        userOperations = new UserOperations(getApplicationContext());
        cartOperations = new CartOperations(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerCheckout);
        tvNamaUser = findViewById(R.id.tvNamaUserCheckout);
        tvEmailUser = findViewById(R.id.tvEmailUserCheckout);
        tvTanggal = findViewById(R.id.tvTanggalCheckout);
        tvSubtotal = findViewById(R.id.tvSubtotalCheckout);
        tvDiskon = findViewById(R.id.tvDiskonCheckout);
        tvTotal = findViewById(R.id.tvTotalCheckout);
        etAlamat = findViewById(R.id.etAlamatPenerima);
        etNotelp = findViewById(R.id.etNotelpPenerima);

        //data user
        if(checkUser()){
            getDataUser();
            setCurrentDate();
        }

        //receycler data
        joinData();

    }

    private void setCurrentDate() {

    }

    private void getDataUser() {
        try{
            userOperations.openDb();
            userModels = userOperations.getAllUser();
            Log.i("DataUser", userModels.get(0).getNama());
            //set user
            setUser(userModels);
            userOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorGetAllUser", e.getMessage());
        }
    }

    private void setUser(List<UserModel> userModels) {
        tvNamaUser.setText(userModels.get(0).getNama());
        tvEmailUser.setText(userModels.get(0).getEmail());
        idUser = userModels.get(0).getId();
    }


    private boolean checkUser() {
        boolean cek = false;
        try{
            userOperations.openDb();
            cek = userOperations.checkRecordUser();
            userOperations.closeDb();
            Log.i("CheckUser", "Masuk");
        }catch (SQLException e){
            Log.i("ErrorCheckUserCheckout", e.getMessage());
        }
        return cek;
    }

    private void joinData() {
        try {
            shopOperations.openDb();
            shopOperations.joinData();
            //insert to adapter
            listDataItem(shopOperations.joinData());
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.d("ERROR JOIN", e.getMessage());
        }
    }

    private void listDataItem(ArrayList<JoinModel> dataitem) {
        for (int i = 0; i < dataitem.size(); i++) {
            Log.i("DataItemCheckout", String.valueOf(dataitem.get(i).getQuantity()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
        adapter = new AdapterCheckout(getApplicationContext(), dataitem);
        recyclerView.setAdapter(adapter);
    }
}
