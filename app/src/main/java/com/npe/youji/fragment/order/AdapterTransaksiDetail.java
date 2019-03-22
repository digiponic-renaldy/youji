package com.npe.youji.fragment.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.npe.youji.R;
import com.npe.youji.model.order.DataListDetailTransaksiModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterTransaksiDetail extends RecyclerView.Adapter<AdapterTransaksiDetail.ViewHolder> {

    Context context;
    List<DataListDetailTransaksiModel> items;
    Locale locale;

    public AdapterTransaksiDetail(Context context, List<DataListDetailTransaksiModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_barang_detailorder, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DataListDetailTransaksiModel data = items.get(i);
        RequestOptions myOptions = new RequestOptions()
                .fitCenter() // or centerCrop
                .override(100, 100);

        Glide.with(context)
                .load(data.getGambar())
                .apply(myOptions)
                .into(viewHolder.imgBarang);
        locale = new Locale("in","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setMaximumFractionDigits(3);
        viewHolder.tvHarga.setText(String.valueOf(numberFormat.format(data.getHarga())));
        viewHolder.tvNama.setText(String.valueOf(data.getNama_produk()));
        viewHolder.tvJumlah.setText(String.valueOf(data.getKuantitas()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgBarang;
        TextView tvNama, tvHarga, tvJumlah;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBarang = itemView.findViewById(R.id.imgvListDetailTransaksi);
            tvNama = itemView.findViewById(R.id.tvNamaBarangListDetailTransaksi);
            tvHarga = itemView.findViewById(R.id.tvHargaBarangListDetailTransaksi);
            tvJumlah = itemView.findViewById(R.id.tvJumlahBarangListDetailTransaksi);
        }
    }
}
