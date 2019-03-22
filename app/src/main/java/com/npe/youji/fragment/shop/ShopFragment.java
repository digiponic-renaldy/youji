package com.npe.youji.fragment.shop;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.npe.youji.R;
import com.npe.youji.activity.auth.LoginActivity;
import com.npe.youji.activity.checkout.CheckoutActivity;
import com.npe.youji.activity.shop.ListKategoriShopActivity;
import com.npe.youji.activity.shop.ListNonKategoryShopActivity;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.DataShopModel;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.shop.RootProdukModel;
import com.npe.youji.model.shop.menu.RootTipeKategoriModel;

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
public class ShopFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerItem, recyclerCategory, recyclerNewest, recyclerBest, recyclerAll;
    private AdapterShopItem adapterItem;
    private AdapterShopItemSayur adapterItemSayur;
    private AdapterShopItemBuah adapterItemBuah;
    private AdapterCategory adapterCategory;
    //retrofit
    private Retrofit retrofit_local;
    private ApiService service_local;

    private CartOperations cartOperations;
    private ShopOperations shopOperations;
    private RelativeLayout layoutShop;

    private CardView cardRekom, cardNews, cardBest, cardAllItem;
    Button btnLihatSemua, btnLihatSayuran, btnLihatBuah, btnLihatAllItem;
    BottomSheetBehavior botomSheet;
    RelativeLayout layoutBottomSheet;
    CircleButton btnFloatCheckout;
    ShimmerRecyclerView shimmerRecyclerShopItem, shimmerRecyclerShopMenu;
    //progress dialog
    ProgressDialog progressDialog;
    //swipe
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<JoinModel> mDataItem = new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseUser mUser;

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
        recyclerNewest = v.findViewById(R.id.recycler_all_list_shopNewest);
        recyclerBest = v.findViewById(R.id.recycler_all_list_shopBest);
        recyclerAll = v.findViewById(R.id.recycler_all_list_shopAll);
        layoutBottomSheet = v.findViewById(R.id.bottom_sheet);
        shopOperations = new ShopOperations(getContext());
        cartOperations = new CartOperations(getContext());
        layoutShop = v.findViewById(R.id.layoutShop);
        shimmerRecyclerShopItem = v.findViewById(R.id.shimmer_shopItem);
        shimmerRecyclerShopMenu = v.findViewById(R.id.shimmer_shopMenu);
        cardRekom = v.findViewById(R.id.layoutRekomendasi);
        cardNews = v.findViewById(R.id.layoutNewsitem);
        cardBest = v.findViewById(R.id.layoutBestSaller);
        cardAllItem = v.findViewById(R.id.layoutAllItem);
        btnLihatSemua = v.findViewById(R.id.lihatSemuaData);
        btnLihatSayuran = v.findViewById(R.id.btnLihatSayuran);
        btnLihatBuah = v.findViewById(R.id.btnLihatBuah);
        btnLihatAllItem = v.findViewById(R.id.btnLihatAllItem);
        swipeRefreshLayout = v.findViewById(R.id.swipeMainShop);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //swipe
        swipeRefreshLayout.setOnRefreshListener(this);

        //shimmer
        shimmerBehavior();

        btnFloatCheckout = v.findViewById(R.id.floatBtn_checkout);

        //retrofit
        initRetrofit();

        getCategory();
        getItemProduk_local();


        btnLihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAllItem("Recommendation");
            }
        });

        btnLihatAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAllItem("All Items");
            }
        });
        btnLihatSayuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAllSayuran();
            }
        });

        btnLihatBuah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAllBuah();
            }
        });

