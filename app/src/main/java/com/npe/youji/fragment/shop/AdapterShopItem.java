package com.npe.youji.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.activity.DetailShop;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.DataShopItemModel;

import java.util.List;

public class AdapterShopItem extends RecyclerView.Adapter<AdapterShopItem.ViewHolder> {

    private Context context;
    private List<DataShopItemModel> items;
    private Gson gson;
    private CartOperations cartOperations;

    public AdapterShopItem(Context context, List<DataShopItemModel> items) {
        this.context = context;
        this.items = items;
        this.cartOperations = new CartOperations(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_shop_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final DataShopItemModel data = items.get(i);
        final int idProduct = data.id;

        if (checkRecordDb(idProduct)) {
            if (getDataCart(idProduct) != null) {
                CartModel carts = getDataCart(idProduct);
                showLayoutCart(viewHolder, data);
                Log.i("DATA RECORD", String.valueOf(carts));
                displayIfExist(carts.getQuantity(), viewHolder.textQuantity);
            }
        }

        Glide.with(context)
                .load(data.image)
                .into(viewHolder.imageView);
        viewHolder.nama.setText(data.getName());
        viewHolder.harga.setText(String.valueOf(data.getSell_price()));
        viewHolder.beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayoutCart(viewHolder, data);
            }
        });

        viewHolder.lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailItem(data);
            }
        });
    }

    private void displayIfExist(long quantity, TextView textQuantity) {
        Log.d("IFEXIST", "MASUK");
        textQuantity.setText(String.valueOf(quantity));
    }

    private Boolean checkRecordDb(int idProduct) {
        boolean check = false;
        try {
            cartOperations.openDb();
            check = cartOperations.checkRecordCart(idProduct);
            cartOperations.closeDb();
            Log.i("SQL CHECK ADAPTER", "SUCCESS");
        } catch (SQLException e) {
            Log.i("SQL CHECK", "ERROR");
        }
        return check;
    }

    private void detailItem(DataShopItemModel data) {
        gson = new Gson();
        String json = gson.toJson(data);
        Intent intent = new Intent(context, DetailShop.class);
        intent.putExtra("DATA", json);
        context.startActivity(intent);

    }

    private void showLayoutCart(final ViewHolder viewHolder, final DataShopItemModel data) {
        viewHolder.layoutCart.setVisibility(View.VISIBLE);
        viewHolder.beli.setVisibility(View.GONE);

        if (viewHolder.layoutCart.getVisibility() == View.VISIBLE) {
            viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "ADD", Toast.LENGTH_SHORT).show();
                    int quantity = 1;
                    quantity = quantity + 1;
                    if (quantity > data.getStock()) {
                        viewHolder.btnAdd.setClickable(false);
                    } else {
                        if (checkRecordDb(data.getId())) {
                            Log.i("ADD_UPDATE", "MASUK");
                            displayQuantityAndUpdate(quantity, viewHolder, data.getId());
                        } else {
                            Log.i("ADD_INSERT", "MASUK");
                            displayQuantityAndInsert(quantity, viewHolder, data);
                        }
                    }
                }
            });

            viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "MINUS", Toast.LENGTH_SHORT).show();
                    int quantity = 1;
                    quantity = quantity - 1;
                    if (quantity <= 0) {
                        quantity = 0;
                        deleteRowCart(data.getId());
                        viewHolder.layoutCart.setVisibility(View.GONE);
                        viewHolder.beli.setVisibility(View.VISIBLE);
                    } else {
                        if (checkRecordDb(data.getId())) {
                            displayQuantityAndUpdate(quantity, viewHolder, data.getId());
                        } else {
                            displayQuantityAndInsert(quantity, viewHolder, data);
                        }
                    }
                }
            });
        }
    }

    private void deleteRowCart(int id) {
        try {
            cartOperations.openDb();
            cartOperations.deleteRow(String.valueOf(id));
            cartOperations.closeDb();
            Log.i("DELETE_ROW_ADAPTER", "SUCCESS");
        } catch (SQLException e) {
            Log.i("DELETE_ERROR_ADAPTER", "ERROR " + e.getMessage());
        }
    }

    private void displayQuantityAndUpdate(int quantity, ViewHolder viewHolder, int id) {
        CartModel carts = getDataCart(id);
        updateCart(carts, quantity);
        viewHolder.textQuantity.setText(String.valueOf(quantity));
    }

    private CartModel getDataCart(int id) {
        CartModel carts = null;
        try {
            cartOperations.openDb();
            carts = cartOperations.getCart(id);
            cartOperations.closeDb();
            Log.i("GET_DATA_ADAPTER", "SUCCESS");
        } catch (SQLException e) {
            Log.i("GET_DATA_ADAPTER", "ERROR " + e.getMessage());
        }
        return carts;
    }

    private void updateCart(CartModel carts, int quantity) {
        try {
            cartOperations.openDb();
            cartOperations.updateCart(new CartModel(carts.getIdProduct(), carts.getNameProduct(), carts.getStokProduct(), quantity));
            cartOperations.closeDb();
            Log.i("SQL_UPDATE_ADAPTER", "SUCCESS");
        } catch (SQLException e) {
            Log.i("SQL UPDATE ADAPTER", "ERROR " + e.getMessage());
        }
    }

    private void displayQuantityAndInsert(int quantity, ViewHolder viewHolder, DataShopItemModel data) {
        insertDataCart(data, quantity);
        viewHolder.textQuantity.setText(String.valueOf(quantity));
    }

    private void insertDataCart(DataShopItemModel data, int quantity) {
        try {
            cartOperations.openDb();
            CartModel carts = new CartModel(data.getId(), data.getName(), data.getStock(), quantity);
            cartOperations.insertCart(carts);
            cartOperations.closeDb();
        } catch (SQLException e) {
            Log.i("ERROR_INSERT_ADAPTER", "ERROR " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nama, harga, textQuantity;
        Button beli;
        CardView lihat;
        RelativeLayout layoutCart;
        ImageButton btnAdd, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgv_listItem_shop);
            nama = itemView.findViewById(R.id.tv_namaBarangListItem_shop);
            harga = itemView.findViewById(R.id.tv_hargaBarangListItem_shop);
            beli = itemView.findViewById(R.id.btn_beliItemShop);
            lihat = itemView.findViewById(R.id.btnLihat);
            layoutCart = itemView.findViewById(R.id.layout_addToCart_adapter);
            btnAdd = itemView.findViewById(R.id.btn_addCart_adapter);
            btnMinus = itemView.findViewById(R.id.btn_minusCart_adapter);
            textQuantity = itemView.findViewById(R.id.tv_jumlahBarang_adapter);
        }
    }

}
