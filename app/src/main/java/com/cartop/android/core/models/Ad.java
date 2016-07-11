package com.cartop.android.core.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cartop.android.core.media.Category;
import com.cartop.android.core.utils.MetricUtils;
import com.cartop.android.core.widgets.TextureVideoView;
import com.cartop.android.helpers.MediaHelper;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Arrays;
import java.util.Date;

public class Ad implements Parcelable, Comparable<Ad> {

    //region fields
    @SerializedName("repeat")
    private RepeatType repeat;

    @SerializedName("repeat_every")
    private int repeat_every;

    @SerializedName("repeat_time_start")
    private Date repeatTimeStart;

    @SerializedName("repeat_time_end")
    private Date repeatTimeEnd;

    @SerializedName("repeat_on")
    private int[] repeatOn;

    @SerializedName("ad")
    private AdBody body;
    //endregion

    //region Constructors
    public Ad() {
    }
    //endregion

    //region Getters And Setters
    public RepeatType getRepeat() {
        return repeat;
    }

    public void setRepeat(RepeatType repeat) {
        this.repeat = repeat;
    }

    public int getRepeatEvery() {
        return repeat_every;
    }

    public void setRepeat_every(int repeat_every) {
        this.repeat_every = repeat_every;
    }

    public Date getRepeatTimeStart() {
        return repeatTimeStart;
    }

    public void setRepeatTimeStart(Date repeatTimeStart) {
        this.repeatTimeStart = repeatTimeStart;
    }

    public Date getRepeatTimeEnd() {
        return repeatTimeEnd;
    }

    public void setRepeatTimeEnd(Date repeatTimeEnd) {
        this.repeatTimeEnd = repeatTimeEnd;
    }

    public int[] getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(int[] repeatOn) {
        this.repeatOn = repeatOn;
    }

    public AdBody getBody() {
        return body;
    }

    public void setBody(AdBody body) {
        this.body = body;
    }
    //endregion

    //region Public Tools
    public View createView(Context context) {
        if (body != null) {
            switch (Category.getFromContentType(body.getContentType())) {
                case IMAGE:
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return imageView;
                case VIDEO:
                    TextureVideoView textureVideoView = new TextureVideoView(context);
                    textureVideoView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
                    textureVideoView.setLooping(true);
                    return textureVideoView;
            }
        }
        return null;
    }

    public void prepareViewLayouting(@NonNull View view, @Nullable Program program) {
        if (body != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 0);
            MetricUtils metricUtils = new MetricUtils(view.getContext());
            layoutParams.width = metricUtils.dp2px(body.getWidth());
            layoutParams.height = metricUtils.dp2px(body.getHeight());
            layoutParams.topMargin = program == null ? 0 : program.getPositionY() + body.getPositionY();
            layoutParams.leftMargin = program == null ? 0 : program.getPositionX() + body.getPositionX();
            view.setLayoutParams(layoutParams);
        }
    }

    public void configureViewDisplaying(@NonNull View view) {
        if (body != null)
            switch (Category.getFromContentType(body.getContentType())) {
                case IMAGE:
                    ImageView imageView = (ImageView) view;
                    Picasso picasso = Picasso.with(view.getContext());
                    RequestCreator requestCreator;
                    if (body.isContentFromAsset())
                        requestCreator = picasso.load(body.getContentUrl());
                    else requestCreator = picasso.load(MediaHelper.get().getAdFile(this));
                    requestCreator.into(imageView);
                    break;
                case VIDEO:
                    TextureVideoView textureVideoView = (TextureVideoView) view;
                    textureVideoView.setDataSource(MediaHelper.get().getAdFile(this).toString());
                    textureVideoView.play();
                    break;
            }
    }
    //endregion

    //region toString
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ad{");
        sb.append("repeat=").append(repeat);
        sb.append(", repeat_every=").append(repeat_every);
        sb.append(", repeatTimeStart=").append(repeatTimeStart);
        sb.append(", repeatTimeEnd=").append(repeatTimeEnd);
        sb.append(", repeatOn=").append(Arrays.toString(repeatOn));
        sb.append(", body=").append(body);
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
        dest.writeInt(this.repeat == null ? -1 : this.repeat.ordinal());
        dest.writeInt(this.repeat_every);
        dest.writeLong(repeatTimeStart != null ? repeatTimeStart.getTime() : -1);
        dest.writeLong(repeatTimeEnd != null ? repeatTimeEnd.getTime() : -1);
        dest.writeIntArray(this.repeatOn);
        dest.writeParcelable(this.body, 0);
    }

    protected Ad(Parcel in) {
        int tmpRepeat = in.readInt();
        this.repeat = tmpRepeat == -1 ? null : RepeatType.values()[tmpRepeat];
        this.repeat_every = in.readInt();
        long tmpRepeatTimeStart = in.readLong();
        this.repeatTimeStart = tmpRepeatTimeStart == -1 ? null : new Date(tmpRepeatTimeStart);
        long tmpRepeatTimeEnd = in.readLong();
        this.repeatTimeEnd = tmpRepeatTimeEnd == -1 ? null : new Date(tmpRepeatTimeEnd);
        this.repeatOn = in.createIntArray();
        this.body = in.readParcelable(AdBody.class.getClassLoader());
    }

    public static final Creator<Ad> CREATOR = new Creator<Ad>() {
        public Ad createFromParcel(Parcel source) {
            return new Ad(source);
        }

        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };
    //endregion

    //region Comparable Implementation
    @Override
    public int compareTo(@NonNull Ad another) {
        return body == null || another.body == null ? 0 : body.getId().compareTo(another.body.getId());
    }
    //endregion
}