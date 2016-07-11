package com.cartop.android.core.models;

public class AdInfo {

    public enum DownloadStatus {NOT_DOWNLOADED, DOWNLOADING, DOWNLOADED}

    private DownloadStatus status;
    private long fileSize;
    private long downloadedSize;

    public AdInfo() {
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdInfo{");
        sb.append("status='").append(status).append('\'');
        sb.append(", fileSize=").append(fileSize);
        sb.append(", downloadedSize=").append(downloadedSize);
        sb.append('}');
        return sb.toString();
    }
}
