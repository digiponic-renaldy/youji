package com.npe.youji.activity.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.npe.youji.MainActivity;
import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.user.RootPelangganModel;
import com.npe.youji.model.user.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginFirst";
    private static final int RC_SIGN_IN = 9002;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnLoginGoogle, btnLoginFacebook;
    //facebook
    LoginButton btnFacebook;
    CallbackManager callbackManager;
    private Retrofit retrofit;
    private ApiService service;
    private UserOperations userOperations;
    UserModel mUserModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //inisialisasi
        btnLoginGoogle = findViewById(R.id.btn_login_google_first);
        btnLoginFacebook = findViewById(R.id.btn_login_facebook_first);
        btnFacebook = findViewById(R.id.login_button_facebook_first);
        //progress dialog
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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        //retrofit
        retrofit = NetworkClient.getRetrofitClientLocal();
        service = retrofit.create(ApiService.class);
        userOperations = new UserOperations(getApplicationContext());

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFacebook();
            }
        });
    }

    private void signInFacebook() {
        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions("email");
        btnFacebook.performClick();
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                updateUI(null);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressDialog.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            JsonObject jsonObject = new JsonObject();
            try {
                jsonObject.addProperty("name", user.getDisplayName().toString());
                jsonObject.addProperty("email", user.getEmail().toString());
            } catch (JsonIOException e){
                Log.i("JsonPelangganError",e.getMessage());
            }

            service.sendPelanggan(jsonObject).enqueue(new Callback<List<RootPelangganModel>>() {
                @Override
                public void onResponse(Call<List<RootPelangganModel>> call, Response<List<RootPelangganModel>> response) {
                    List<RootPelangganModel> data = response.body();
                    if(data != null){
                        if(insertDataUser(data) != null){
                            toMain();
                            Log.i("ApiPelanggan", "masuk");
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<RootPelangganModel>> call, Throwable t) {

                }
            });
        }
    }

    private UserModel insertDataUser(List<RootPelangganModel> listUser) {
        String fullname = "";
        String alamat = "";
        String notelp = "";

        if(listUser.get(0).getFullname()!= null){
            fullname = listUser.get(0).getFullname();
        }
        if(listUser.get(0).getAddress()!= null){
            alamat = listUser.get(0).getAddress();
        }
        if(listUser.get(0).getPhone()!= null){
            notelp = listUser.get(0).getPhone();
        }

        try{
            userOperations.openDb();
            UserModel userModel = new UserModel(listUser.get(0).getId(),
                    listUser.get(0).getName(), listUser.get(0).getEmail(),
                    fullname, alamat, notelp);
            mUserModel = userOperations.insertUser(userModel);
            userOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorInsertUser", e.getMessage());
        }

        return mUserModel;
    }

    private void toMain() {
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
