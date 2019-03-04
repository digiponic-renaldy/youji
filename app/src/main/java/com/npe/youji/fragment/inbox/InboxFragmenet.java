package com.npe.youji.fragment.inbox;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npe.youji.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragmenet extends Fragment {


    public InboxFragmenet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox_fragmenet, container, false);

        return v;
    }

}
