package com.cartop.android.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Program implements Parcelable {

    //region fields
    @SerializedName("program_id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("position_x")
    private int positionX;

    @SerializedName("position_y")
    private int positionY;

    @SerializedName("show_count")
    private int showCount;

    @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("created_by")
    private String createdBy;

    @SerializedName("ads")
    private ArrayList<Ad> ads;
    //endregion

    //region Constructors
    public Program() {
    }
    //endregion

    //region Getters And Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ArrayList<Ad> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Ad> ads) {
        this.ads = ads;
    }
    //endregion

    public static Program getDefault() {
        Program program = new Program();
        program.id = -1;
        program.title = "Default";
        program.width = 1000;
        program.height = 1000;
        program.positionX = 0;
        program.positionY = 0;
        program.showCount = 0;
        program.status = null;
        program.createdAt = new Date();
        program.createdBy = "System";

        program.ads = new ArrayList<>();
        AdBody adBody;
        Ad ad;

        adBody = new AdBody();
        adBody.setId("default_1");
        adBody.setTitle("default ad 1");
        adBody.setWidth(200);
        adBody.setHeight(200);
        adBody.setPositionX(0);
        adBody.setPositionY(0);
        adBody.setContentType("image/jpeg");
        adBody.setContentUrl("file:///android_asset/default_program/ad_default_1.jpg");
        adBody.setCreatedAt(new Date());
        adBody.setCreatedBy("System");
        ad = new Ad();
        ad.setBody(adBody);
        program.ads.add(ad);

        return program;
    }

    //region toString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Program{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", positionX=").append(positionX);
        sb.append(", positionY=").append(positionY);
        sb.append(", showCount=").append(showCount);
        sb.append(", status='").append(status).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", ads=").append(ads);
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
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.positionX);
        dest.writeInt(this.positionY);
        dest.writeInt(this.showCount);
        dest.writeString(this.status);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeString(this.createdBy);
        dest.writeTypedList(ads);
    }

    protected Program(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.positionX = in.readInt();
        this.positionY = in.readInt();
        this.showCount = in.readInt();
        this.status = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.createdBy = in.readString();
        this.ads = in.createTypedArrayList(Ad.CREATOR);
    }

    public static final Parcelable.Creator<Program> CREATOR = new Parcelable.Creator<Program>() {
        public Program createFromParcel(Parcel source) {
            return new Program(source);
        }

        public Program[] newArray(int size) {
            return new Program[size];
        }
    };
    //endregion
}
