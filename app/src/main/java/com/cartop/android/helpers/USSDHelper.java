package com.cartop.android.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class USSDHelper {

    private Context context;

    public USSDHelper(Context context) {
        this.context = context;
    }

    public void call(String phoneNumber) {
        call(context, phoneNumber);
    }

    public static void call(Context context, String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("#", Uri.encode("#"));
        try {
            final String CALL_ACTION = "android.intent.action.CALL";
            final Uri uri = Uri.parse("tel:" + phoneNumber);
            context.startActivity(new Intent(CALL_ACTION, uri));
        } catch (Exception eExcept) {
            eExcept.printStackTrace();
        }
    }
}
