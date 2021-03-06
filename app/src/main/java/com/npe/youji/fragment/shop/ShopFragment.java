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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.npe.youji.R;
import com.npe.youji.model.api.ApiService;
import com.npe.youji.model.api.NetworkClient;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.DataShopItemModel;
import com.npe.youji.model.shop.RootShopItemModel;
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
    private Retrofit retrofit;
    private ApiService service;
    private CartOperations cartOperations;
    private CartModel cartModel;
    private List<CartModel> listCartModel;
    private DataShopItemModel data;

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

        //sqlite
        cartOperations = new CartOperations(getContext());
        checkSqliDb();

        //bottom sheet
        botomSheet = BottomSheetBehavior.from(layoutBottomSheet);
        botomSheet.setHideable(false);
        btnFloatCheckout = v.findViewById(R.id.floatBtn_checkout);



        //retrofit
        retrofit = NetworkClient.getRetrofitClient();
        service = retrofit.create(ApiService.class);
        getCategory();
        getItemProduct();
        //float sheet
        btnFloatCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSheetCollapse();
            }
        });
        //bottom sheet
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

        return v;
    }

    private void checkSqliDb() {
        try{
            cartOperations.openDb();
            listCartModel = cartOperations.getAllCart();
            Log.d("LIST_CART",String.valueOf(listCartModel));
            if(listCartModel.isEmpty()){
                layoutBottomSheet.setVisibility(View.GONE);
            }else {
                layoutBottomSheet.setVisibility(View.VISIBLE);
            }
            cartOperations.closeDb();
        } catch (SQLException e){
            Log.d("ERROR SHOP FRAGMENT", e.getMessage() + " ERROR");
        }
    }

    private void expandSheetCollapse() {
        if (botomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            botomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            botomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void getCategory() {
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

    private void getItemProduct() {
        service.listProduct().enqueue(new Callback<RootShopItemModel>() {
            @Override
            public void onResponse(Call<RootShopItemModel> call, Response<RootShopItemModel> response) {
                if (response.body() != null) {
                    RootShopItemModel data = response.body();
                    if (data.getApi_message().equalsIgnoreCase("success")) {
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

    private void listItemShop(final ArrayList<DataShopItemModel> dataItem) {
        Log.d("LIST_DATA_PRODUCT", dataItem.toString());
        recyclerItem.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterItem = new AdapterShopItem(getContext(), dataItem);
        recyclerItem.setAdapter(adapterItem);
        adapterItem.setOnItemClickListener(new AdapterShopItem.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                detailItem(position);
            }

            @Override
            public void onBeliClick(int position, ImageButton btnAdd, ImageButton btnMinus, Button beli,
                                    TextView textQuantity, RelativeLayout layoutCart) {
                showLayoutCart(position,btnAdd, btnMinus, beli, textQuantity, layoutCart);
            }
        });
    }

    private void showLayoutCart(int position, ImageButton btnAdd, ImageButton btnMinus,
                                Button beli, TextView textQuantity, RelativeLayout layoutCart) {
        adapterItem.showLayoutCart(position,btnAdd, btnMinus, beli, textQuantity, layoutCart);
    }

    private void detailItem(int position) {
        data = dataItem.get(position);
        adapterItem.detailItem(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkSqliDb();
    }


}
