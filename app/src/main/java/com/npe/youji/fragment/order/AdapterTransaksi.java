package com.npe.youji.fragment.order;

import android.content.Context;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.npe.youji.R;
import com.npe.youji.model.dbsqlite.UserOperations;
import com.npe.youji.model.order.RootListTransaksiModel;
import com.npe.youji.model.user.UserModel;

import java.util.List;

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.ViewHolder> {

    Context context;
    List<RootListTransaksiModel> items;
    UserOperations userOperations;
    List<UserModel> userModels;
    String namaUser;

    public AdapterTransaksi(Context context, List<RootListTransaksiModel> items) {
        this.context = context;
        userOperations = new UserOperations(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RootListTransaksiModel data = items.get(i);

        if(chekUserData()){
            getDataUser();
            //set data
            viewHolder.tvNama.setText(namaUser);
            viewHolder.tvStatus.setText(data.getStatus());
            viewHolder.tvTanggal.setText(data.getTanggal());
            viewHolder.tvKode.setText(data.getKode());
        }
    }

    private void getDataUser() {
        try{
            userOperations.openDb();
            userModels = userOperations.getAllUser();
            namaUser = userModels.get(0).getNama();
            userOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorGetDataUser", e.getMessage());
        }
    }

    private boolean chekUserData() {
        boolean cek = false;
        try{
            userOperations.openDb();
            cek = userOperations.checkRecordUser();
            userOperations.closeDb();
        }catch (SQLException e){
            Log.i("ErrorCheckUserAdapter", e.getMessage());
        }
        return cek;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNama, tvKode, tvTanggal, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaListTransaksi);
            tvKode = itemView.findViewById(R.id.tvKodeListTransaksi);
            tvTanggal = itemView.findViewById(R.id.tvTanggalListTransaksi);
            tvStatus = itemView.findViewById(R.id.tvStatusListTransaksi);
        }
    }
}
