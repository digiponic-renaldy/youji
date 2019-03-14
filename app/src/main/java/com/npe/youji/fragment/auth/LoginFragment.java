package com.npe.youji.fragment.auth;


import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.Wave;
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

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnLoginGoogle, btnLoginFacebook;
    //facebook
    LoginButton btnFacebook;
    CallbackManager callbackManager;
    //retrofit
    private Retrofit retrofit_local;
    private ApiService service_local;
    private UserOperations userOperations;
    UserModel mUserModel;
    ProgressDialog progressDialog;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //inisialisasi
        btnLoginGoogle = v.findViewById(R.id.btn_login_google);
        btnLoginFacebook = v.findViewById(R.id.btn_login_facebook);
        btnFacebook = v.findViewById(R.id.login_button_facebook);
        //progress dialog
        progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        mAuth = FirebaseAuth.getInstance();

        //retrofit
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);
        userOperations = new UserOperations(getContext());

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

        return v;
    }

    private void signInFacebook() {
        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions("email");
        btnFacebook.setFragment(this);
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateUI(final FirebaseUser user) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("name", user.getDisplayName().toString());
            jsonObject.addProperty("email", user.getEmail().toString());
        } catch (JsonIOException e){
            Log.i("JsonPelangganError",e.getMessage());
        }

        service_local.sendPelanggan(jsonObject).enqueue(new Callback<List<RootPelangganModel>>() {
            @Override
            public void onResponse(Call<List<RootPelangganModel>> call, Response<List<RootPelangganModel>> response) {
                List<RootPelangganModel> data = response.body();
                if(data != null){
                    if(insertDataUser(data) != null){
                        toMain();
                        Log.i("ApiPelanggan", "masuk");
                        Log.i("NamaUser", mUserModel.getNama().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RootPelangganModel>> call, Throwable t) {
                Log.i("GagalApiCustomer", t.getMessage());
            }
        });
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(getContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void toMain() {
        progressDialog.dismiss();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




}
