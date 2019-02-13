package com.npe.youji.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.npe.youji.model.shop.DataShopItemModel;

import java.util.List;

public class AdapterShopItem extends RecyclerView.Adapter<AdapterShopItem.ViewHolder> {

    private Context context;
    private List<DataShopItemModel> items;
    private Gson gson;
    private CartOperations cartOperations;
    int quantity = 0;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onBeliClick(int position, ImageButton add, ImageButton btnAdd, Button beli, TextView textQuantity, RelativeLayout layoutCart);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdapterShopItem(Context context, List<DataShopItemModel> items) {
        this.context = context;
        this.items = items;
        this.cartOperations = new CartOperations(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_shop_item, viewGroup, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final DataShopItemModel data = items.get(i);
        final int idProduct = data.id;

        Glide.with(context)
                .load(data.image)
                .into(viewHolder.imageView);
        viewHolder.nama.setText(data.getName());
        viewHolder.harga.setText(String.valueOf(data.getSell_price()));
       /* viewHolder.beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayoutCart(viewHolder, data, i);
            }
        });

        viewHolder.lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailItem(data);
            }
        });*/
    }

    private void displayIfExist(long quantity, TextView textQuantity) {
        Log.i("IFEXIST", String.valueOf(quantity));
        textQuantity.setText(String.valueOf(quantity));
    }

    public void detailItem(DataShopItemModel data) {
        gson = new Gson();
        String json = gson.toJson(data);
        Intent intent = new Intent(context, DetailShop.class);
        intent.putExtra("DATA", json);
        context.startActivity(intent);
    }

    public void showLayoutCart(int position, ImageButton add, ImageButton btnAdd,
                               Button beli, TextView textQuantity, RelativeLayout layoutCart) {
        beli.setVisibility(View.GONE);
        layoutCart.setVisibility(View.VISIBLE);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nama, harga, textQuantity;
        Button beli;
        RelativeLayout layoutCart;
        ImageButton btnAdd, btnMinus;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgv_listItem_shop);
            nama = itemView.findViewById(R.id.tv_namaBarangListItem_shop);
            harga = itemView.findViewById(R.id.tv_hargaBarangListItem_shop);
            beli = itemView.findViewById(R.id.btn_beliItemShop);
            layoutCart = itemView.findViewById(R.id.layout_addToCart_adapter);
            btnAdd = itemView.findViewById(R.id.btn_addCart_adapter);
            btnMinus = itemView.findViewById(R.id.btn_minusCart_adapter);
            textQuantity = itemView.findViewById(R.id.tv_jumlahBarang_adapter);
            //card click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onBeliClick(position, btnAdd, btnMinus, beli,textQuantity,layoutCart);
                        }
                    }
                }
            });
        }
    }


}
