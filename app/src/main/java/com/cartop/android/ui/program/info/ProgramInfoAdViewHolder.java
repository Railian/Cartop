package com.cartop.android.ui.program.info;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cartop.android.R;
import com.cartop.android.core.media.Extension;
import com.cartop.android.core.models.Ad;
import com.cartop.android.helpers.MediaHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;


public class ProgramInfoAdViewHolder extends ProgramInfoViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = ProgramInfoAdViewHolder.class.getSimpleName();

    private TextView tvTitle;
    private TextView tvId;
    private TextView tvWidthHeight;
    private TextView tvPositionXY;
    private TextView tvCreatedAt;
    private TextView tvCreatedBy;
    private TextView tvContentType;
    private View vLoadContent;
    private ImageView ivImage;
    private TextView tvRepeatType;
    private TextView tvRepeatEvery;
    private TextView tvRepeatTime;
    private TextView tvRepeatOn;

    public ProgramInfoAdViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvTitle);
        tvId = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvId);
        tvWidthHeight = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvWidthHeight);
        tvPositionXY = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvPositionXY);
        tvCreatedAt = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvCreatedAt);
        tvCreatedBy = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvCreatedBy);
        tvContentType = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvContentType);
        vLoadContent = itemView.findViewById(R.id.item_program_info_ad_vLoadContent);
        ivImage = (ImageView) itemView.findViewById(R.id.item_program_info_ad_ivImage);
        tvRepeatType = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvRepeatType);
        tvRepeatEvery = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvRepeatEvery);
        tvRepeatTime = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvRepeatTime);
        tvRepeatOn = (TextView) itemView.findViewById(R.id.item_program_info_ad_tvRepeatOn);

        vLoadContent.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        ivImage.setOnLongClickListener(this);
    }

    @Override
    void configureAdWith(Ad ad) {
        tvTitle.setText(String.format("Title: %s", ad.getBody().getTitle()));
        tvId.setText(String.format("id: %s", ad.getBody().getId()));
        tvWidthHeight.setText(String.format("width: %sdp, height: %sdp", ad.getBody().getWidth(), ad.getBody().getHeight()));
        tvPositionXY.setText(String.format("Position x: %spx, y: %spx", ad.getBody().getPositionX(), ad.getBody().getPositionY()));
        tvCreatedAt.setText(String.format("Created at: %s", SimpleDateFormat.getDateInstance().format(ad.getBody().getCreatedAt())));
        tvCreatedBy.setText(String.format("Created by: %s", ad.getBody().getCreatedBy()));
        tvContentType.setText(String.format("Content type: %s", ad.getBody().getContentType()));
        if (ad.getBody().isContentFromAsset()) {
            Picasso.with(itemView.getContext()).load(ad.getBody().getContentUrl()).into(ivImage);
            ivImage.setVisibility(View.VISIBLE);
            vLoadContent.setVisibility(View.GONE);
        } else {
            File adFile = MediaHelper.get().getAdFile(ad);
            if (adFile.exists()) {
                switch (Extension.getFromFile(adFile).getCategory()) {
                    case IMAGE:
                        Picasso.with(itemView.getContext()).load(adFile).into(ivImage);
                        break;
                    case VIDEO:
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(adFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                        ivImage.setImageBitmap(thumb);
                        break;
                }
                ivImage.setVisibility(View.VISIBLE);
                vLoadContent.setVisibility(View.GONE);
            } else {
                vLoadContent.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.GONE);
            }
        }
        tvRepeatType.setText(String.format("Repeat type: %s", ad.getRepeat()));
        tvRepeatEvery.setText(String.format("Repeat every: %s", ad.getRepeatEvery()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM:hh", Locale.getDefault());
        String repeatTimeStart = ad.getRepeatTimeStart() == null ? null : timeFormat.format(ad.getRepeatTimeStart());
        String repeatTimeEnd = ad.getRepeatTimeEnd() == null ? null : timeFormat.format(ad.getRepeatTimeEnd());
        tvRepeatTime.setText(String.format("Repeat time: %s - %s ", repeatTimeStart, repeatTimeEnd));
        tvRepeatOn.setText(String.format("Repeat on: %s", Arrays.toString(ad.getRepeatOn())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_program_info_ad_vLoadContent:
                if (onAdClickListener != null)
                    onAdClickListener.onLoadContentClick(null, getAdapterPosition());
                break;
            case R.id.item_program_info_ad_ivImage:
                if (onAdClickListener != null)
                    onAdClickListener.onContentClick(null, getAdapterPosition());
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.item_program_info_ad_ivImage:
                if (onAdClickListener != null)
                    onAdClickListener.onContentLongClick(v, null, getAdapterPosition());
                return true;
        }
        return false;
    }

    private OnAdClickListener onAdClickListener;

    public void setOnAdClickListener(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
    }

    public interface OnAdClickListener {

        void onLoadContentClick(Ad ad, int adapterPosition);

        void onContentClick(Ad ad, int adapterPosition);

        void onContentLongClick(View v, Ad ad, int adapterPosition);
    }
}
