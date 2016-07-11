package com.cartop.android.autoboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    //region BroadcastReceiver Methods
    @Override
    public void onReceive(Context context, Intent intent) {
        BootService.showRunningProgramIfNeed(context);
    }
    //endregion
}