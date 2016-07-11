package com.cartop.android.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class BootSettingsHelper {

    //region constants
    private static String TAG = BootSettingsHelper.class.getSimpleName();
    private static final String PREFERENCES_NAME = TAG + ".BootSettings";

    private static final String PREF_PROGRAM_IS_RUNNING = "PREF_PROGRAM_IS_RUNNING";
    //endregion

    //region fields
    private SharedPreferences settings;
    //endregion

    //region Constructors
    public BootSettingsHelper(Context context) {
        settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    //endregion

    //region Public Tools
    public void setProgramIsRunning(boolean isRunning) {
        settings.edit().putBoolean(PREF_PROGRAM_IS_RUNNING, isRunning).apply();
    }

    public boolean isProgramRunning() {
        return settings.getBoolean(PREF_PROGRAM_IS_RUNNING, false);
    }
    //endregion
}
