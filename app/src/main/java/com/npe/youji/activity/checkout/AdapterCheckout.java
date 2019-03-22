package com.npe.youji.activity.checkout;

import android.content.Context;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final JoinModel data = items.get(i);

        if(checkQuantity(i) > 0){
            viewHolder.layoutCheckout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(data.getGambar())
                    .into(viewHolder.imgCheckout);
            viewHolder.tvNamaBarang.setText(String.valueOf(data.getKeterangan()));
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            viewHolder.tvHargaBarang.setText("Rp "+String.valueOf(decimalFormat.format(data.getHarga())));
            viewHolder.tvJumlahBarang.setText(String.valueOf(checkQuantity(i)));
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

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
//            btnAdd = itemView.findViewById(R.id.btn_addCart_checkout);
//            btnMinus = itemView.findViewById(R.id.btn_minusCart_checkout);

            layoutCheckout = itemView.findViewById(R.id.layoutOrderCheckout);
        }
    }

}
