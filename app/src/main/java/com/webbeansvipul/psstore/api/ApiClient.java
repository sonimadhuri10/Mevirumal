package com.webbeansvipul.psstore.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //public  static  String Base_Url="http://webbeanstech.co.in/namodemo/index.php/";
  //  public  static  String Base_Url="http://medvirumal.webbeanstech.co.in/index.php/rest/";
    public  static  String Base_Url="http://medvirumal.webbeanstech.co.in/index.php/";
    private static Retrofit retrofit = null;

    public  static Retrofit getInstance()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
