package com.npe.youji.activity.checkout;

import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;
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
import com.npe.youji.model.order.RequestOrder;
import com.npe.youji.model.order.RootOrderModel;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.user.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    Button btnPembayaran;
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
    ArrayList<Integer> listIdDistrik = new ArrayList<Integer>();


    JSONArray jsonArray;
    int idDistrik;
    DateFormat sdf;
    String currentDate;
    int subTotal;
    int discount;
    int totalHarga;

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
        btnPembayaran = findViewById(R.id.btn_pembayaran);

        //data user
        if (checkUser()) {
            getDataUser();
            setCurrentDate();
        }
        //date and time
        tvTanggal.setText(getCurrentDate());
        //receycler data
        joinData();

        //retrofit
        retrofit = NetworkClient.getRetrofitClient();
        service = retrofit.create(ApiService.class);

        getApiCity();
        //get spin city and distrik


        //btn pembayaran
        btnPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendOrder();
                rawJson();
            }
        });
    }

    private void rawJson() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new HTTPAsyncTaskPOSTData().execute("http://192.168.1.186/digiponic/youji/apiyouji/api/transaksi");

            }
        }, 3000);

    }

    public class HTTPAsyncTaskPOSTData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    Log.i("RunningRawJson", "Run");
                    return HttpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }

        private String HttpPost(String myUrl) throws IOException, JSONException {
            String result = "";
            URL url = new URL(myUrl);

            // 1. create HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 2. build JSON object
            JSONObject jsonObject = createJSON();
            Log.i("ISIjson", String.valueOf(jsonObject));
            // 3. add JSON content to POST request body
            setPostRequestContent(conn, jsonObject);
            Log.i("ResponseCode", String.valueOf(conn.getResponseCode()));
            // 4. make POST request to the given URL
            conn.connect();
            Log.i("SuccessKirim", "Berhasil");
            // 5. return response message
            Log.i("Kembalian",conn.getResponseMessage());
            return conn.getResponseMessage() + "";
        }

        private void setPostRequestContent(HttpURLConnection conn,
                                           JSONObject jsonObject) throws IOException {
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            os.close();
        }
    }

    private JSONObject createJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        int discount = 0;
        int grandTotal = getSubTotal() - discount;
        int status = 1;
        int pajak = 0;
        int satuan = 10;
        String kodeProduk = "PRD/00012";
        try {
            // Add Property
            jsonObject.accumulate("customer_id", getUserId());
            jsonObject.accumulate("tanggal", getCurrentDate());
            jsonObject.accumulate("subtotal", getSubTotal());
            jsonObject.accumulate("diskon", discount);
            jsonObject.accumulate("pajak", pajak);
            jsonObject.accumulate("grand_total", grandTotal);

            jsonObject.accumulate("penjualan_detil", jsonArray);

            Log.d("EXPORTJSON", jsonObject.toString());
        } catch (JsonIOException e) {
            Log.d("JSONERROREX", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void sendOrder() {
        Log.i("Customers_id", String.valueOf(getUserId()));
        Log.i("total", String.valueOf(getSubTotal()));
        Log.i("discount", String.valueOf(this.discount));
        int grandTotal = getSubTotal() - discount;
        Log.i("grand_total", String.valueOf(grandTotal));
        Log.i("shipping_date", getCurrentDate());
        int status = 1;
        Log.i("status", String.valueOf(status));
        Log.i("IdDistrik", String.valueOf(getIdDistrik()));
        Log.i("Customers_name", getUserName());
        Log.i("ordering_detail", String.valueOf(jsonArray));
        Log.i("Customers_email", getUserEmail());


        RequestOrder requestOrder = new RequestOrder(getUserId(), getSubTotal(),
                discount, grandTotal, getCurrentDate(), status, getIdDistrik(), getUserName(), getUserEmail());

        Log.i("RequestJson", String.valueOf(requestOrder));

        service.sendOrder(requestOrder).enqueue(new Callback<RootOrderModel>() {
            @Override
            public void onResponse(Call<RootOrderModel> call, Response<RootOrderModel> response) {
                RootOrderModel data = response.body();
                Log.i("DataResponseOrder", String.valueOf(response));

                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                Log.i("BerhasilResponseInsert", "Berhasil");
                if (data != null) {
                    if (data.getApi_message().equalsIgnoreCase("success")) {
                        Log.i("SuccessSendOrder", "Success");
                        Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RootOrderModel> call, Throwable t) {
                Log.i("ErrorSendOrder", t.getMessage());
            }
        });

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
            listIdDistrik.add(i, listDistrik.get(i).getId());
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
                int idDistrik = listIdDistrik.get(position);
                dataIdDistrik(idDistrik);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("NothingSelectCity", "Nothing");
            }
        });
    }

    private void dataIdDistrik(int idDistrik) {
        this.idDistrik = idDistrik;
    }

    private int getIdDistrik() {
        return this.idDistrik;
    }

    private void setCurrentDate() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.currentDate = sdf.format(new Date());

        Log.i("CurrentDate", currentDate);

    }

    private String getCurrentDate() {
        return this.currentDate;
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
        this.tvNamaUser.setText(userModels.get(0).getNama());
        this.tvEmailUser.setText(userModels.get(0).getEmail());
        this.idUser = userModels.get(0).getId();
    }

    private int getUserId() {
        return this.idUser;
    }

    private String getUserName() {
        String nameUser = this.tvNamaUser.getText().toString();
        return nameUser;
    }

    private String getUserEmail() {
        String emailUser = this.tvEmailUser.getText().toString();
        return emailUser;
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
        int subTotal = 0;
        //String kodeProduk = "PRD/00012";
        //int satuan = 10;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < dataitem.size(); i++) {
            Log.i("DataItemCheckout", String.valueOf(dataitem.get(i).getQuantity()));
            if (dataitem.get(i).getQuantity() > 0) {
                try {
                    JSONObject pnObj = new JSONObject();
                    pnObj.put("id_produk", dataitem.get(i).getIdproduk());
                    pnObj.put("kode_produk", dataitem.get(i).getKode());
                    //Log.i("NamaProdukJoin", dataitem.get(i).getKeterangan());
                    pnObj.put("nama_produk", dataitem.get(i).getKeterangan());
                    pnObj.put("satuan", dataitem.get(i).getSatuan());
                    pnObj.put("kuantitas", dataitem.get(i).getQuantity());
                    int jsonSubTotal = dataitem.get(i).getHarga() * dataitem.get(i).getQuantity();
                    pnObj.put("harga", jsonSubTotal);
                    pnObj.put("diskon", discount);
                    //add subtotal
                    subTotal = subTotal + jsonSubTotal;
                    Log.i("JsonObjectSuccess", "Berhasil");
                    jsonArray.put(pnObj);
                } catch (JSONException e) {
                    Log.i("JsonObjectError", e.getMessage());
                }
            }
        }
        this.jsonArray = jsonArray;
        Log.i("JSONARRAYJoin", String.valueOf(jsonArray));
        setSubTotal(subTotal);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
        adapter = new AdapterCheckout(getApplicationContext(), dataitem);
        recyclerView.setAdapter(adapter);
    }

    private void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    private int getSubTotal() {
        return this.subTotal;
    }


}
