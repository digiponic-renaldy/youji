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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.activity.DetailShop;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.DataShopItemModel;
import com.npe.youji.model.shop.DataShopModel;
import com.npe.youji.model.shop.JoinModel;

import java.util.List;

public class AdapterShopItem extends RecyclerView.Adapter<AdapterShopItem.ViewHolder> {

    private Context context;
    private List<JoinModel> items;
    private Gson gson;
    Button btnBeli;
    RelativeLayout layoutCart;
    CartOperations cartOperations;
    public AdapterShopItem(Context context, List<JoinModel> items) {
        this.context = context;
        this.items = items;
        cartOperations = new CartOperations(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_shop_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final JoinModel data = items.get(i);
        btnBeli = viewHolder.beli;
        layoutCart = viewHolder.layoutCart;
        Glide.with(context)
                .load(data.getImage())
                .into(viewHolder.imageView);
        viewHolder.nama.setText(data.getName());
        viewHolder.harga.setText(String.valueOf(data.getSell_price()));


        /*viewHolder.beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
/*
        viewHolder.lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailItem(data);
            }
        });*/
    }



    private void detailItem(DataShopItemModel data) {
        gson = new Gson();
        String json = gson.toJson(data);
        Intent intent = new Intent(context, DetailShop.class);
        intent.putExtra("DATA", json);
        context.startActivity(intent);

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
