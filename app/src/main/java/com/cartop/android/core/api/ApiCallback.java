package com.cartop.android.core.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;

public abstract class ApiCallback<T> implements retrofit2.Callback<T> {

    //region constants
    private static final String TAG = ApiCallback.class.getSimpleName();
    //endregion

    //region Retrofit2 Callback Implementation
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T body = response.body();
        onSuccess(body);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.w(TAG, String.format("onFailure: call = %s, t = %s", call, t));
        onFailure(t);
    }
    //endregion

    //region New Callback Methods
    public abstract void onSuccess(T body);

    public void onFailure(Throwable throwable) {
    }
    //endregion
}
