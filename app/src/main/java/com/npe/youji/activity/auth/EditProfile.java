package com.npe.youji.activity.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.npe.youji.MainActivity;
import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.user.RootPerbaruiUser;
import com.npe.youji.model.user.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfile extends AppCompatActivity {

    Retrofit retrofit;
    ApiService service;
    TextView tvNama;
    EditText etEmail, etFullname, etPhone, etAlamat;
    Button btnUpdate;
    UserOperations userOperations;
    List<UserModel> userModels;
    ProgressDialog progressDialog;
    int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //insialisasi
        tvNama = findViewById(R.id.tvNamaEditProfile);
        etEmail = findViewById(R.id.tvEmailEditProfile);
        etFullname = findViewById(R.id.tvFullnameEditProfile);
        etPhone = findViewById(R.id.tvPhoneEditProfile);
        etAlamat = findViewById(R.id.tvAlamatEditProfile);
        btnUpdate = findViewById(R.id.btn_updateProfile);

        //sql data
        userOperations = new UserOperations(getApplicationContext());

        //retrofit
        retrofit = NetworkClient.getRetrofitClientLocal();
        service = retrofit.create(ApiService.class);

        //progress dialog
        dialogWait();

        //get Data user
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUser();
            }
        }, 2000);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname, adress, notelp, username, email;
                fullname = etFullname.getText().toString();
                notelp = etPhone.getText().toString();
                adress = etAlamat.getText().toString();
                username = tvNama.getText().toString();
                email = etEmail.getText().toString();
                if (checkMasukkan(fullname, adress, notelp)) {
                    progressDialog.show();

                    updateProfile(fullname, adress, notelp, username, email);
//                    Toast.makeText(getApplicationContext(), "Berhasil update", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateProfile(final String fullname, final String adress, final String notelp, final String username, final String email) {
        //body json
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", username);
        jsonObject.addProperty("fullname", fullname);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("phone", notelp);
        jsonObject.addProperty("address", adress);
        //update user
        service.updateUser(jsonObject).enqueue(new Callback<RootPerbaruiUser>() {
            @Override
            public void onResponse(Call<RootPerbaruiUser> call, Response<RootPerbaruiUser> response) {
                RootPerbaruiUser data = response.body();
                if (data != null) {
                    progressDialog.dismiss();
                    updateUser(id_user, username, fullname, email, notelp, adress);
                    toMain();
                    Toast.makeText(getApplicationContext(), "Berhasil Update Data User", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RootPerbaruiUser> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("ErrorUpdateUser", t.getMessage());
            }
        });
    }

    private void updateUser(int id_user, String username, String fullname, String email, String notelp, String adress) {
        try {
            userOperations.openDb();
            UserModel userModel = new UserModel(id_user, username, email, fullname, adress, notelp);
            userOperations.updateuser(userModel);
            userOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorUpdateUser", e.getMessage());
        }
    }

    private void toMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private boolean checkMasukkan(String fullname, String adress, String notelp) {
        boolean valid;
        if (!fullname.isEmpty()) {
            if (!notelp.isEmpty()) {
                if (!adress.isEmpty()) {
                    valid = true;
                } else {
                    etAlamat.setError("Alamat Belum Terisi");
                    etAlamat.requestFocus();
                    valid = false;
                }
            } else {
                etPhone.setError("Nomor Telepon Belum Terisi");
                etPhone.requestFocus();
                valid = false;
            }
        } else {
            etFullname.setError("Nama Lengkap Belum Terisi");
            etFullname.requestFocus();
            valid = false;
        }
        return valid;
    }


    private void dialogWait() {
        progressDialog = new ProgressDialog(this, R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void getUser() {
        try {
            userOperations.openDb();
            userModels = userOperations.getAllUser();
            userOperations.closeDb();
            //init data user
            initUser(userModels);
        } catch (SQLException e) {
            Log.i("ErrorGetDataUser", e.getMessage());
        }
    }

    private void initUser(List<UserModel> userModels) {
        if (userModels != null) {
            this.id_user = userModels.get(0).getId();

            if (userModels.get(0).getFullname() != null) {
                Log.i("Fullname", userModels.get(0).getFullname());
                etFullname.setText(userModels.get(0).getFullname());
            }
            if (userModels.get(0).getAlamat() != null) {
                Log.i("Alamat", userModels.get(0).getAlamat());
                etAlamat.setText(userModels.get(0).getAlamat());
            }
            if (userModels.get(0).getNotelp() != null) {
                Log.i("Phone", userModels.get(0).getNotelp());
                etPhone.setText(userModels.get(0).getNotelp());
            }

            tvNama.setText(userModels.get(0).getNama());
            etEmail.setText(userModels.get(0).getEmail());
        }
        progressDialog.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
