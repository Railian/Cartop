package com.cartop.android.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceInfoHelper {

    private static final String TAG = DeviceInfoHelper.class.getSimpleName();

    private Context context;
    private TelephonyManager telephonyManager;
    private LocationManager locationManager;

    private int signalStrength;
    private Location location;

    public void init(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    //region Public Tools;
    public String getIMEI() {
        return getIMEI(telephonyManager);
    }

    public String getPhoneNumber() {
        return getPhoneNumber(telephonyManager);
    }

    public Float getBatteryLevel() {
        return getBatteryLevel(context);
    }

    public static String getIMEI(TelephonyManager telephonyManager) {
        return telephonyManager.getDeviceId();
    }

    public static String getPhoneNumber(TelephonyManager telephonyManager) {
        return telephonyManager.getLine1Number();
    }

    private static Float getBatteryLevel(Context context) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        if (batteryStatus == null) return null;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return level / (float) scale;
    }
    //endregion

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            int signalStrengthValue;
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            DeviceInfoHelper.this.signalStrength = signalStrengthValue;
            Log.d(TAG, "onSignalStrengthsChanged: " + signalStrengthValue);
        }
    };

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            DeviceInfoHelper.this.location = location;
            Log.d(TAG, "onLocationChanged: " + location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
}