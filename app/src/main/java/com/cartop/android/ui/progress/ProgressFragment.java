package com.cartop.android.ui.progress;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cartop.android.R;

public class ProgressFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    //region constants
    public static final String TAG = ProgressFragment.class.getSimpleName();

    private static final String ARG_THEME = "ARG_THEME";
    private static final String ARG_ORIENTATION = "ARG_ORIENTATION";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_ACTION = "ARG_ACTION";
    private static final String STATE_MESSAGE = "STATE_MESSAGE";
    //endregion

    //region widgets
    private View vBackground;
    private LinearLayout llContent;
    private ProgressBar pbProgress;
    private TextView tvMessage;
    private Button btAction;
    //endregion

    //region arguments
    private ProgressTheme theme;
    private Orientation orientation;
    private String message;
    private String action;
    //endregion

    //region Creating New Instances
    public static ProgressFragment newInstance(@NonNull ProgressTheme theme, @NonNull Orientation orientation, @Nullable String message, @Nullable String action, @Nullable Fragment targetFragment) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_THEME, theme);
        args.putParcelable(ARG_ORIENTATION, orientation);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_ACTION, action);
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, 0);
        return fragment;
    }

    public static ProgressFragment newInstance(@NonNull ProgressTheme theme, @NonNull Orientation orientation, @Nullable String message, @Nullable String action) {
        return newInstance(theme, orientation, message, action, null);
    }

    public static ProgressFragment newInstance(@NonNull ProgressTheme theme, @NonNull Orientation orientation, @Nullable String message) {
        return newInstance(theme, orientation, message, null);
    }
    //endregion

    //region Life-Cycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = getArguments().getParcelable(ARG_THEME);
        orientation = getArguments().getParcelable(ARG_ORIENTATION);
        message = savedInstanceState == null ?
                getArguments().getString(ARG_MESSAGE) : savedInstanceState.getString(STATE_MESSAGE);
        action = getArguments().getString(ARG_ACTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vBackground = view.findViewById(R.id.fragment_progress_vBackground);
        llContent = (LinearLayout) view.findViewById(R.id.fragment_progress_llContent);
        pbProgress = (ProgressBar) view.findViewById(R.id.fragment_progress_pbProgress);
        tvMessage = (TextView) view.findViewById(R.id.fragment_progress_tvMessage);
        btAction = (Button) view.findViewById(R.id.fragment_progress_btAction);

        vBackground.setBackgroundResource(theme.getBackgroundColorRes());
        vBackground.setAlpha(theme.getBackgroundAlpha());
        vBackground.setOnTouchListener(this);
        //noinspection WrongConstant
        llContent.setOrientation(orientation.ordinal());
        pbProgress.getIndeterminateDrawable().setColorFilter(
                theme.getProgressColor(getResources()),
                android.graphics.PorterDuff.Mode.SRC_IN);
        tvMessage.setTextColor(theme.getMessageColor(getResources()));
        tvMessage.setText(message);
        btAction.setVisibility(action != null ? View.VISIBLE : View.GONE);
        btAction.setText(action);
        btAction.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_MESSAGE, message);
    }
    //endregion

    //region Public Tools
    public void setMessage(String message) {
        this.message = message;
        tvMessage.setText(message);
    }
    //endregion

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return v.getId() == R.id.fragment_progress_vBackground;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_progress_btAction:
                if (getTargetFragment() != null && getTargetFragment() instanceof ActionListener)
                    ((ActionListener) getTargetFragment()).onActionClick(this);
                else if (getActivity() instanceof ActionListener)
                    ((ActionListener) getActivity()).onActionClick(this);
                break;
        }
    }

    public interface ActionListener {
        void onActionClick(Fragment fragment);
    }
}