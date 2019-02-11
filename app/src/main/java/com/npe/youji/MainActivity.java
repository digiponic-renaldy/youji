package com.npe.youji;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.npe.youji.fragment.LoginFragment;
import com.npe.youji.fragment.OrderFragment;
import com.npe.youji.fragment.shop.ShopFragment;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    BottomSheetBehavior botomSheet;
    RelativeLayout layoutBottomSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //insialisasi
        bottomNavigation = findViewById(R.id.bottom_navigation);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        botomSheet = BottomSheetBehavior.from(layoutBottomSheet);

        //bottom navigation
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

        //bottom sheet
        botomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED : {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED : {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING :
                        break;
                    case BottomSheetBehavior.STATE_SETTLING :
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

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
