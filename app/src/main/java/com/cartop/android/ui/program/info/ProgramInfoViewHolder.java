package com.cartop.android.ui.program.info;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cartop.android.core.models.Ad;
import com.cartop.android.core.models.Program;

public abstract class ProgramInfoViewHolder extends RecyclerView.ViewHolder {

    public ProgramInfoViewHolder(View itemView) {
        super(itemView);
    }

    void configureHeaderWith(Program program) {

    }

    void configureAdWith(Ad ad) {

    }
}
