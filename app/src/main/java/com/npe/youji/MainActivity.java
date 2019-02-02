package com.npe.youji;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.npe.youji.fragment.LoginFragment;
import com.npe.youji.fragment.OrderFragment;
import com.npe.youji.fragment.shop.ShopFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()){
                    case R.id.menu_login :
                        fragment = new LoginFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.menu_order :
                        fragment = new OrderFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.menu_shop :
                        fragment = new ShopFragment();
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


}
