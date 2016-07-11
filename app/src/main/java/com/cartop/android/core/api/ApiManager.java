package com.cartop.android.core.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cartop.android.core.media.AdCallback;
import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.AdInfo;
import com.cartop.android.core.models.Program;
import com.cartop.android.core.models.ProgramsPage;
import com.cartop.android.core.models.Token;
import com.cartop.android.core.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApiManager {

    //region constants
    public static final int API_REQUEST_DEFAULT = 0;

    private static final String GRANT_TYPE = "password";
    private static final String CLIENT_ID = "aQpz5uFJu6ypEGnwnwGK";
    private static final String CLIENT_SECRET = "j9NtgrP7ywCCydKSGjnW";
    private static final String USERNAME = "cartop@gmail.com";
    private static final String PASSWORD = "123456";

    private static final String ACCESS_TOKEN = "29099546cfd830831742e881649857a914c25ccc";
    //endregion

    //region fields
    private final ApiService apiService;
    private final DelegatesSet<ApiDelegate> delegatesSet;
    //endregion

    //region Singleton Implementation
    private static ApiManager apiManager;

    private ApiManager() {
        apiService = ApiFactory.getApiService();
        delegatesSet = new DelegatesSet<>(ApiDelegate.class);
    }

    public static ApiManager get() {
        return apiManager == null ? apiManager = new ApiManager() : apiManager;
    }
    //endregion

    //region Getters And Setters
    public DelegatesSet<ApiDelegate> getDelegatesSet() {
        return delegatesSet;
    }
    //endregion

    //region Requests
    public void getToken(final String tag, final int requestCode) {
        Call<Token> call = apiService.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD);
        call.enqueue(new ApiCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                delegatesSet.notify(tag).onReceiveToken(requestCode, token);
            }

            @Override
            public void onFailure(Throwable throwable) {
                delegatesSet.notify(tag).onFailure(requestCode, throwable);
            }
        });
    }

    public void getPrograms(final String tag, final int requestCode) {
        final int page = 1;
        final int limit = 100;
        Call<ProgramsPage> call = apiService.getPrograms(ACCESS_TOKEN, page, limit);
        call.enqueue(new ApiCallback<ProgramsPage>() {
            @Override
            public void onSuccess(ProgramsPage programsPage) {
                delegatesSet.notify(tag).onReceiveProgramsPage(requestCode, programsPage);
            }

            @Override
            public void onFailure(Throwable throwable) {
                delegatesSet.notify(tag).onFailure(requestCode, throwable);
            }
        });
    }

    public void getProgram(final String tag, final int requestCode, int programId) {
        Call<Program> call = apiService.getProgram(programId, ACCESS_TOKEN);
        call.enqueue(new ApiCallback<Program>() {
            @Override
            public void onSuccess(Program program) {
                delegatesSet.notify(tag).onReceiveProgram(requestCode, program);
            }

            @Override
            public void onFailure(Throwable throwable) {
                delegatesSet.notify(tag).onFailure(requestCode, throwable);
            }
        });
    }

    private Map<Ad, DownloadLargeFileTask> downloadFileTasks = new TreeMap<>();

    public void downloadFile(@NonNull final Ad ad, @NonNull final File file, @NonNull final File infoFile, @NonNull final AdCallback callback) {
        cancelDownload(ad);
        DownloadLargeFileTask downloadFileTask = new DownloadLargeFileTask(ad, file, infoFile, callback);
        downloadFileTasks.put(ad, downloadFileTask);
        downloadFileTask.execute();
    }

    public void cancelDownload(@NonNull final Ad ad) {
        DownloadLargeFileTask downloadFileTask = downloadFileTasks.remove(ad);
        if (downloadFileTask != null) downloadFileTask.cancel(true);
    }
    //endregion

    //region class DownloadLargeFileTask
    private class DownloadLargeFileTask extends AsyncTask<Void, Long, File> {

        //region fields
        private Ad ad;
        private File file;
        private File infoFile;
        private AdCallback callback;
        private Throwable throwable;
        //endregion

        //region Constructors
        public DownloadLargeFileTask(Ad ad, File file, File infoFile, AdCallback callback) {
            this.ad = ad;
            this.file = file;
            this.infoFile = infoFile;
            this.callback = callback;
        }
        //endregion

        //region AsyncTask Implementation
        @Override
        protected void onPreExecute() {
            callback.onDownloadStarted(ad);
        }

        @Override
        protected File doInBackground(Void... voids) {
            final Call<ResponseBody> call = apiService.downloadFile(ad.getBody().getContentUrl());
            try {
                ResponseBody body = call.execute().body();
                InputStream inputStream = null;
                OutputStream outputStream = null;
                boolean complete = false;
                try {

//                    long fileSize = body.contentLength();
//                    long downloadedSize = 0;

//                    AdInfo adInfo = FileUtils.readAsJson(infoFile, AdInfo.class);
//                     if (adInfo == null) adInfo = new AdInfo();

                    AdInfo adInfo = new AdInfo();
                    adInfo.setStatus(AdInfo.DownloadStatus.NOT_DOWNLOADED);

                    if (adInfo.getStatus() == AdInfo.DownloadStatus.DOWNLOADED && adInfo.getFileSize() == body.contentLength()) {
                        return file;
                    }
                    if (!(adInfo.getStatus() == AdInfo.DownloadStatus.DOWNLOADING && adInfo.getFileSize() == body.contentLength())) {
                        adInfo.setStatus(AdInfo.DownloadStatus.NOT_DOWNLOADED);
                        adInfo.setFileSize(body.contentLength());
                        adInfo.setDownloadedSize(0);
                    }
                    inputStream = body.byteStream();
                    //noinspection ResultOfMethodCallIgnored

                    if (adInfo.getStatus() == AdInfo.DownloadStatus.NOT_DOWNLOADED)
                        file.createNewFile();

                    adInfo.setStatus(AdInfo.DownloadStatus.DOWNLOADING);
                    FileUtils.writeAsJson(infoFile, adInfo);
                    outputStream = new FileOutputStream(file);


                    byte[] fileReader = new byte[4096];
                    for (int i = 0; i < adInfo.getDownloadedSize() / fileReader.length; i++)
                        inputStream.read(fileReader);
                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            adInfo.setStatus(AdInfo.DownloadStatus.DOWNLOADED);
                            FileUtils.writeAsJson(infoFile, adInfo);
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
                        adInfo.setDownloadedSize(adInfo.getDownloadedSize() + read);
                        FileUtils.writeAsJson(infoFile, adInfo);
                        publishProgress(adInfo.getFileSize(), adInfo.getDownloadedSize());
                    }
                    complete = true;
                    outputStream.flush();
                    return file;
                } catch (IOException e) {
                    throwable = e;
                } finally {
                    //noinspection ResultOfMethodCallIgnored
                    if (!complete) file.delete();
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                }
            } catch (
                    IOException e
                    )

            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            callback.onDownloadProgressChanged(ad, values[0], values[1]);
        }

        @Override
        protected void onPostExecute(File file) {
            downloadFileTasks.remove(ad);
            if (file != null) callback.onFileDownloaded(ad, file);
            else if (throwable != null) callback.onDownloadFailed(ad, throwable);
        }

        @Override
        protected void onCancelled() {
            Log.d("TAG", "onCancelled() called with: " + "");
            downloadFileTasks.remove(ad);
            callback.onDownloadCanceled(ad);
        }

        @Override
        protected void onCancelled(File file) {
            Log.d("TAG", "onCancelled() called with: " + "file = [" + file + "]");
            downloadFileTasks.remove(ad);
            callback.onDownloadCanceled(ad);
        }
        //endregion
    }
//endregion
}