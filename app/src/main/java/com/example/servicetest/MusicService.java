package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    private static final String MUSIC_CHANNEL_ID = "MUSIC_CHANNEL_ID";
    private final String ACTION_PLAY = "ACTION_PLAY";
    private MediaPlayer mPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(this, R.raw.nhac);
            mPlayer.setLooping(true);
        }
        createChannel();
        Intent myIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, MUSIC_CHANNEL_ID)
                .setContentTitle("Now playing")
                .setContentText("...")
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();

        if (mPlayer != null) {
            mPlayer.start();
        }
        startForeground(2,notification);
        return START_STICKY;
    }

    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    MUSIC_CHANNEL_ID,
                    "Music now",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
