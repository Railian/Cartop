package com.cartop.android.ui.program.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cartop.android.R;
import com.cartop.android.core.models.Program;
import com.cartop.android.core.models.ProgramsPage;

public class ProgramsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private static final String STATE_PROGRAMS_PAGE = "STATE_PROGRAMS_PAGE";

    private ProgramsPage programsPage;

    public void setProgramsPage(ProgramsPage programsPage) {
        this.programsPage = programsPage;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return programsPage != null && programsPage.getData() != null ? programsPage.getData().size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return programsPage.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View spinView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            spinView = inflater.inflate(R.layout.item_program_info_spin, null);
        } else spinView = convertView;

        TextView tvProgramTitle = (TextView) spinView.findViewById(R.id.item_program_info_spin_tvProgramTitle);
        TextView tvProgramId = (TextView) spinView.findViewById(R.id.item_program_info_spin_tvProgramId);
        Program program = (Program) getItem(position);
        String programTitle = String.format("Program: %s", program.getTitle());
        String programId = String.format("id: %s", program.getId());
        tvProgramTitle.setText(programTitle);
        tvProgramId.setText(programId);
        tvProgramTitle.setSelected(true);
        tvProgramId.setSelected(true);
        return spinView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View spinView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            spinView = inflater.inflate(R.layout.item_program_info_spin, null);
        } else spinView = convertView;

        TextView tvProgramTitle = (TextView) spinView.findViewById(R.id.item_program_info_spin_tvProgramTitle);
        TextView tvProgramId = (TextView) spinView.findViewById(R.id.item_program_info_spin_tvProgramId);
        Program program = (Program) getItem(position);
        String programTitle = String.format("Program: %s", program.getTitle());
        String programId = String.format("id: %s", program.getId());
        tvProgramTitle.setText(programTitle);
        tvProgramId.setText(programId);
        tvProgramTitle.setSelected(false);
        tvProgramId.setSelected(false);
        return spinView;
    }

    public void saveState(Bundle outState) {
        outState.putParcelable(STATE_PROGRAMS_PAGE, programsPage);
    }

    public void restoreState(Bundle savedInstanceState) {
        programsPage = savedInstanceState.getParcelable(STATE_PROGRAMS_PAGE);
        notifyDataSetChanged();
    }
}
