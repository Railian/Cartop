package com.cartop.android.core.media;

import com.cartop.android.core.models.Ad;

import java.io.File;

public interface AdCallback {

    void onDownloadStarted(Ad ad);

    void onDownloadProgressChanged(Ad ad, long fileSize, long fileSizeDownloaded);

    void onDownloadFailed(Ad ad, Throwable throwable);

    void onDownloadCanceled(Ad ad);

    void onFileDownloaded(Ad ad, File file);
}