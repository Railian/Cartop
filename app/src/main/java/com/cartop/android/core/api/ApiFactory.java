package com.cartop.android.core.api;

import android.support.annotation.NonNull;

import com.cartop.android.core.models.RepeatType;
import com.cartop.android.core.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    //region constants
    private static final String BASE_API_URL = "http://dev-api.rapidus-services.com";
    public static final String API_PREFIX = "/api/v1/cartop";

    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int WRITE_TIMEOUT = 120_000;
    private static final int READ_TIMEOUT = 60_000;

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .build();

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(RepeatType.class, new RepeatType.Deserializer())
            .registerTypeAdapter(Date.class, new DateUtils.Serializer())
            .registerTypeAdapter(Date.class, new DateUtils.Deserializer())
            .create();
    //endregion

    //region Public Tools
    public static ApiService getApiService() {
        return getRetrofit(BASE_API_URL).create(ApiService.class);
    }
    //endregion

    //region Private Tools
    @NonNull
    private static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .client(CLIENT)
                .build();
    }
    //endregion
}