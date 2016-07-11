package com.cartop.android.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.cartop.android.autoboot.BootService;
import com.cartop.android.core.connectivity.ConnectivityManager;
import com.cartop.android.settings.AppSettingsManager;
import com.cartop.android.ui.throwable.ThrowableActivity;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import io.fabric.sdk.android.Fabric;

public class App extends Application implements Thread.UncaughtExceptionHandler {

    //region constants
    private static final String TAG = App.class.getSimpleName();
    //endregion

    //region Life-Cycle
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        ConnectivityManager.init(this);
        AppSettingsManager.init(this);
        startService(new Intent(this, BootService.class));
    }
    //endregion

    //region UncaughtExceptionHandler Implementation
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d(TAG, "uncaughtException() called with: " + "thread = [" + thread + "], ex = [" + ex + "]");
        ex.printStackTrace();
        ThrowableActivity.start(this, ex);
        BootService.showRunningProgramIfNeed(this);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    //endregion
}