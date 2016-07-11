package com.cartop.android.core.media;

public enum Category {

    IMAGE("image"),
    VIDEO("video"),
    UNKNOWN("");

    private String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean checkLabel(String label) {
        return this != UNKNOWN && this.label.equals(label.toLowerCase());
    }

    public static Category identify(String label) {
        if (label != null) for (Category category : values())
            if (category.checkLabel(label)) return category;
        return UNKNOWN;
    }

    public static Category getFromContentType(String contentType) {
        if (contentType == null) return UNKNOWN;
        String label = contentType.substring(0, contentType.indexOf("/"));
        return identify(label);
    }
}