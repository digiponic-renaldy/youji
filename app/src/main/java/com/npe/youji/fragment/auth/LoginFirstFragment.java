package com.npe.youji.fragment.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.npe.youji.R;
import com.npe.youji.activity.auth.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFirstFragment extends Fragment {

    private Button btnLogin;
    public LoginFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_first, container, false);

        //inisialisasi
        btnLogin = v.findViewById(R.id.btnToLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
        return v;
    }

    private void toLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
