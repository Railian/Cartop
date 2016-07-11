package com.cartop.android.core.media;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public enum Extension {

    JPG(Category.IMAGE, ".jpg", ".jpeg"),
    PNG(Category.IMAGE, ".png"),
    GIF(Category.IMAGE, ".gif"),
    MOV(Category.VIDEO, ".mov"),
    MP4(Category.VIDEO, ".mp4"),
    UNKNOWN(null, "");

    private Category category;
    private List<String> suffixes;

    Extension(Category category, String... suffixes) {
        this.category = category;
        this.suffixes = Arrays.asList(suffixes);
    }

    public Category getCategory() {
        return category;
    }

    public String getSuffix() {
        return suffixes != null && suffixes.size() != 0 ? suffixes.get(0) : "";
    }

    public boolean checkSuffix(String suffix) {
        if (suffix != null) for (String patternSuffix : suffixes)
            if (patternSuffix.equals(suffix.toLowerCase())) return true;
        return false;
    }

    public static Extension identify(String suffix) {
        if (suffix != null) for (Extension extension : values())
            if (extension.checkSuffix(suffix)) return extension;
        return UNKNOWN;
    }

    public static String getSuffixFromFileName(@NonNull String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) return fileName.substring(dotIndex);
        else return null;
    }

    public static Extension getFromFileName(@NonNull String fileName) {
        return identify(getSuffixFromFileName(fileName));
    }

    public static Extension getFromFile(File file) {
        return file != null ? getFromFileName(file.getName()) : UNKNOWN;
    }

    public static Extension getFromContentType(String contentType) {
        if (contentType == null) return UNKNOWN;
        String suffix = "." + contentType.substring(contentType.indexOf("/") + 1);
        return identify(suffix);
    }
}
