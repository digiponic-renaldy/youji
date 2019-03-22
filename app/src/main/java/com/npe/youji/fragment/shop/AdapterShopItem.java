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
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.activity.shop.DetailShop;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.JoinModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterShopItem extends RecyclerView.Adapter<AdapterShopItem.ViewHolder> {

    private Context context;
    private List<JoinModel> items;
    private List<JoinModel> itemsFilter;
    private Gson gson;
    private OnItemClickListener mListener;
    private ShopFragment fragment;
    int limit = 10;


    public interface OnItemClickListener {
        void onItemCick(int position, JoinModel data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    CartOperations cartOperations;
    ShopOperations shopOperations;

    public AdapterShopItem(Context context, List<JoinModel> items, ShopFragment fragment) {
        this.context = context;
        this.items = items;
        cartOperations = new CartOperations(context);
        shopOperations = new ShopOperations(context);

        itemsFilter = new ArrayList<>(items);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_shop_item, viewGroup,
                false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        RequestOptions myOptions = new RequestOptions()
                .fitCenter() // or centerCrop
                .override(100, 100);

        final JoinModel data = items.get(i);
        Glide.with(context)
                .load(data.getGambar())
                .apply(myOptions)
                .into(viewHolder.imageView);
        viewHolder.nama.setText(data.getKeterangan());
        viewHolder.nama.setSelected(true);
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        viewHolder.harga.setText("Rp " + String.valueOf(decimalFormat.format(data.getHarga())));
        viewHolder.label.setText(String.valueOf(data.getKategori()));
        viewHolder.satuan.setText("/ " + String.valueOf(data.getSatuan()));
        viewHolder.des.setText(String.valueOf(data.getDeskripsi()));
        //check quantity
        if (checkQuantity(i) > 0) {
            Log.i("QuantityBarang", "LebihDari0");
            displayExist(viewHolder, i);
            showLayoutCart(viewHolder, data, i);
        }
        if (data.getStok() == 0) {
            viewHolder.beli.setVisibility(View.GONE);
            viewHolder.textStokNull.setVisibility(View.VISIBLE);
        } else {
            viewHolder.beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLayoutCart(viewHolder, data, i);
                }
            });
        }
    }

    private void displayExist(ViewHolder viewHolder, int i) {
        viewHolder.textQuantity.setText(String.valueOf(checkQuantity(i)));
    }

    private void showLayoutCart(final ViewHolder viewHolder, final JoinModel data, final int position) {
        viewHolder.layoutCart.setVisibility(View.VISIBLE);
        viewHolder.beli.setVisibility(View.GONE);


        //insert data to cart
        if (checkQuantity(position) == 0) {
            //Toast.makeText(context, "Check", Toast.LENGTH_SHORT).show();
            insertFirst(position, 1);
        }
        //add
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(viewHolder, data, position);
            }
        });
        //minus
        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCart(viewHolder, data, position);
            }
        });
        //show checkout
//        fragment.showCheckOut();
    }

    private void addCart(ViewHolder viewHolder, JoinModel data, int position) {
        //insert table cart and update table shop
        String strQuantity = String.valueOf(checkQuantity(position));
        Log.i("StrQuantity", strQuantity);

        int quantity = Integer.parseInt(strQuantity);
        quantity = quantity + 1;
        if (quantity > data.getStok()) {
            viewHolder.btnAdd.setVisibility(View.GONE);
        } else {
            displayText(viewHolder, position, quantity);
        }
    }

    private void minusCart(ViewHolder viewHolder, JoinModel data, int position) {
        String strQuantity = String.valueOf(checkQuantity(position));
        Log.i("StrQuantity", strQuantity);
        int quantity = Integer.parseInt(strQuantity);
        quantity = quantity - 1;
        if (quantity <= 0) {
            viewHolder.layoutCart.setVisibility(View.GONE);
            viewHolder.beli.setVisibility(View.VISIBLE);
            deleteRowCart(position);
        } else {
            displayText(viewHolder, position, quantity);
        }
    }

    private void deleteRowCart(int position) {
        try {
            cartOperations.openDb();
            cartOperations.deleteRow(String.valueOf(items.get(position).getIdproduk()));
            Log.i("DeleteRow", "Masuk");
            cartOperations.closeDb();
        } catch (SQLException e) {
            Log.i("DeleteRowError", e.getMessage());
        }
    }

    private void insertFirst(int position, int quantity) {
        Log.i("Position", String.valueOf(position));
        try {
            cartOperations.openDb();
            CartModel carts = new CartModel(items.get(position).getIdproduk(), quantity);
            cartOperations.insertCart(carts);
            cartOperations.closeDb();
            refreshView(position);
        } catch (SQLException e) {
            Log.i("UpdateSqlFirst", e.getMessage());
        }
    }

    public void refreshView(int position) {
        joinData();
        notifyItemChanged(position);
        fragment.onResume();
    }

    private void joinData() {
        try {
            shopOperations.openDb();
            shopOperations.joinData();
            //insert to adapter
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.d("ERROR JOIN", e.getMessage());
        }
    }

    private int checkQuantity(int position) {
        int quantity = 0;
        try {
            shopOperations.openDb();
            try {
                quantity = shopOperations.joinData().get(position).getQuantity();
            } catch (IndexOutOfBoundsException e) {
                Log.i("NPEAdapterShopItem", e.getMessage());
            }
//            Log.i("DataShop", String.valueOf(shopOperations.joinData().get(position).getQuantity()));
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("SqlException", e.getMessage());
        }
        Log.i("QuantityCart", String.valueOf(quantity));
        return quantity;
    }


    private void displayText(ViewHolder viewHolder, int position, int quantity) {
        updateQuantity(position, quantity);
        viewHolder.textQuantity.setText(String.valueOf(checkQuantity(position)));
        refreshView(position);
    }

    private void updateQuantity(int position, int quantity) {
        try {
            cartOperations.openDb();
            CartModel cartModel = new CartModel(items.get(position).getIdproduk(), quantity);
            cartOperations.updateCart(cartModel);
            cartOperations.closeDb();
            Log.i("SqlUpate", "masuk");
        } catch (SQLException e) {
            Log.i("ErrorSqlUpdate", e.getMessage());
        }
    }

    public void detailItem(JoinModel data) {
        gson = new Gson();
        String json = gson.toJson(data);
        Intent intent = new Intent(context, DetailShop.class);
        intent.putExtra("DATA", json);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        if (items.size() > limit) {
            return limit;
        } else {
            return items.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nama, harga, textQuantity, textStokNull, label, satuan, des;
        Button beli;
        CardView lihat;
        RelativeLayout layoutCart;
        ImageButton btnAdd, btnMinus;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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
            textStokNull = itemView.findViewById(R.id.tvStokNull);
            label = itemView.findViewById(R.id.labelListItem);
            satuan = itemView.findViewById(R.id.tvSatuanListItem);
            des = itemView.findViewById(R.id.tvDesListItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemCick(position, items.get(position));
                        }
                    }
                }
            });
        }
    }


}
