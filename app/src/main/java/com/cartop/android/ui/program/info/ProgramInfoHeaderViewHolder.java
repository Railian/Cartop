package com.cartop.android.ui.program.info;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cartop.android.R;
import com.cartop.android.core.models.Program;

import java.text.SimpleDateFormat;

public class ProgramInfoHeaderViewHolder extends ProgramInfoViewHolder {

    private TextView tvTitle;
    private TextView tvId;
    private TextView tvWidthHeight;
    private TextView tvPositionXY;
    private TextView tvShowCount;
    private TextView tvStatus;
    private TextView tvCreatedAt;
    private TextView tvCreatedBy;

    public ProgramInfoHeaderViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.item_program_info_header_tvTitle);
        tvId = (TextView) itemView.findViewById(R.id.item_program_info_header_tvId);
        tvWidthHeight = (TextView) itemView.findViewById(R.id.item_program_info_header_tvWidthHeight);
        tvPositionXY = (TextView) itemView.findViewById(R.id.item_program_info_header_tvPositionXY);
        tvShowCount = (TextView) itemView.findViewById(R.id.item_program_info_header_tvShowCount);
        tvStatus = (TextView) itemView.findViewById(R.id.item_program_info_header_tvStatus);
        tvCreatedAt = (TextView) itemView.findViewById(R.id.item_program_info_header_tvCreatedAt);
        tvCreatedBy = (TextView) itemView.findViewById(R.id.item_program_info_header_tvCreatedBy);
    }

    void configureHeaderWith(@NonNull Program program) {
        tvTitle.setText(String.format("Title: %s", program.getTitle()));
        tvId.setText(String.format("id: %s", program.getId()));
        tvWidthHeight.setText(String.format("width: %sdp, height: %sdp", program.getWidth(), program.getHeight()));
        tvPositionXY.setText(String.format("Position x: %spx, y: %spx", program.getPositionX(), program.getPositionY()));
        tvShowCount.setText(String.format("Show count: %s", program.getShowCount()));
        tvStatus.setText(String.format("Status: %s", program.getStatus()));
        tvCreatedAt.setText(String.format("Created at: %s", SimpleDateFormat.getDateInstance().format(program.getCreatedAt())));
        tvCreatedBy.setText(String.format("Created by: %s", program.getCreatedBy()));
    }
}
