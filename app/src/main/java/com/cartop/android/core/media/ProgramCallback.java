package com.cartop.android.core.media;

import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.Program;

import java.io.File;

public interface ProgramCallback {

    void onProgramDownloadStarted(Program program);

    void onAdDownloadStarted(Ad ad);

    void onAdDownloadProgressChanged(Ad ad, long fileSize, long fileSizeDownloaded);

    void onAdDownloadFailed(Ad ad, Throwable throwable);

    void onAdDownloadCanceled(Ad ad);

    void onAdFileDownloaded(Ad ad, File file);

    void onProgramDownloaded(Program program);
}