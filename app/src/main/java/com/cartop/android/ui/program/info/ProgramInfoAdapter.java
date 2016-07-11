package com.cartop.android.ui.program.info;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cartop.android.R;
import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.Program;


public class ProgramInfoAdapter extends RecyclerView.Adapter<ProgramInfoViewHolder> implements ProgramInfoAdViewHolder.OnAdClickListener {

    //region constants
    private static final int VIEW_TYPE_TOP_OFFSET = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_AD = 3;
    private static final int VIEW_TYPE_BOTTOM_OFFSET = 4;
    //endregion

    //region Description
    private Program program;
    //endregion

    public void setProgram(Program program) {
        this.program = program;
        notifyDataSetChanged();
    }

    public void notifyAdChanged(Ad ad) {
        int index = program.getAds().indexOf(ad);
        notifyItemChanged(2 + index);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TOP_OFFSET : position == 1 ? VIEW_TYPE_HEADER :
                position == getItemCount() - 1 ? VIEW_TYPE_BOTTOM_OFFSET : VIEW_TYPE_AD;
    }

    @Override
    public ProgramInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_TOP_OFFSET:
                itemView = inflater.inflate(R.layout.item_program_info_top_offset, parent, false);
                return new ProgramInfoOffsetViewHolder(itemView);
            case VIEW_TYPE_HEADER:
                itemView = inflater.inflate(R.layout.item_program_info_header, parent, false);
                return new ProgramInfoHeaderViewHolder(itemView);
            case VIEW_TYPE_AD:
                itemView = inflater.inflate(R.layout.item_program_info_ad, parent, false);
                ProgramInfoAdViewHolder adHolder = new ProgramInfoAdViewHolder(itemView);
                adHolder.setOnAdClickListener(this);
                return adHolder;
            case VIEW_TYPE_BOTTOM_OFFSET:
                itemView = inflater.inflate(R.layout.item_program_info_bottom_offset, parent, false);
                return new ProgramInfoOffsetViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ProgramInfoViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                holder.configureHeaderWith(program);
                break;
            case VIEW_TYPE_AD:
                holder.configureAdWith(getAd(position));
                break;
        }
    }

    private Ad getAd(int adapterPosition) {
        return program.getAds().get(adapterPosition - 1/*top offset item*/ - 1/*header item*/);
    }

    @Override
    public int getItemCount() {
        return program == null ? 0 : 1/*top offset item*/ + 1/*header item*/ + (program.getAds() != null ? program.getAds().size() : 0) + 1/*bottom offset item*/;
    }

    //region OnAdClickListener
    private ProgramInfoAdViewHolder.OnAdClickListener onAdClickListener;

    public void setOnAdClickListener(ProgramInfoAdViewHolder.OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
    }

    @Override
    public void onLoadContentClick(Ad ad, int adapterPosition) {
        if (onAdClickListener != null)
            onAdClickListener.onLoadContentClick(getAd(adapterPosition), adapterPosition);
    }

    @Override
    public void onContentClick(Ad ad, int adapterPosition) {
        if (onAdClickListener != null)
            onAdClickListener.onContentClick(getAd(adapterPosition), adapterPosition);
    }

    @Override
    public void onContentLongClick(View v, Ad ad, int adapterPosition) {
        if (onAdClickListener != null)
            onAdClickListener.onContentLongClick(v, getAd(adapterPosition), adapterPosition);
    }
    //endregion
}