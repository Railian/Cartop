
package com.cartop.android.core.connectivity;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

public class ConnectivityManager {

    //region constants
    private static final String TAG = ConnectivityManager.class.getSimpleName();
    //endregion

    //region fields
    private boolean networkConnected;
    private NetworkInfo activeNetworkInfo;
    //endregion

    //region Singleton Implementation
    private static ConnectivityManager instance;

    private ConnectivityManager() {
    }

    public static void init(@NonNull Context context) {
        instance = new ConnectivityManager();
        context.registerReceiver(instance.connectivityReceiver, ConnectivityReceiver.getIntentFilter());
        android.net.ConnectivityManager defManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        instance.networkConnected = defManager.getActiveNetworkInfo() != null && defManager.getActiveNetworkInfo().isConnected();
    }

    public static ConnectivityManager get() {
        if (instance == null) {
            String message = "ConnectivityManager first must be initialized by calling method init(Context context)";
            throw new NullPointerException(message);
        } else return instance;
    }

    public static void release(@NonNull Context context) {
        if (instance != null) {
            context.unregisterReceiver(instance.connectivityReceiver);
            instance = null;
        }
    }
    //endregion

    //region Public Tools
    public boolean isNetworkConnected() {
        return networkConnected;
    }

    public NetworkInfo getActiveNetworkInfo() {
        return activeNetworkInfo;
    }
    //endregion

    //region NetworkInfoListener
    private NetworkInfoListener networkInfoListener;

    public void setNetworkInfoListener(NetworkInfoListener listener) {
        this.networkInfoListener = listener;
    }
    //endregion

    //region connectivityReceiver
    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver() {
        @Override
        public void onReceiveNetworkInfo(boolean connectionStateChanged, boolean networkConnected, NetworkInfo activeNetworkInfo) {
            Log.d(TAG, "networkConnected = " + networkConnected
                    + ", activeNetworkInfo = " + activeNetworkInfo);
            connectionStateChanged = ConnectivityManager.this.networkConnected != networkConnected;
            ConnectivityManager.this.networkConnected = networkConnected;
            ConnectivityManager.this.activeNetworkInfo = activeNetworkInfo;
            if (networkInfoListener != null)
                networkInfoListener.onReceiveNetworkInfo(connectionStateChanged, networkConnected, activeNetworkInfo);
        }
    };
    //endregion
}
