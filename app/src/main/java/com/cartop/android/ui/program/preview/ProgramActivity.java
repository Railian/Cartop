package com.cartop.android.ui.program.preview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cartop.android.R;
import com.cartop.android.settings.AppSettingsManager;
import com.cartop.android.autoboot.BootService;
import com.cartop.android.core.media.ProgramCallback;
import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.Program;
import com.cartop.android.core.utils.DateUtils;
import com.cartop.android.core.widgets.TextureVideoView;
import com.cartop.android.helpers.MediaHelper;
import com.cartop.android.ui.dialogs.StopProgramDialog;
import com.cartop.android.ui.program.info.ProgramInfoActivity;
import com.cartop.android.ui.progress.Orientation;
import com.cartop.android.ui.progress.ProgressFragment;
import com.cartop.android.ui.progress.ProgressTheme;

import java.io.File;
import java.util.Locale;

public class ProgramActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private static final String TAG = ProgramActivity.class.getSimpleName();

    public static final String EXTRA_PROGRAM = TAG + ".EXTRA_PROGRAM";

    private RelativeLayout contentView;

    public static Program program;

    public static void startFromOutside(Context context) {
        Intent intent = new Intent(context, ProgramActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MediaHelper.get().getProgramCallbacks().addDelegate(TAG, downloadProgramCallback);

        if (getIntent()!= null && "ACTION_STOP_RUNNING_PROGRAM".equals(getIntent().getAction()))
           onClick(null, DialogInterface.BUTTON_POSITIVE);

        setContentView(R.layout.activity_program);
        contentView = (RelativeLayout) findViewById(R.id.activity_program_contentView);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        program = AppSettingsManager.get().getRunningProgram();
        Log.d(TAG, "onCreate() called with: " + "program = [" + program + "]");
        if (program != null) MediaHelper.get().downloadProgram(program, TAG);

        // TODO: 17.06.16 remove
        //noinspection ConstantConditions
        findViewById(R.id.crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new NullPointerException("Force Crash in Program Preview Screen!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFullscreen();
    }

    boolean forceClose;

    @Override
    protected void onStop() {
        super.onStop();
        MediaHelper.get().cancelDownload(program);
        for (int i = 0; i < contentView.getChildCount(); i++) {
            View adView = contentView.getChildAt(i);
            if (adView instanceof TextureVideoView)
                ((TextureVideoView) adView).stop();
        }
        if (!forceClose) {
            BootService.showRunningProgramIfNeed(this);
            forceClose = true;
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        MediaHelper.get().getProgramCallbacks().removeDelegate(downloadProgramCallback);
        MediaHelper.get().cancelDownload(program);
        for (int i = 0; i < contentView.getChildCount(); i++) {
            View adView = contentView.getChildAt(i);
            if (adView instanceof TextureVideoView)
                ((TextureVideoView) adView).stop();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        StopProgramDialog.newInstance().show(getSupportFragmentManager(), StopProgramDialog.TAG);
    }

    private void initFullscreen() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < 19) decorView.setSystemUiVisibility(View.GONE);
        else decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                BootService.setProgramIsRunning(this, false);
                AppSettingsManager.get().setRunningProgram(null);
                // TODO: 17.06.16 move to static method
                startActivity(new Intent(this, ProgramInfoActivity.class));
                forceClose = true;
                finish();
                break;
        }
    }

    private ProgramCallback downloadProgramCallback = new ProgramCallback() {

        private int currentAdIndex = 1;

        @Override
        public void onProgramDownloadStarted(Program program) {
            showDownloadProgramProgress(program);
        }

        @Override
        public void onAdDownloadStarted(Ad ad) {
        }

        @Override
        public void onAdDownloadProgressChanged(Ad ad, long fileSize, long fileSizeDownloaded) {
            updateDownloadAdContentProgress(ad, fileSize, fileSizeDownloaded);
        }

        @Override
        public void onAdDownloadFailed(Ad ad, Throwable throwable) {
            currentAdIndex++;
        }

        @Override
        public void onAdDownloadCanceled(Ad ad) {
            currentAdIndex++;
        }

        @Override
        public void onAdFileDownloaded(Ad ad, File file) {
            currentAdIndex++;
        }

        @Override
        public void onProgramDownloaded(Program program) {
            Log.d(TAG, "onProgramDownloaded() called with: " + "program = [" + program + "]");
            hideProgress();
            for (Ad ad : program.getAds())
                if (ad != null) {
                    View adView = ad.createView(ProgramActivity.this);
                    if (adView != null) {
                        contentView.addView(adView);
                        ad.prepareViewLayouting(adView, program);
                        DateUtils.createTimer(ProgramActivity.this, program, ad, adView);
                        //   ad.configureViewDisplaying(adView);
                    }
                }
        }

        private ProgressFragment progressFragment;

        private void showDownloadProgramProgress(Program program) {
            hideProgress();
            progressFragment = ProgressFragment.newInstance(ProgressTheme.DARK, Orientation.HORIZONTAL, String.format("Loading program...\nprogram title: %s\nprogram id: %s", program.getTitle(), program.getId()), "Dismiss");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_program_vProgressContainer, progressFragment, ProgressFragment.TAG)
                    .commit();
        }

        private void updateDownloadAdContentProgress(Ad ad, long fileSize, long fileSizeDownloaded) {
            if (progressFragment != null)
                progressFragment.setMessage(String.format(Locale.getDefault(), "Loading program...\n%s of %s ads...\nad title: %s\nad id: %s\n%.2fMb of %.2fMb", currentAdIndex, program.getAds().size(), ad.getBody().getTitle(), ad.getBody().getId(), fileSizeDownloaded / 1024 / 1024., fileSize / 1024 / 1024.));
        }

        private void hideProgress() {
            if (progressFragment != null)
                getSupportFragmentManager().beginTransaction().remove(progressFragment).commit();
            progressFragment = null;
        }
    };
}