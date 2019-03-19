package com.npe.youji.fragment.inbox;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.inbox.RootInboxModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragmenet extends Fragment {


    public InboxFragmenet() {
        // Required empty public constructor
    }

    RecyclerView rvInbox;
    Retrofit retrofit;
    ApiService service;
    AdapterInbox adapterInbox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox_fragmenet, container, false);
        //inisialisasi
        rvInbox = v.findViewById(R.id.rvInbox);

        //retrofit
        initRetrofit();

        getDataInbox();
        return v;
    }

    private void getDataInbox() {
        service.getPesan().enqueue(new Callback<List<RootInboxModel>>() {
            @Override
            public void onResponse(Call<List<RootInboxModel>> call, Response<List<RootInboxModel>> response) {
                List<RootInboxModel> data = response.body();
                if(data != null){
                    listInbox(data);
                }
            }

            @Override
            public void onFailure(Call<List<RootInboxModel>> call, Throwable t) {
                Log.i("ErrorGetInbox", t.getMessage());
            }
        });
    }

    private void listInbox(List<RootInboxModel> data) {
        rvInbox.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterInbox = new AdapterInbox(getContext(), data);
        rvInbox.setAdapter(adapterInbox);
    }

    private void initRetrofit() {
        retrofit = NetworkClient.getRetrofitClientLocal();
        service = retrofit.create(ApiService.class);
    }

}
