package com.cartop.android.core.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.Program;
import com.cartop.android.core.models.RepeatType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    private static final String[] DATE_FORMATS = {"yyyy-MM-DD'T'HH:mm:ss.SSS'Z'", "HH:mm"};

    private static final long SECOND_IN_MILLIS = 1_000;
    private static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
    private static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    private static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;

    public static Timer createTimer(final Activity activity, Program program, final Ad ad, final View adView) {
        ad.configureViewDisplaying(adView);

        Timer timer = new Timer();
//        ad.setRepeat(RepeatType.DAY);
//        switch (ad.getRepeat()) {
//            case MONTH:
//                break;
//            case WEAK:
//                break;
//            case DAY: {
//                TimerTask showTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d(TAG, "showTask called");
//                                ad.configureViewDisplaying(adView);
//                                adView.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                };
//                TimerTask hideTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "hideTask called");
//                        adView.setVisibility(View.GONE);
//                    }
//                };
//                GregorianCalendar now = new GregorianCalendar();
//                GregorianCalendar whenStart = new GregorianCalendar();
//                whenStart.setTime(ad.getRepeatTimeStart());
//                whenStart.set(GregorianCalendar.YEAR, now.get(GregorianCalendar.YEAR));
//                whenStart.set(GregorianCalendar.DAY_OF_YEAR, now.get(GregorianCalendar.DAY_OF_YEAR));
//                timer.scheduleAtFixedRate(showTask, whenStart.getTime(), DAY_IN_MILLIS);
//                GregorianCalendar whenEnd = new GregorianCalendar();
//                whenEnd.setTime(ad.getRepeatTimeEnd());
//                whenEnd.set(GregorianCalendar.YEAR, now.get(GregorianCalendar.YEAR));
//                whenEnd.set(GregorianCalendar.MONTH, now.get(GregorianCalendar.MONTH));
//                whenEnd.set(GregorianCalendar.DAY_OF_MONTH, now.get(GregorianCalendar.DAY_OF_MONTH));
//                timer.scheduleAtFixedRate(hideTask, whenEnd.getTime(), DAY_IN_MILLIS);
//                break;
//            }
//            case HOUR:
//                break;
//            case MINUTE:
//                break;
//            case SECOND:
//                break;
//        }
        return timer;
    }

    public static class Deserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            for (String dateFormat : DATE_FORMATS)
                try {
                    return new SimpleDateFormat(dateFormat, Locale.getDefault()).parse(json.getAsString());
                } catch (ParseException ignored) {
                }
            return null;
        }
    }

    public static class Serializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.getDefault());
            return src == null ? JsonNull.INSTANCE : new JsonPrimitive(dateFormat.format(src));
        }
    }
}
