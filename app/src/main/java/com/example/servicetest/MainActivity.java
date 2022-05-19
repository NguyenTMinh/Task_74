package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;

import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.servicetest.MusicBoundService.LocalIBinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ICommunicate {
    private static final String TAG = "TAG";
    private Button mBTStart, mBTStop, mBTFore, mBTMusic;
    private ImageButton mBTForward, mBTReplay;
    private ToggleButton mBTPausePlay;
    private SeekBar mSBProgress;
    private TextView mTVTime;

    // thread to run and update current time of mediaPlayer
    CountDownTimer timer = null;

    //member variables for bound service
    private MusicBoundService mService;
    private boolean mBound = false;

    /*Defines callbacks for service binding, passed to bindService()*/
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalIBinder binder = (LocalIBinder) service;
            mService = binder.getService();
            mBound = true;
            mSBProgress.setMax((int) mService.getTimeDuration());
            mService.setmICommunicate(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

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

        mBTStart.setOnClickListener(this);
        mBTStop.setOnClickListener(this);
        mBTFore.setOnClickListener(this);
        mBTMusic.setOnClickListener(this);
        mBTForward.setOnClickListener(this);
        mBTReplay.setOnClickListener(this);

        mBTPausePlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mBound) {
                    if (isChecked) {
                        mService.startMediaPlayer();
                        startTimer(mService.getTimeDuration());
                    } else {
                        mService.pauseMediaPlayer();
                        timer.cancel();
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicBoundService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        unbindService(connection);
        mBound = false;
        timer.cancel();
    }

    @Override
    public void onClick(View v) {
        if (v == mBTStart) {
            startService(new Intent(this, ExampleService.class));
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
        } else if (v == mBTForward) {
            mService.forward10Second();
        } else if (v == mBTReplay) {
            mService.replay10Second();
        }
    }

    private void startTimer(long duration) {
        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTVTime.setText(mService.getCurrentTime());
                mSBProgress.setProgress(mService.getCurrentTimeInt());
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    @Override
    public void onCompleteMusic() {
        mBTPausePlay.setChecked(false);
    }
}