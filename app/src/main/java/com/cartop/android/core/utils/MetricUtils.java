package com.cartop.android.core.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class MetricUtils {

    private Context context;

    //region Constructors
    public MetricUtils(Context context) {
        this.context = context;
    }
    //endregion

    //region dp2px
    public int dp2px(float dp) {
        return dp2px(context, dp);
    }

    public static int dp2px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
    //endregion

    //region sp2px
    public float sp2px(float sp) {
        return sp2px(context, sp);
    }

    public static int sp2px(Context context, float sp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }
    //endregion

    //region RelativeCords
    public static int getRelativeLeft(View view, View rootView) {
        if (view.getParent() == rootView) return view.getLeft();
        else return view.getLeft() + getRelativeLeft((View) view.getParent(), rootView);
    }

    public static int getRelativeTop(View view, View rootView) {
        if (view.getParent() == rootView) return view.getTop();
        else return view.getTop() + getRelativeTop((View) view.getParent(), rootView);
    }
    //endregion
}
