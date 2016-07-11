package com.cartop.android.core.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;

public abstract class ConnectivityReceiver extends BroadcastReceiver implements NetworkInfoListener {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        onReceiveNetworkInfo(false, networkConnected, activeNetworkInfo);
    }

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public abstract void onReceiveNetworkInfo(boolean connectionStateChanged, boolean networkConnected, NetworkInfo activeNetworkInfo);
}
