package com.npe.youji.fragment.inbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.model.inbox.RootInboxModel;

import java.util.List;

public class AdapterInbox extends RecyclerView.Adapter<AdapterInbox.ViewHolder>  {

    Context context;
    List<RootInboxModel> items;
    OnRecyclerViewItemClick mListener;
    Gson gson = new Gson();
    String dataGson;

    public  interface OnRecyclerViewItemClick{
        void onItemClick(int position, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClick listener){
        mListener = listener;
    }

    public AdapterInbox(Context context, List<RootInboxModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_inbox, viewGroup, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RootInboxModel data = items.get(i);

        String[] spliTanggal = data.getCreated_at().split(" ");
        viewHolder.tvJudul.setText(data.getSubjek());
        viewHolder.tvDes.setText(data.getPesan());
        viewHolder.tvTanggal.setText(spliTanggal[0]);

        dataGson = gson.toJson(data);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvDes, tvTanggal;
        public ViewHolder(@NonNull View itemView, final OnRecyclerViewItemClick listener) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudulInboxList);
            tvDes = itemView.findViewById(R.id.tvDeskripsiInboxList);
            tvTanggal = itemView.findViewById(R.id.tvTanggalInboxList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position, dataGson);
                        }
                    }
                }
            });
        }
    }
}
