package com.cartop.android.ui.preview_video;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cartop.android.R;

public class PreviewVideoActivity extends AppCompatActivity {

    private static final String TAG = PreviewVideoActivity.class.getSimpleName();

    public static final String EXTRA_CONTENT_PATH = TAG + ".EXTRA_CONTENT_PATH";
    private static final String STATE_PLAYING = TAG + ".STATE_PLAYING";

    private VideoView vvVideo;

    private String contentPath;

    private boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);

        vvVideo = (VideoView) findViewById(R.id.activity_preview_video_vvVideo);

        if (savedInstanceState == null) playing = true;
        else playing = savedInstanceState.getBoolean(STATE_PLAYING);

        contentPath = getIntent().getStringExtra(EXTRA_CONTENT_PATH);
        vvVideo.setVideoPath(contentPath);
        MediaController controller = new MediaController(this);
        controller.setAnchorView(vvVideo);
        vvVideo.setMediaController(controller);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playing) vvVideo.start();
    }

    @Override
    protected void onPause() {
        playing = vvVideo.isPlaying();
        vvVideo.pause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(STATE_PLAYING, playing);
    }
}
