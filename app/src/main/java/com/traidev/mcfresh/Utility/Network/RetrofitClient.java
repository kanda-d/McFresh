package com.traidev.mcfresh.Utility.Network;


import com.traidev.mcfresh.Utility.Main_Interface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "https://www.traidev.com/LIVE_APPS/MCF/";
    public static final String BASE_URL_A = "http://mcfresh.in/panel/content/";

    private static RetrofitClient mInstance;
    private static Retrofit retrofit;

    private RetrofitClient()
    {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }


    public static Retrofit getApiClient()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;

    }


    public static synchronized RetrofitClient getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Main_Interface getApi()
    {
        return retrofit.create(Main_Interface.class);
    }


}


