package com.npe.youji.activity.auth;

import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.user.UserModel;

import java.util.List;

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
        }, 30000);
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
