package com.npe.youji.fragment.shop;


import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.DataShopItemModel;
import com.npe.youji.model.shop.DataShopModel;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.shop.RootProdukModel;
import com.npe.youji.model.shop.menu.DataCategory;
import com.npe.youji.model.shop.menu.RootCategoryModel;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
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
    //retrofit
    private Retrofit retrofit;
    private Retrofit retrofit_local;
    private ApiService service;
    private ApiService service_local;

    private CartOperations cartOperations;
    private ShopOperations shopOperations;
    private ProgressBar progressBar;
    private RelativeLayout layoutShop;

    BottomSheetBehavior botomSheet;
    RelativeLayout layoutBottomSheet;
    CircleButton btnFloatCheckout;

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
        layoutBottomSheet = v.findViewById(R.id.bottom_sheet);
        shopOperations = new ShopOperations(getContext());
        cartOperations = new CartOperations(getContext());
        progressBar = v.findViewById(R.id.pbShop);
        layoutShop = v.findViewById(R.id.layoutShop);

        //bottom sheet
        botomSheet = BottomSheetBehavior.from(layoutBottomSheet);
        botomSheet.setHideable(false);
        btnFloatCheckout = v.findViewById(R.id.floatBtn_checkout);


        //retrofit
        //retrofit = NetworkClient.getRetrofitClient();
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        //service = retrofit.create(ApiService.class);
        service_local = retrofit_local.create(ApiService.class);

        //getCategory();
        getItemProduk_local();

        //getItemProduct();

        //float sheet
        btnFloatCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSheetCollapse();
            }
        });
        //bottom sheet

        bottomSheetBehavior();

        return v;
    }

    private void getItemProduk_local() {
        service_local.listProduk().enqueue(new Callback<List<RootProdukModel>>() {
            @Override
            public void onResponse(Call<List<RootProdukModel>> call, Response<List<RootProdukModel>> response) {
                List<RootProdukModel> data = response.body();
                if (data != null) {
                    Log.i("ResponSucc", "Berhasil");
                    insertAllDataShopLocal(data);
                    checkIsiSqlShop();
                    joinData();
                }
            }

            @Override
            public void onFailure(Call<List<RootProdukModel>> call, Throwable t) {
                Log.i("ResponseError", t.getMessage());
            }
        });
    }


    private void insertAllDataShopLocal(List<RootProdukModel> dataItem) {
        String imgUrl = "https://i.imgur.com/kTRJDky.png";
        for (int i = 0; i < dataItem.size(); i++) {
            if (dataItem.get(i).getGambar() == null) {
                dataItem.get(i).setGambar(imgUrl);
            }
            Log.i("DataItemSize", String.valueOf(dataItem.size()));

            Log.i("DataProduk", String.valueOf(dataItem.get(i).getSatuan()));
            DataShopModel data = new DataShopModel(
                    dataItem.get(i).getId(),
                    dataItem.get(i).getCabang(),
                    dataItem.get(i).getKode(),
                    dataItem.get(i).getKeterangan(),
                    dataItem.get(i).getKategori(),
                    dataItem.get(i).getJenis(),
                    dataItem.get(i).getSatuan(),
                    dataItem.get(i).getStok(),
                    dataItem.get(i).getHarga(),
                    dataItem.get(i).getGambar(),
                    dataItem.get(i).getCreated_at(),
                    dataItem.get(i).getUpdated_at(),
                    dataItem.get(i).getDeleted_at());
            try {
                shopOperations.openDb();
                shopOperations.insertShop(data);
                Log.i("DataNamaProdukInsert", data.getKeterangan());
                shopOperations.closeDb();
                Log.i("INSERTSQL", "SUCCESS");
            } catch (SQLException e) {
                Log.i("ERRORINSERT", e.getMessage() + " ERROR");
            }
        }
    }

    private void joinData() {
        try {
            shopOperations.openDb();
            shopOperations.joinData();

            //insert to adapter
            listItemShop(shopOperations.joinData());
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.d("ERROR JOIN", e.getMessage());
        }
    }

    private void listItemShop(ArrayList<JoinModel> dataItem) {
        Log.d("LIST_DATA_PRODUCT", dataItem.toString());
        recyclerItem.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterItem = new AdapterShopItem(getContext(), dataItem);
        recyclerItem.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterShopItem.OnItemClickListener() {
            @Override
            public void onItemCick(int position, JoinModel data) {
                adapterItem.detailItem(data);
            }
        });

        layoutShop.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void checkIsiSqlShop() {
        try {
            shopOperations.openDb();
            shopOperations.getAllShop();
            Log.i("CheckAllDataShop", String.valueOf(shopOperations.getAllShop()));
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("CheckErrorAll", e.getMessage());
        }
    }


    private void getCategory() {
        layoutShop.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        service.listCategory().enqueue(new Callback<RootCategoryModel>() {
            @Override
            public void onResponse(Call<RootCategoryModel> call, Response<RootCategoryModel> response) {
                if (response.body() != null) {
                    RootCategoryModel data = response.body();
                    if (data.getApi_message().equalsIgnoreCase("success")) {
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

    private void bottomSheetBehavior() {
        botomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        btnFloatCheckout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        btnFloatCheckout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        btnFloatCheckout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        btnFloatCheckout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        btnFloatCheckout.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }

    private void expandSheetCollapse() {
        if (botomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            botomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            botomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //checkSqliDb();
    }


}
