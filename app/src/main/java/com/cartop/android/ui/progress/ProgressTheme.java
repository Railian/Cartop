package com.cartop.android.ui.progress;

import android.content.res.Resources;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;

import com.cartop.android.R;

public enum ProgressTheme implements Parcelable {

    //region instances
    LIGHT(android.R.color.white, 0.8f, R.color.colorAccent, R.color.colorPrimaryDark),
    DARK(R.color.colorPrimaryDark, 0.8f, R.color.colorAccent, android.R.color.white);
    //endregion

    //region fields
    @ColorRes
    private int backgroundColorRes;
    private float backgroundAlpha;
    @ColorRes
    private int progressColorRes;
    @ColorRes
    private int messageColorRes;
    //endregion

    //region Constructors
    ProgressTheme(@ColorRes int backgroundColorRes, float backgroundAlpha, @ColorRes int progressColorRes, @ColorRes int messageColorRes) {
        this.backgroundColorRes = backgroundColorRes;
        this.backgroundAlpha = backgroundAlpha;
        this.progressColorRes = progressColorRes;
        this.messageColorRes = messageColorRes;
    }
    //endregion

    //region Getters
    public int getBackgroundColorRes() {
        return backgroundColorRes;
    }

    public int getBackgroundColor(Resources res) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return res.getColor(backgroundColorRes);
        else return res.getColor(backgroundColorRes, null);
    }

    public float getBackgroundAlpha() {
        return backgroundAlpha;
    }

    public int getProgressColorRes() {
        return progressColorRes;
    }

    public int getProgressColor(Resources res) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return res.getColor(progressColorRes);
        else return res.getColor(progressColorRes, null);
    }

    public int getMessageColorRes() {
        return messageColorRes;
    }

    public int getMessageColor(Resources res) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return res.getColor(messageColorRes);
        else return res.getColor(messageColorRes, null);
    }
    //endregion

    //region Parcelable Implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }

    public static final Parcelable.Creator<ProgressTheme> CREATOR = new Parcelable.Creator<ProgressTheme>() {
        @Override
        public ProgressTheme createFromParcel(Parcel in) {
            return ProgressTheme.values()[in.readInt()];
        }

        @Override
        public ProgressTheme[] newArray(int size) {
            return new ProgressTheme[size];
        }
    };
    //endregion
}
