package com.cartop.android.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProgramsPage implements Parcelable {

    //region fields
    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("data")
    private ArrayList<Program> data;
    //endregion

    //region Constructors
    public ProgramsPage() {
    }
    //endregion

    //region Getters And Setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<Program> getData() {
        return data;
    }

    public void setData(ArrayList<Program> data) {
        this.data = data;
    }
    //endregion

    //region toString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProgramsPage{");
        sb.append("page=").append(page);
        sb.append(", totalPages=").append(totalPages);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
    //endregion

    //region Parcelable Implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.totalCount);
        dest.writeTypedList(this.data);
    }

    protected ProgramsPage(Parcel in) {
        this.page = in.readInt();
        this.totalPages = in.readInt();
        this.totalCount = in.readInt();
        this.data = in.createTypedArrayList(Program.CREATOR);
    }

    public static final Parcelable.Creator<ProgramsPage> CREATOR = new Parcelable.Creator<ProgramsPage>() {
        @Override
        public ProgramsPage createFromParcel(Parcel source) {
            return new ProgramsPage(source);
        }

        @Override
        public ProgramsPage[] newArray(int size) {
            return new ProgramsPage[size];
        }
    };
    //endregion
}
