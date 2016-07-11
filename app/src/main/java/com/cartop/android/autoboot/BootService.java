package com.cartop.android.autoboot;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.cartop.android.R;
import com.cartop.android.settings.BootSettingsHelper;
import com.cartop.android.ui.program.info.ProgramInfoActivity;
import com.cartop.android.ui.program.preview.ProgramActivity;

public class BootService extends Service {

    //region constants
    private static final String TAG = BootService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1;

    private static final String ACTION_SET_PROGRAM_IS_RUNNING = "ACTION_SET_PROGRAM_RUNNING";
    private static final String ACTION_SHOW_RUNNING_PROGRAM_IF_NEED = "ACTION_SHOW_RUNNING_PROGRAM_IF_NEED";

    private static final String EXTRA_PROGRAM_IS_RUNNING = "EXTRA_PROGRAM_IS_RUNNING";
    //endregion

    //region fields
    private NotificationManagerCompat notificationManager;
    private static BootSettingsHelper bootSettings;
    //endregion

    //region timerToShowProgram
    private CountDownTimer timerToShowProgram = new CountDownTimer(3000, 3000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            ProgramActivity.startFromOutside(BootService.this);
        }
    };
    //endregion

    //region Public Tools
    public static void setProgramIsRunning(Context context, boolean isRunning) {
        Intent serviceIntent = new Intent(context, BootService.class);
        serviceIntent.setAction(ACTION_SET_PROGRAM_IS_RUNNING);
        serviceIntent.putExtra(EXTRA_PROGRAM_IS_RUNNING, isRunning);
        context.startService(serviceIntent);
    }

    public static void showRunningProgramIfNeed(Context context) {
        Intent serviceIntent = new Intent(context, BootService.class);
        serviceIntent.setAction(ACTION_SHOW_RUNNING_PROGRAM_IF_NEED);
        context.startService(serviceIntent);
    }
    //endregion

    //region Life-Cycle
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
        bootSettings = new BootSettingsHelper(this);
        showInfoNotification();
        showRunningProgramIfNeed(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) switch (action) {
                case ACTION_SET_PROGRAM_IS_RUNNING:
                    boolean isProgramRunning = intent.getBooleanExtra(EXTRA_PROGRAM_IS_RUNNING, false);
                    bootSettings.setProgramIsRunning(isProgramRunning);
                    showInfoNotification();
                    break;
                case ACTION_SHOW_RUNNING_PROGRAM_IF_NEED:
                    if (bootSettings.isProgramRunning()) {
                        timerToShowProgram.cancel();
                        timerToShowProgram.start();
                    }
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(TAG, NOTIFICATION_ID);
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        BootService.showRunningProgramIfNeed(this);
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        BootService.showRunningProgramIfNeed(this);
        super.onTrimMemory(level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        BootService.showRunningProgramIfNeed(this);
        super.onTaskRemoved(rootIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //endregion

    //region Private Tools
    private void showInfoNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setContentTitle("CarTop")
                .setContentText("Boot-Service is running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false);

        if (bootSettings.isProgramRunning()) {
            Intent intent = new Intent(this, ProgramActivity.class);
            intent.setAction("ACTION_STOP_RUNNING_PROGRAM");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationBuilder.addAction(R.drawable.ic_stop_running_program, "Stop Running Program", PendingIntent.getActivity(this, 0, intent, 0));
        } else {
            Intent intent = new Intent(this, ProgramInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationBuilder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));
        }

        notificationManager.notify(TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
    //endregion
}
