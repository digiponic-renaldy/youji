package com.npe.youji.activity.checkout;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.city.DataCitiesModel;
import com.npe.youji.model.city.DataDistrikModel;
import com.npe.youji.model.city.RootCitiesModel;
import com.npe.youji.model.city.RootDistrikModel;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.user.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckoutActivity extends AppCompatActivity {
    private CartOperations cartOperations;
    private UserOperations userOperations;
    private ShopOperations shopOperations;

    private ArrayList<JoinModel> dataitem;
    private List<UserModel> userModels;
    private AdapterCheckout adapter;

    private RecyclerView recyclerView;
    TextView tvNamaUser, tvEmailUser, tvTanggal, tvSubtotal, tvDiskon, tvTotal;
    EditText etAlamat, etNotelp;
    int idUser;

    //spinner
    private Spinner spinCity;
    private Spinner spinDistrik;
    //retrofit
    private Retrofit retrofit;
    private ApiService service;
    List<DataCitiesModel> listCity;
    ArrayList<String> listNamaCity = new ArrayList<String>();
    ArrayList<Integer> listStateIdCity = new ArrayList<Integer>();
    List<DataDistrikModel> listDistrik;
    ArrayList<String> listNamaDistrik = new ArrayList<String>();

    JSONObject jsonObject;
    ArrayList<JSONObject> listJsonObject = new ArrayList<JSONObject>();

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
        spinCity = findViewById(R.id.spinCity);
        spinDistrik = findViewById(R.id.spinDistrik);
        //data user
        if (checkUser()) {
            getDataUser();
            setCurrentDate();
        }

        //receycler data
        joinData();

        //retrofit
        retrofit = NetworkClient.getRetrofitClient();
        service = retrofit.create(ApiService.class);

        getApiCity();
        //get spin city and distrik

    }

    private void getApiCity() {
        service.listCity().enqueue(new Callback<RootCitiesModel>() {
            @Override
            public void onResponse(Call<RootCitiesModel> call, Response<RootCitiesModel> response) {
                RootCitiesModel data = response.body();
                if (data != null) {
                    if (data.getApi_message().equalsIgnoreCase("success")) {
                        listCity = data.getData();
                        getCity(listCity);
                    }
                }
            }

            @Override
            public void onFailure(Call<RootCitiesModel> call, Throwable t) {
                Log.i("ErrorGetListCity", t.getMessage());
            }
        });
    }

    private void getCity(List<DataCitiesModel> listCity) {
        for (int i = 0; i < listCity.size(); i++) {
            Log.i("DataNamaCity", listCity.get(i).getName());
            listNamaCity.add(i, listCity.get(i).getName());
            listStateIdCity.add(i, listCity.get(i).getStates_id());
        }
        spinNamaCity(listNamaCity);
    }

    private void spinNamaCity(final ArrayList<String> listNamaCity) {
        ArrayAdapter<String> dataCity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listNamaCity);
        dataCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCity.setAdapter(dataCity);
        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int states_id = listStateIdCity.get(position);
                getApiDistrik(states_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("NothingSelectCity", "Nothing");
            }
        });
    }


    private void getApiDistrik(int states_id) {
        service.listDistrik(states_id).enqueue(new Callback<RootDistrikModel>() {
            @Override
            public void onResponse(Call<RootDistrikModel> call, Response<RootDistrikModel> response) {
                RootDistrikModel data = response.body();
                if (data != null) {
                    if (data.getApi_message().equalsIgnoreCase("success")) {
                        listDistrik = data.getData();
                        getDistrik(listDistrik);
                    }
                }
            }

            @Override
            public void onFailure(Call<RootDistrikModel> call, Throwable t) {
                Log.i("ErrorGetListDistrik", t.getMessage());
            }
        });
    }

    private void getDistrik(List<DataDistrikModel> listDistrik) {
        for (int i = 0; i < listDistrik.size(); i++) {
            listNamaDistrik.add(i, listDistrik.get(i).getName());
        }
        spinNamaDistrik(listNamaDistrik);
    }

    private void spinNamaDistrik(ArrayList<String> listNamaDistrik) {
        ArrayAdapter<String> dataDistrik = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listNamaDistrik);
        dataDistrik.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDistrik.setAdapter(dataDistrik);
        spinDistrik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("NothingSelectCity", "Nothing");
            }
        });
    }

    private void setCurrentDate() {

    }

    private void getDataUser() {
        try {
            userOperations.openDb();
            userModels = userOperations.getAllUser();
            Log.i("DataUser", userModels.get(0).getNama());
            //set user
            setUser(userModels);
            userOperations.closeDb();
        } catch (SQLException e) {
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
        try {
            userOperations.openDb();
            cek = userOperations.checkRecordUser();
            userOperations.closeDb();
            Log.i("CheckUser", "Masuk");
        } catch (SQLException e) {
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
            if (dataitem.get(i).getQuantity() > 0) {
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("products_id", dataitem.get(i).getIdproduk());
                    jsonObject.put("products_name", dataitem.get(i).getName());
                    jsonObject.put("products_price", dataitem.get(i).getSell_price());
                    jsonObject.put("quantity", dataitem.get(i).getQuantity());
                    jsonObject.put("sub_total", (dataitem.get(i).getSell_price() * dataitem.get(i).getQuantity()));
                    Log.i("JsonObjectSuccess", "Berhasil");
                    listJsonObject.add(i, jsonObject);
                } catch (JSONException e) {
                    Log.i("JsonObjectError", e.getMessage());
                }
                Log.i("JSONObject", String.valueOf(listJsonObject.get(i)));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
        adapter = new AdapterCheckout(getApplicationContext(), dataitem);
        recyclerView.setAdapter(adapter);
    }
}
