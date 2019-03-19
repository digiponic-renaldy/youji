package com.npe.youji.model.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public static final String BASE_URL_LOCAL = "http://app.digiponic.co.id/youji/apiyouji/api/";
    public static Retrofit retrofit_local;

    public static Retrofit getRetrofitClientLocal(){
        if(retrofit_local == null){
            retrofit_local = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL_LOCAL)
                    .build();
        }
        return retrofit_local;
    }
}
