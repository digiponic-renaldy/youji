package com.npe.youji.fragment.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.activity.DetailShop;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.DataShopItemModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterShopItem extends RecyclerView.Adapter<AdapterShopItem.ViewHolder> {

    private Context context;
    private List<DataShopItemModel> items;
    private Gson gson;
    private CartOperations cartOperations;
    private CartModel cartModel;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final DataShopItemModel data = items.get(i);
        final int idProduct = data.id;
        final String namaProduct = data.name;
        final int stokProduct = data.stock;
        try{
            cartOperations.openDb();
            boolean check = cartOperations.checkRecordCart(idProduct);
            if(check){
                viewHolder.beli.setVisibility(View.GONE);
                viewHolder.coba.setVisibility(View.VISIBLE);
            }
            cartOperations.closeDb();
        }catch (SQLException e){
            Log.d("SQL CHECK", "ERROR");
        }
        Glide.with(context)
                .load(data.image)
                .into(viewHolder.imageView);
        viewHolder.nama.setText(data.getName());
        viewHolder.harga.setText(String.valueOf(data.getSell_price()));
        viewHolder.beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cartOperations.openDb();
                    cartModel = cartOperations.insertCart(new CartModel(idProduct, namaProduct, stokProduct));
                    Intent intent = new Intent(context, DetailShop.class);
                    long id = cartModel.getIdcart();
                    intent.putExtra("IDPRODUCT", id);
                    Log.d("IDADAPTER", String.valueOf(id));

                    cartOperations.closeDb();
                    context.startActivity(intent);
                    Log.d("SQL INSERT", "SUCCESS");
                } catch (SQLException e){
                    Log.d("SQL ERROR", "ERROR");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nama, harga;
        Button beli, coba;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgv_listItem_shop);
            nama = itemView.findViewById(R.id.tv_namaBarangListItem_shop);
            harga = itemView.findViewById(R.id.tv_hargaBarangListItem_shop);
            beli = itemView.findViewById(R.id.btn_beliItemShop);
            coba = itemView.findViewById(R.id.btnCoba);
        }
    }

    public void refreshView(int position) {
        notifyItemChanged(position);
    }

}
