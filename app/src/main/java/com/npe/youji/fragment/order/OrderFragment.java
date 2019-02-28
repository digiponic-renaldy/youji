package com.npe.youji.fragment.order;


import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.order.RootListTransaksiModel;
import com.npe.youji.model.user.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    RecyclerView rvTransaksi;
    Retrofit retrofit_local;
    ApiService service_local;
    UserOperations userOperations;
    List<UserModel> userModel;
    int idUser;
    AdapterTransaksi adapterTransaksi;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        //inisialisasi
        rvTransaksi = v.findViewById(R.id.rvListTransaksi);
        userOperations = new UserOperations(getContext());

        //retrofit
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);


        if (checkUser()) {
            //get data user
            getDataUser();
            //get data Transaksi
            getDataTransaksi();
        }
        return v;
    }

    private void getDataTransaksi() {
        JsonObject request = new JsonObject();
        request.addProperty("customer_id", idUser);
        service_local.listTransaksiUser(request).enqueue(new Callback<List<RootListTransaksiModel>>() {
            @Override
            public void onResponse(Call<List<RootListTransaksiModel>> call, Response<List<RootListTransaksiModel>> response) {
                List<RootListTransaksiModel> data = response.body();
                if(data != null){
                    Log.i("GetDataUserTransaksi","Masuk");
                    listTransaksi(data);
                }
            }

            @Override
            public void onFailure(Call<List<RootListTransaksiModel>> call, Throwable t) {
                Log.i("ErrorGetDataTransaksi", t.getMessage());
            }
        });
    }

    private void listTransaksi(List<RootListTransaksiModel> data) {
        rvTransaksi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        adapterTransaksi = new AdapterTransaksi(getContext(), data);
        rvTransaksi.setAdapter(adapterTransaksi);
    }

    private boolean checkUser() {
        boolean cek = false;
        try {
            userOperations.openDb();
            cek = userOperations.checkRecordUser();
            userOperations.closeDb();
        } catch (SQLException e) {
            Log.i("ErrorCheckUserOrder", e.getMessage());
        }
        return cek;
    }

    private void getDataUser() {
        try {
            userOperations.openDb();
            userModel = userOperations.getAllUser();
            this.idUser = userModel.get(0).getId();
            userOperations.closeDb();
        } catch (SQLException e) {
            Log.i("ErrorGetDataUser", e.getMessage());
        }
    }


}
