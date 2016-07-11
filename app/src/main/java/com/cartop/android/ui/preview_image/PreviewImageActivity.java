package com.cartop.android.ui.preview_image;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cartop.android.R;
import com.cartop.android.core.models.Ad;
import com.cartop.android.helpers.MediaHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import uk.co.senab.photoview.PhotoViewAttacher;

@SuppressWarnings("FieldCanBeLocal")
public class PreviewImageActivity extends AppCompatActivity {

    //region constants
    private static final String TAG = PreviewImageActivity.class.getSimpleName();
    public static final String EXTRA_AD = TAG + ".EXTRA_AD";
    //endregion

    //region widgets
    private ImageView ivImage;
    //endregion

    //region fields
    private PhotoViewAttacher attacher;
    //endregion

    //region extras
    private Ad ad;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

        ivImage = (ImageView) findViewById(R.id.activity_preview_image_ivImage);

        //noinspection ConstantConditions
        attacher = new PhotoViewAttacher(ivImage);

        ad = getIntent().getParcelableExtra(EXTRA_AD);
        RequestCreator requestCreator;
        if (ad.getBody().isContentFromAsset())
            requestCreator = Picasso.with(this).load(ad.getBody().getContentUrl());
        else requestCreator = Picasso.with(this).load(MediaHelper.get().getAdFile(ad));
        requestCreator.into(ivImage, picassoCallback);
    }

    Callback picassoCallback = new Callback() {
        @Override
        public void onSuccess() {
            attacher.update();
        }

        @Override
        public void onError() {
            attacher.update();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        initFullscreen();
    }

    private void initFullscreen() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < 19) decorView.setSystemUiVisibility(View.GONE);
        else decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
