package com.npe.youji.activity.inbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.npe.youji.R;
import com.npe.youji.model.inbox.RootInboxModel;

public class DetailInbox extends AppCompatActivity {
    TextView tvJudul, tvDes, tvTanggal;
    Gson gson;
    RootInboxModel dataInbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inbox);
        getSupportActionBar().setTitle("Detail Inbox");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //inisialisasi
        gson = new Gson();
        tvJudul = findViewById(R.id.tvJudulDetailInbox);
        tvDes = findViewById(R.id.tvDeskripsiDetailInbox);
        tvTanggal = findViewById(R.id.tvTanggalDetailInbox);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            Log.i("Data2Inbox", extra.getString("DataInbox"));
            dataInbox = gson.fromJson(extra.getString("DataInbox"), RootInboxModel.class);
            initDetail(dataInbox);
        }


    }

    private void initDetail(RootInboxModel dataInbox) {
        tvJudul.setText(dataInbox.getSubjek());
        String[] tanggal = dataInbox.getCreated_at().split(" ");
        tvTanggal.setText(tanggal[0]);
        tvDes.setText(dataInbox.getPesan());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
