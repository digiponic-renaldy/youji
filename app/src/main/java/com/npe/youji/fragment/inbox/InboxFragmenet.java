package com.npe.youji.fragment.inbox;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npe.youji.R;
import com.npe.youji.activity.inbox.DetailInbox;
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
public class InboxFragmenet extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public InboxFragmenet() {
        // Required empty public constructor
    }

    RecyclerView rvInbox;
    Retrofit retrofit;
    ApiService service;
    List<RootInboxModel> data;
    AdapterInbox adapterInbox;
    ProgressDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox_fragmenet, container, false);
        //inisialisasi
        rvInbox = v.findViewById(R.id.rvInbox);
        swipeRefreshLayout = v.findViewById(R.id.swipeMainInbox);

        //swipe
        swipeRefreshLayout.setOnRefreshListener(this);
        //dialog
        dialogWait();
        //retrofit
        initRetrofit();

        return v;
    }

    private void dialogWait() {
        dialog = new ProgressDialog(getContext(), R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataInbox();
            }
        }, 2000);
    }

    private void getDataInbox() {
        service.getPesan().enqueue(new Callback<List<RootInboxModel>>() {
            @Override
            public void onResponse(Call<List<RootInboxModel>> call, Response<List<RootInboxModel>> response) {
                data = response.body();
                if (data != null) {
                    listInbox(data);
                }
            }

            @Override
            public void onFailure(Call<List<RootInboxModel>> call, Throwable t) {
                Log.i("ErrorGetInbox", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void listInbox(List<RootInboxModel> data) {
        rvInbox.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterInbox = new AdapterInbox(getContext(), data);
        rvInbox.setAdapter(adapterInbox);
        adapterInbox.setOnItemClickListener(new AdapterInbox.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(int position, String data) {
                if (data != null) {
                    toDetail(data);
                }
            }
        });
        //dialog hide
        dialog.dismiss();
        //swipe hide
        swipeRefreshLayout.setRefreshing(false);
    }

    private void toDetail(String data) {
        Intent intent = new Intent(getContext(), DetailInbox.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("DataInbox", data);
        startActivity(intent);
    }

    private void initRetrofit() {
        retrofit = NetworkClient.getRetrofitClientLocal();
        service = retrofit.create(ApiService.class);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRetrofit();
                getDataInbox();
            }
        }, 2000);
    }
}
