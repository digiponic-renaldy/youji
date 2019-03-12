package com.npe.youji.fragment.auth;


import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.npe.youji.MainActivity;
import com.npe.youji.R;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.user.UserModel;

import java.util.List;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private UserOperations userOperations;
    private Button btnLogout,btnEditProfile;
    private TextView tvNama, tvEmail;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        //inisialisasi
        mAuth = FirebaseAuth.getInstance();
        tvNama = v.findViewById(R.id.tvNamaProfile);
        tvEmail = v.findViewById(R.id.tvEmailProfile);
        userOperations = new UserOperations(getContext());
        btnLogout = v.findViewById(R.id.btnLogout);
        btnEditProfile = v.findViewById(R.id.btnEditProfile);
        
        if(checkUser()){
            Log.i("CheckUserAuth", "Masuk");
            getDataUser();
        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        return v;
    }

    private void editProfile() {
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

    private void getDataUser() {
        try{
            userOperations.openDb();
            userOperations.getAllUser();
            initDataUser(userOperations.getAllUser());
            userOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorGetDataUser", e.getMessage());
        }
    }

    private void initDataUser(List<UserModel> allUser) {
        if(allUser != null){
            tvNama.setText(allUser.get(0).getNama());
            tvEmail.setText(allUser.get(0).getEmail());
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        revokeAccess();
                    }
                });
    }

    private void toMain() {
        if (deleteDataUserSql()) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean deleteDataUserSql() {
        boolean cek = false;
        try {
            userOperations.openDb();
            userOperations.truncateUser();
            cek = true;
            userOperations.closeDb();
            Log.i("DeleteDataUser", "Berhasil");
        } catch (SQLException e) {
            Log.i("DeleteDataUser", e.getMessage());
        }
        return cek;
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(requireActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        toMain();
                    }
                });
    }



}
