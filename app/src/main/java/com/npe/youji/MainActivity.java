package com.npe.youji;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.npe.youji.activity.auth.LoginActivity;
import com.npe.youji.activity.checkout.CheckoutActivity;
import com.npe.youji.fragment.auth.AccountFragment;
import com.npe.youji.fragment.auth.LoginFirstFragment;
import com.npe.youji.fragment.auth.LoginFragment;
import com.npe.youji.fragment.inbox.InboxFragmenet;
import com.npe.youji.fragment.order.OrderFragment;
import com.npe.youji.fragment.shop.ShopFragment;
import com.npe.youji.model.dbsqlite.CartOperations;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FirebaseAuth auth;
    private FirebaseUser mUser;
    private boolean doubleBackToExitPressedOnce = false;
    CartOperations cartOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //insialisasi
        bottomNavigation = findViewById(R.id.bottom_navigation);
        auth = FirebaseAuth.getInstance();
        cartOperations = new CartOperations(getApplicationContext());
        //shop
        Fragment fragment = new ShopFragment();
        loadFragment(fragment);

        //bottom navigation
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.menu_login:
                        if (mUser != null) {
                            fragment = new AccountFragment();
                        } else {
                            fragment = new LoginFragment();
                        }
                        loadFragment(fragment);
                        return true;
                    case R.id.menu_order:
                        if (mUser != null) {
                            fragment = new OrderFragment();
                        } else {
                            fragment = new LoginFirstFragment();
                        }
                        loadFragment(fragment);
                        return true;
                    case R.id.menu_shop:
                        fragment = new ShopFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.menu_inbox:
                        fragment = new InboxFragmenet();
                        loadFragment(fragment);
                        return true;
//                    case R.id.menu_chat:
//                        fragment = new ChatFragment();
//                        loadFragment(fragment);
//                        return true;
                }
                return false;
            }
        });


    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_checkoutApp:
                toCheckOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toCheckOut() {
        mUser = auth.getCurrentUser();
        if (mUser != null) {
            if (checkQuantityData()) {
                CheckOut();
            } else {
                Toast.makeText(getApplicationContext(), "Tidak terdapat barang yang dibeli", Toast.LENGTH_SHORT).show();
            }
        } else {
            toLogin();
        }
    }

    private void CheckOut() {
        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean checkQuantityData() {
        boolean valid = false;
        try{
            cartOperations.openDb();
            valid = cartOperations.checkRecordCart();
            cartOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorCheckDataCart", e.getMessage());
        }
        return valid;
    }

    private void toLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLogin();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan tombol kembali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    private void checkLogin() {
        mUser = auth.getCurrentUser();
        if (mUser != null) {
            Menu menu = bottomNavigation.getMenu();
            MenuItem menuItem = menu.findItem(R.id.menu_login);
            menuItem.setTitle("Profile");
        }
    }
}
