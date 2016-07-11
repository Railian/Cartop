package com.cartop.android.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AdBody implements Parcelable {

    //region fields
    @SerializedName("_id")
    private String id;

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

    @SerializedName("content_type")
    private String contentType;

    @SerializedName("content_url")
    private String contentUrl;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("created_by")
    private String createdBy;
    //endregion

    //region Constructors
    public AdBody() {
    }
    //endregion

    //region Getters And Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
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
    //endregion

    //region Public Tools
    public boolean isContentFromAsset() {
        return contentUrl != null && contentUrl.contains("file:///android_asset/");
    }
    //endregion

    //region toString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdBody{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", positionX=").append(positionX);
        sb.append(", positionY=").append(positionY);
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", contentUrl='").append(contentUrl).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", createdBy='").append(createdBy).append('\'');
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
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.positionX);
        dest.writeInt(this.positionY);
        dest.writeString(this.contentType);
        dest.writeString(this.contentUrl);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeString(this.createdBy);
    }

    protected AdBody(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.positionX = in.readInt();
        this.positionY = in.readInt();
        this.contentType = in.readString();
        this.contentUrl = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.createdBy = in.readString();
    }

    public static final Parcelable.Creator<AdBody> CREATOR = new Parcelable.Creator<AdBody>() {
        public AdBody createFromParcel(Parcel source) {
            return new AdBody(source);
        }

        public AdBody[] newArray(int size) {
            return new AdBody[size];
        }
    };
    //endregion
}
