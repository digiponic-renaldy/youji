package com.npe.youji.activity.checkout;

import android.content.Context;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.npe.youji.R;
import com.npe.youji.model.dbsqlite.CartOperations;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.shop.CartModel;
import com.npe.youji.model.shop.JoinModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class AdapterCheckout extends RecyclerView.Adapter<AdapterCheckout.ViewHolder> {
    private Context context;
    private List<JoinModel> items;
    private ShopOperations shopOperations;
    private UserOperations userOperations;
    private CartOperations cartOperations;

    public AdapterCheckout(Context context, List<JoinModel> items) {
        this.context = context;
        this.items = items;
        //inisialisasi
        shopOperations = new ShopOperations(context);
        cartOperations = new CartOperations(context);
        userOperations = new UserOperations(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_checkout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final JoinModel data = items.get(i);

        if (checkQuantity(i) > 0) {
            viewHolder.layoutCheckout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(data.getGambar())
                    .into(viewHolder.imgCheckout);
            viewHolder.tvNamaBarang.setText(String.valueOf(data.getKeterangan()));
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            viewHolder.tvHargaBarang.setText("Rp " + String.valueOf(decimalFormat.format(data.getHarga())));
            viewHolder.tvJumlahBarang.setText(String.valueOf(checkQuantity(i)));
            //show layout

            //btn add
            viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCart(viewHolder, data, i);
                }
            });

            //btn minus
            if (data.getQuantity() == 1) {
                viewHolder.btnMinus.setVisibility(View.GONE);
            } else {
                viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minusCart(viewHolder, data, i);
                    }
                });
            }
        }
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

    private void displayText(ViewHolder viewHolder, int position, int quantity) {
        updateQuantity(position, quantity);
        viewHolder.tvJumlahBarang.setText(String.valueOf(checkQuantity(position)));
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

    private int checkQuantity(int position) {
        int quantity = 0;
        try {
            shopOperations.openDb();
            quantity = shopOperations.joinData().get(position).getQuantity();
            Log.i("DataShop", String.valueOf(shopOperations.joinData().get(position).getQuantity()));
            shopOperations.closeDb();
        } catch (SQLException e) {
            Log.i("SqlException", e.getMessage());
        }
        return quantity;
    }

    public void refreshView(int position) {
        joinData();
        notifyItemChanged(position);
        ((CheckoutActivity) context).onResume();
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

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCheckout;
        TextView tvNamaBarang, tvHargaBarang, tvJumlahBarang;
        ImageButton btnAdd, btnMinus;
        LinearLayout layoutCheckout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCheckout = itemView.findViewById(R.id.imgCheckout);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarangCheckout);
            tvHargaBarang = itemView.findViewById(R.id.tvHargaBarangCheckout);
            tvJumlahBarang = itemView.findViewById(R.id.jumlahBarang_checkout);
            btnAdd = itemView.findViewById(R.id.btn_addCart_checkout);
            btnMinus = itemView.findViewById(R.id.btn_minusCart_checkout);
            layoutCheckout = itemView.findViewById(R.id.layoutOrderCheckout);
        }
    }

}
