package com.npe.youji.fragment.shop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npe.youji.R;
import com.npe.youji.model.shop.DataShopItemModel;
import com.npe.youji.model.shop.RootShopItemModel;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.shop.menu.DataCategory;
import com.npe.youji.model.shop.menu.RootCategoryModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    private RecyclerView recyclerItem, recyclerCategory;
    private AdapterShopItem adapterItem;
    private AdapterCategory adapterCategory;
    private ArrayList<DataShopItemModel> dataItem;
    private ArrayList<DataCategory> dataCategories;
    private Retrofit retrofit;
    private ApiService service;
    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerItem = v.findViewById(R.id.recycler_all_list_shop);
        recyclerCategory = v.findViewById(R.id.recycler_menu_shop);
        retrofit = NetworkClient.getRetrofitClient();
        service = retrofit.create(ApiService.class);
        getCategory();
        getItemProduct();

        return v;
    }

    private void getCategory() {
        service.listCategory().enqueue(new Callback<RootCategoryModel>() {
            @Override
            public void onResponse(Call<RootCategoryModel> call, Response<RootCategoryModel> response) {
                if(response.body() != null){
                    RootCategoryModel data = response.body();
                    if(data.getApi_message().equalsIgnoreCase("success")){
                        Log.d("DATA_CATEGORY", "SUCCESS");
                        dataCategories = (ArrayList<DataCategory>) data.getData();
                        listCategory(dataCategories);
                    }
                }
            }

            @Override
            public void onFailure(Call<RootCategoryModel> call, Throwable t) {
                Log.d("FAILURE_CATEGORY", t.getMessage());
            }
        });
    }

    private void listCategory(ArrayList<DataCategory> dataCategories) {
        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapterCategory = new AdapterCategory(getContext(), dataCategories);
        recyclerCategory.setAdapter(adapterCategory);

    }

    private void getItemProduct() {
        service.listProduct().enqueue(new Callback<RootShopItemModel>() {
            @Override
            public void onResponse(Call<RootShopItemModel> call, Response<RootShopItemModel> response) {
                if(response.body() != null){
                    RootShopItemModel data = response.body();
                    if(data.getApi_message().equalsIgnoreCase("success")){
                        dataItem = (ArrayList<DataShopItemModel>) data.getData();
                        listItemShop(dataItem);
                    }
                }
            }

            @Override
            public void onFailure(Call<RootShopItemModel> call, Throwable t) {
                Log.d("FAILURE_PRODUCT", t.getMessage());
            }
        });
    }

    private void listItemShop(ArrayList<DataShopItemModel> dataItem) {
        Log.d("LIST_DATA_PRODUCT", dataItem.toString());
        recyclerItem.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterItem = new AdapterShopItem(getContext(), dataItem);
        recyclerItem.setAdapter(adapterItem);
    }

}
