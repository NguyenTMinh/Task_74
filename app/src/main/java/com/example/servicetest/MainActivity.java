package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBTStart, mBTStop, mBTFore, mBTMusic;
    private ImageButton mBTForward, mBTReplay;
    private ToggleButton mBTPausePlay;
    private SeekBar mSBProgress;
    private TextView mTVTime;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBTStart = findViewById(R.id.startButton);
        mBTStop = findViewById(R.id.stopButton);
        mBTFore = findViewById(R.id.startForegroundButton);
        mBTMusic = findViewById(R.id.startMusicWithFore);

        mTVTime = findViewById(R.id.layout_media_controller).findViewById(R.id.tv_time);
        mSBProgress = findViewById(R.id.layout_media_controller).findViewById(R.id.sb_media);
        mBTReplay = findViewById(R.id.layout_media_controller).findViewById(R.id.ib_replay);
        mBTPausePlay = findViewById(R.id.layout_media_controller).findViewById(R.id.ib_play_pause);
        mBTForward = findViewById(R.id.layout_media_controller).findViewById(R.id.ib_forward);

        mTVTime.setText("SSS");

        mBTStart.setOnClickListener(this);
        mBTStop.setOnClickListener(this);
        mBTFore.setOnClickListener(this);
        mBTMusic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mBTStart) {
            startService(new Intent(this, ExampleService.class));
//            MediaPlayer mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.start();
        } else if (v == mBTStop) {
            stopService(new Intent(this, ExampleService.class));
            stopService(new Intent(this, ForegroundService.class));
            stopService(new Intent(this, MusicService.class));
        } else if (v == mBTFore) {
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
        } else if (v == mBTMusic) {
            Intent intent = new Intent(this, MusicService.class);
            ContextCompat.startForegroundService(this, intent);
        }
    }
}