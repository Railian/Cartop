package com.cartop.android.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.cartop.android.core.api.ApiFactory;
import com.cartop.android.core.models.Program;
import com.google.gson.JsonSyntaxException;

public class AppSettingsManager {

    //region constants
    private static String TAG = AppSettingsManager.class.getSimpleName();
    private static final String PREFERENCES_NAME = TAG + ".AppSettings";

    private static final String PREF_RUNNING_PROGRAM = "PREF_RUNNING_PROGRAM";
    //endregion

    //region fields
    private SharedPreferences settings;
    //endregion

    //region Singleton Implementation
    private static AppSettingsManager instance;

    public AppSettingsManager(Context context) {
        settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        instance = new AppSettingsManager(context);

    }

    public static AppSettingsManager get() {
        if (instance == null) {
            String message = "AppSettingsManager first must be initialized by calling method init(Context)";
            throw new NullPointerException(message);
        } else return instance;
    }
    //endregion

    //region Public Tools
    public void setRunningProgram(Program program) {
        settings.edit().putString(PREF_RUNNING_PROGRAM, program != null ? ApiFactory.GSON.toJson(program) : null).apply();
    }

    public Program getRunningProgram() {
        String json = settings.getString(PREF_RUNNING_PROGRAM, null);
        if (json == null) return null;
        try {
            Program program = ApiFactory.GSON.fromJson(json, Program.class);
            return program;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion
}
