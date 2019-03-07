package com.npe.youji;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.npe.youji.activity.LoginActivity;
import com.npe.youji.fragment.auth.LoginFirstFragment;
import com.npe.youji.fragment.chat.ChatFragment;
import com.npe.youji.fragment.inbox.InboxFragmenet;
import com.npe.youji.fragment.order.OrderFragment;
import com.npe.youji.fragment.auth.AccountFragment;
import com.npe.youji.fragment.auth.LoginFragment;
import com.npe.youji.fragment.shop.ShopFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FirebaseAuth auth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //insialisasi
        bottomNavigation = findViewById(R.id.bottom_navigation);
        auth = FirebaseAuth.getInstance();

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
                    case R.id.menu_chat:
                        fragment = new ChatFragment();
                        loadFragment(fragment);
                        return true;
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
    protected void onStart() {
        super.onStart();
        checkLogin();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
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
