package com.cartop.android.core.connectivity;

import android.net.NetworkInfo;

public interface NetworkInfoListener {

    void onReceiveNetworkInfo(boolean connectionStateChanged, boolean networkConnected, NetworkInfo activeNetworkInfo);
}
