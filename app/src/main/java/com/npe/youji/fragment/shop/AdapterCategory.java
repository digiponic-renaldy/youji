package com.npe.youji.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.activity.DetailShop;
import com.npe.youji.model.dbsqlite.ShopOperations;
import com.npe.youji.model.shop.JoinModel;
import com.npe.youji.model.shop.menu.DataCategory;
import com.npe.youji.model.shop.menu.RootTipeKategoriModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    private Context context;
    private List<RootTipeKategoriModel> items;
    private Gson gson;
    ShopOperations shopOperations;
    ArrayList<JoinModel> dataJoin;

    public AdapterCategory(Context context, List<RootTipeKategoriModel> items) {
        this.context = context;
        shopOperations = new ShopOperations(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_shop_category, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        RootTipeKategoriModel data = items.get(i);
        //join data
        getDataJoin();

//        Glide.with(context)
//                .load(data.get)
//                .into(viewHolder.image);

        viewHolder.nama.setText(data.getKeterangan()    );

//        viewHolder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                toDetail(i);
//            }
//        });
    }


    private void getDataJoin() {
        try{
            shopOperations.openDb();
            dataJoin = shopOperations.joinData();
            shopOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorGetDataJoin", e.getMessage());
        }
    }


    private void toDetail(int position) {
        gson = new Gson();
        String json = gson.toJson(dataJoin.get(position));
        Intent intent = new Intent(context, DetailShop.class);
        intent.putExtra("DATA", json);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nama;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgv_listMenu_shop);
            nama = itemView.findViewById(R.id.tv_listMenu_shop);
        }
    }
}
