package com.cartop.android.ui.throwable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.cartop.android.R;

public class ThrowableActivity extends AppCompatActivity {

    private static final String EXTRA_THROWABLE = "EXTRA_THROWABLE";

    private TextView tvErrorContent;

    private Throwable throwable;

    public static void start(Context context, Throwable throwable) {
        Intent intent = new Intent(context, ThrowableActivity.class);
        intent.putExtra(EXTRA_THROWABLE, throwable);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throwable);

        throwable = (Throwable) getIntent().getSerializableExtra(EXTRA_THROWABLE);
        tvErrorContent = (TextView) findViewById(R.id.activity_error_tvErrorContent);
        tvErrorContent.setMovementMethod(new ScrollingMovementMethod());
        tvErrorContent.setText(stackTraceToCharSequence(throwable, this));
        setTitle(throwable.getClass().getSimpleName());
    }


    public static SpannableStringBuilder stackTraceToCharSequence(Throwable throwable, Context context) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        int start;
        int end;

        String name = throwable.getClass().getName();
        start = builder.length();
        builder.append(name).append("\n");
        end = builder.length();

        builder.setSpan(new RelativeSizeSpan(1.6f), start, end, 0);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
        builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)), start, end, 0);

        String msg = throwable.getLocalizedMessage();
        if (msg != null) {
            start = builder.length();
            builder.append(msg).append("\n");
            end = builder.length();

            builder.setSpan(new RelativeSizeSpan(1.2f), start, end, 0);
            builder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 0);
            builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)), start, end, 0);
        }

        StackTraceElement[] stack = throwable.getStackTrace();
        if (stack != null)
            for (StackTraceElement aStack : stack)
                builder.append("\t\tat ").append(aStack.toString()).append("\n");

        // Print suppressed exceptions indented one level deeper.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            if (throwable.getSuppressed() != null)
                for (Throwable suppressed : throwable.getSuppressed())
                    builder.append("\tSuppressed: ").append(stackTraceToCharSequence(suppressed, context));

        Throwable cause = throwable.getCause();
        if (cause != null) {
            start = builder.length();
            builder.append("\nCaused by: ");
            end = builder.length();

            builder.setSpan(new RelativeSizeSpan(1.6f), start, end, 0);
            builder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, 0);
            builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)), start, end, 0);

            builder.append(stackTraceToCharSequence(cause, context));
        }
        return builder;
    }
}
