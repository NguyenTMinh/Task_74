package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBTStart, mBTStop, mBTFore, mBTMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBTStart = findViewById(R.id.startButton);
        mBTStop = findViewById(R.id.stopButton);
        mBTFore = findViewById(R.id.startForegroundButton);
        mBTMusic = findViewById(R.id.startMusicWithFore);

        mBTStart.setOnClickListener(this);
        mBTStop.setOnClickListener(this);
        mBTFore.setOnClickListener(this);
        mBTMusic.setOnClickListener(this);
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
        }
    }
}