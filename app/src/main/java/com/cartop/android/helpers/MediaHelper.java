package com.cartop.android.helpers;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.cartop.android.core.api.ApiManager;
import com.cartop.android.core.api.DelegatesSet;
import com.cartop.android.core.media.AdCallback;
import com.cartop.android.core.media.Extension;
import com.cartop.android.core.media.ProgramCallback;
import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.AdInfo;
import com.cartop.android.core.models.Program;
import com.cartop.android.core.utils.FileUtils;

import java.io.File;
import java.util.Iterator;

public class MediaHelper {

    //region constants
    @SuppressWarnings("unused")
    private static final String TAG = MediaHelper.class.getSimpleName();

    private static final String MAIN_STORAGE_NAME = "CarTop";
    private static final String MEDIA_STORAGE_NAME = "media";
    private static final String ADS_STORAGE_NAME = "ads";
    //endregion

    //region Singleton Implementation
    private static MediaHelper instance;

    private MediaHelper() {
    }

    public static MediaHelper get() {
        return instance == null ? instance = new MediaHelper() : instance;
    }
    //endregion

    //region Public Tools
    public File getAdFile(Ad ad) {
        String id = ad.getBody().getId();
        String extension = Extension.getFromContentType(ad.getBody().getContentType()).getSuffix();
        String fileName = String.format("ad_%s%s", id, extension);
        return new File(getAdsStorageDir(), fileName);
    }

    public File getAdInfoFile(Ad ad) {
        String id = ad.getBody().getId();
        String fileName = String.format("ad_%s.info", id);
        return new File(getAdsStorageDir(), fileName);
    }

    private DelegatesSet<AdCallback> adCallbacks = new DelegatesSet<>(AdCallback.class);

    public DelegatesSet<AdCallback> getAdCallbacks() {
        return adCallbacks;
    }

    public void downloadAdFile(@NonNull Ad ad, @NonNull final String tag) {
        File adFile = getAdFile(ad);
        File adInfoFile = getAdInfoFile(ad);
        AdInfo adInfo = FileUtils.readAsJson(adInfoFile, AdInfo.class);

        ApiManager.get().downloadFile(ad, adFile, adInfoFile, new AdCallback() {
            @Override
            public void onDownloadStarted(Ad ad) {
                adCallbacks.notify(tag).onDownloadStarted(ad);
            }

            @Override
            public void onDownloadProgressChanged(Ad ad, long fileSize, long fileSizeDownloaded) {
                adCallbacks.notify(tag).onDownloadProgressChanged(ad, fileSize, fileSizeDownloaded);
            }

            @Override
            public void onDownloadFailed(Ad ad, Throwable throwable) {
                adCallbacks.notify(tag).onDownloadFailed(ad, throwable);
            }

            @Override
            public void onDownloadCanceled(Ad ad) {
                adCallbacks.notify(tag).onDownloadCanceled(ad);
            }

            @Override
            public void onFileDownloaded(Ad ad, File file) {
                adCallbacks.notify(tag).onFileDownloaded(ad, file);
            }
        });
    }

    public void deleteAdFile(@NonNull Ad ad) {
        getAdFile(ad).delete();
        getAdInfoFile(ad).delete();
    }


        private DelegatesSet<ProgramCallback> programCallbacks = new DelegatesSet<>(ProgramCallback.class);

    public DelegatesSet<ProgramCallback> getProgramCallbacks() {
        return programCallbacks;
    }

    public void downloadProgram(@NonNull final Program program, final String programTag) {
        programCallbacks.notify(programTag).onProgramDownloadStarted(program);
        if (program.getAds() != null && program.getAds().size() != 0) {
            final Iterator<Ad> adIterator = program.getAds().iterator();
            final String adTag = String.valueOf(System.currentTimeMillis());
            final AdCallback adCallback = new AdCallback() {

                @Override
                public void onDownloadStarted(Ad ad) {
                    programCallbacks.notify(programTag).onAdDownloadStarted(ad);
                }

                @Override
                public void onDownloadProgressChanged(Ad ad, long fileSize, long fileSizeDownloaded) {
                    programCallbacks.notify(programTag).onAdDownloadProgressChanged(ad, fileSize, fileSizeDownloaded);
                }

                @Override
                public void onDownloadFailed(Ad ad, Throwable throwable) {
                    programCallbacks.notify(programTag).onAdDownloadFailed(ad, throwable);
                    if (adIterator.hasNext())
                        downloadNextProgramPart(program, adIterator, programTag, adTag);
                    else {
                        programCallbacks.notify(programTag).onProgramDownloaded(program);
                        adCallbacks.removeDelegate(this);
                    }
                }

                @Override
                public void onDownloadCanceled(Ad ad) {
                    programCallbacks.notify(programTag).onAdDownloadCanceled(ad);
                    if (adIterator.hasNext())
                        downloadNextProgramPart(program, adIterator, programTag, adTag);
                    else {
                        programCallbacks.notify(programTag).onProgramDownloaded(program);
                        adCallbacks.removeDelegate(this);
                    }
                }

                @Override
                public void onFileDownloaded(Ad ad, File file) {
                    adCallbacks.removeDelegate(this);
                    programCallbacks.notify(programTag).onAdFileDownloaded(ad, file);
                    if (adIterator.hasNext())
                        downloadNextProgramPart(program, adIterator, programTag, adTag);
                    else {
                        programCallbacks.notify(programTag).onProgramDownloaded(program);
                        adCallbacks.removeDelegate(this);
                    }
                }
            };
            adCallbacks.addDelegate(adTag, adCallback);
            downloadNextProgramPart(program, adIterator, programTag, adTag);
        } else programCallbacks.notify(programTag).onProgramDownloaded(program);
    }

    void downloadNextProgramPart(Program program, Iterator<Ad> adIterator, String programTag, String adTag) {
        Ad ad = adIterator.next();
        File adFile = getAdFile(ad);
        if (!ad.getBody().isContentFromAsset() && !adFile.exists()) downloadAdFile(ad, adTag);
        else {
            programCallbacks.notify(programTag).onAdFileDownloaded(ad, adFile);
            if (adIterator.hasNext())
                downloadNextProgramPart(program, adIterator, programTag, adTag);
            else programCallbacks.notify(programTag).onProgramDownloaded(program);
        }
    }

    public void cancelDownload(Ad ad) {
        ApiManager.get().cancelDownload(ad);
    }

    public void cancelDownload(Program program) {
        if (program != null && program.getAds() != null)
            for (Ad ad : program.getAds()) cancelDownload(ad);
    }
    //endregion

    //region Private Tools
    private static File getMediaStorageDir() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_STORAGE_NAME, MEDIA_STORAGE_NAME);
        //noinspection ResultOfMethodCallIgnored
        mediaStorageDir.mkdirs();
        return mediaStorageDir;
    }

    private static File getAdsStorageDir() {
        File adsStorageDir = new File(getMediaStorageDir(), ADS_STORAGE_NAME);
        //noinspection ResultOfMethodCallIgnored
        adsStorageDir.mkdirs();
        return adsStorageDir;
    }

    //endregion

}