//        btnFloatCheckout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkUser()) {
//                    if (checkQuantityData(mDataItem)) {
//                        toCheckOut();
//                    }
//                } else {
//                    toLogin();
//                }
//            }
//        });

        return v;
    }

    private void toLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean checkUser() {
        boolean valid = false;
        if (mUser != null) {
            valid = true;
        }
        return valid;
    }

    private void toCheckOut() {
        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void showCheckOut() {
        btnFloatCheckout.setVisibility(View.VISIBLE);
    }

    public void hideCheckOut() {
        btnFloatCheckout.setVisibility(View.GONE);
    }

    private void toAllBuah() {
        Intent intent = new Intent(getContext(), ListKategoriShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        truncate();
        intent.putExtra("KATEGORI", "Organic Fruits");
        startActivity(intent);
    }

    private void truncate() {
        try {
            shopOperations.openDb();
            shopOperations.deleteRecord();
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("ErrorTruncateFilter", e.getMessage());
        }
    }

    private void toAllSayuran() {
        Intent intent = new Intent(getContext(), ListKategoriShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        truncate();
        intent.putExtra("KATEGORI", "Organic Vegetable");
        startActivity(intent);
    }

    private void toAllItem(String title) {
        Intent intent = new Intent(getContext(), ListNonKategoryShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("TITLE", title);
        startActivity(intent);
    }

    private void initRetrofit() {
        retrofit_local = NetworkClient.getRetrofitClientLocal();
        service_local = retrofit_local.create(ApiService.class);
    }

    private void shimmerBehavior() {
        shimmerRecyclerShopItem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        shimmerRecyclerShopItem.setAdapter(adapterItem);
        shimmerRecyclerShopItem.showShimmerAdapter();

        shimmerRecyclerShopMenu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        shimmerRecyclerShopMenu.setAdapter(adapterCategory);
        shimmerRecyclerShopMenu.showShimmerAdapter();

        cardRekom.setVisibility(View.GONE);
        cardBest.setVisibility(View.GONE);
        cardNews.setVisibility(View.GONE);
        cardAllItem.setVisibility(View.GONE);
    }

    private void getItemProduk_local() {
        service_local.listProduk().enqueue(new Callback<List<RootProdukModel>>() {
            @Override
            public void onResponse(Call<List<RootProdukModel>> call, Response<List<RootProdukModel>> response) {
                List<RootProdukModel> data = response.body();
                if (data != null) {
                    Log.i("ResponSucc", "Berhasil");
                    Log.i("Deskripsi", data.get(0).getDeskripsi());
                    insertAllDataShopLocal(data);

                    if (checkIsiSqlShop()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                joinData();
                            }
                        }, 1000);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<RootProdukModel>> call, Throwable t) {
                Log.i("ResponseError", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
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

            Log.i("DataProduk", String.valueOf(dataItem.get(i).getStok()));
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
                    dataItem.get(i).getDeskripsi(),
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
        //check quantity
        //inisialisasi
        this.mDataItem = dataItem;
//        if (checkQuantityData(dataItem)) {
//            showCheckOut();
//        } else {
//            hideCheckOut();
//        }


        Log.d("LIST_DATA_PRODUCT", dataItem.toString());
        recyclerItem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Log.i("DataModel", String.valueOf(dataItem));
        adapterItem = new AdapterShopItem(getContext(), dataItem, ShopFragment.this);
        recyclerItem.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterShopItem.OnItemClickListener() {
            @Override
            public void onItemCick(int position, JoinModel data) {
                adapterItem.detailItem(data);
            }
        });
        //Sayur
        recyclerSayur(dataItem);
        //Buah
        recyclerBuah(dataItem);
        //all item
        recyclerAllItem();

        //hide shimmer and show card
        shimmerRecyclerShopMenu.hideShimmerAdapter();
        shimmerRecyclerShopItem.hideShimmerAdapter();
        //hide dialog
        progressDialog.dismiss();
        //hide swipe
        swipeRefreshLayout.setRefreshing(false);
        //visible card
        cardRekom.setVisibility(View.VISIBLE);
        cardBest.setVisibility(View.VISIBLE);
        cardNews.setVisibility(View.VISIBLE);
        cardAllItem.setVisibility(View.VISIBLE);
    }

//    private boolean checkQuantityData(ArrayList<JoinModel> dataItem) {
//        boolean valid = false;
//        for (int i = 0; i < dataItem.size(); i++) {
//            if (dataItem.get(i).getQuantity() != 0) {
//                valid = true;
//            }
//            Log.i("CheckQuantity", String.valueOf(dataItem.get(i).getQuantity()));
//        }
//        return valid;
//    }

    private void recyclerAllItem() {
        recyclerAll.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerAll.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterShopItem.OnItemClickListener() {
            @Override
            public void onItemCick(int position, JoinModel data) {
                adapterItem.detailItem(data);
            }
        });
    }

    private void recyclerSayur(ArrayList<JoinModel> dataItem) {
        ArrayList<JoinModel> dataSayur = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < dataItem.size(); i++) {
            if (dataItem.get(i).getKategori().equalsIgnoreCase("Organic Vegetable")) {
                dataSayur.add(index, dataItem.get(i));
                index++;

                Log.i("DataSayur", String.valueOf(i));
            }
        }

        adapterItemSayur = new AdapterShopItemSayur(getContext(), dataSayur, ShopFragment.this);
        recyclerNewest.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerNewest.setAdapter(adapterItemSayur);
        adapterItemSayur.setOnItemClickListener(new AdapterShopItemSayur.OnItemClickListener() {
            @Override
            public void onItemCick(int position, JoinModel data) {
                adapterItemSayur.detailItem(data);
            }
        });
    }

    private void recyclerBuah(ArrayList<JoinModel> dataItem) {
        ArrayList<JoinModel> dataBuah = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < dataItem.size(); i++) {
            if (dataItem.get(i).getKategori().equalsIgnoreCase("Organic Fruits")) {
                dataBuah.add(index, dataItem.get(i));
                index++;

                Log.i("DataSayur", String.valueOf(i));
            }
        }
        adapterItemBuah = new AdapterShopItemBuah(getContext(), dataBuah, ShopFragment.this);
        recyclerBest.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerBest.setAdapter(adapterItemBuah);
        adapterItemBuah.setOnItemClickListener(new AdapterShopItemBuah.OnItemClickListener() {
            @Override
            public void onItemCick(int position, JoinModel data) {
                adapterItemBuah.detailItem(data);
            }
        });
    }

    public boolean checkIsiSqlShop() {
        boolean valid = false;
        try {
            shopOperations.openDb();
            valid = shopOperations.checkRecordShop();
            Log.i("DataTersedia", "Tersedia");
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("CheckErrorAll", e.getMessage());
        }
        return valid;
    }

    private void getCategory() {
//        swipeRefreshLayout.setRefreshing(true);

        service_local.listCategory().enqueue(new Callback<List<RootTipeKategoriModel>>() {
            @Override
            public void onResponse(Call<List<RootTipeKategoriModel>> call, Response<List<RootTipeKategoriModel>> response) {
                List<RootTipeKategoriModel> data = response.body();
                if (data != null) {
                    listCategory(data);
                }
            }

            @Override
            public void onFailure(Call<List<RootTipeKategoriModel>> call, Throwable t) {
                Log.i("ErrorListKategori", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void listCategory(List<RootTipeKategoriModel> dataCategories) {
        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapterCategory = new AdapterCategory(getContext(), dataCategories);
        recyclerCategory.setAdapter(adapterCategory);
    }

    @Override
    public void onResume() {
        super.onResume();
        initRetrofit();
        truncate();
        //dialog
        dialogWait();

        getCategory();
        getItemProduk_local();


    }

    @Override
    public void onStart() {
        super.onStart();
        initRetrofit();
        truncate();
        //dialog
        getCategory();
        getItemProduk_local();

    }

    private void dialogWait() {
        progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRetrofit();
                truncate();
                getCategory();
                getItemProduk_local();
            }
        }, 2000);
    }
}
