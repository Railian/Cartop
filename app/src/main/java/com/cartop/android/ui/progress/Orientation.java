package com.cartop.android.ui.progress;

import android.os.Parcel;
import android.os.Parcelable;

public enum Orientation implements Parcelable {

    //region instances
    HORIZONTAL, VERTICAL;
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

    public static final Parcelable.Creator<Orientation> CREATOR = new Parcelable.Creator<Orientation>() {
        @Override
        public Orientation createFromParcel(Parcel in) {
            return Orientation.values()[in.readInt()];
        }

        @Override
        public Orientation[] newArray(int size) {
            return new Orientation[size];
        }
    };
    //endregion
}
