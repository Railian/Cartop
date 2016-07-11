package com.cartop.android.core.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    private static final Gson GSON = new Gson();

    public static void writeAsJson(@NonNull File file, Object object) {
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(GSON.toJson(object));
            fileWriter.flush();
            fileWriter.close();
            Log.d(TAG, "writeAsJson() called with: " + "file = [" + file + "], object = [" + object + "]");
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static <T> T readAsJson(@NonNull File file, @NonNull Class<T> jsonClass) {
        if (!file.exists()) return null;
        try {
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Log.d(TAG, "readAsJson() called with: " + "file = [" + file + "], jsonClass = [" + jsonClass + "]");
            return GSON.fromJson(new String(buffer), jsonClass);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }
}