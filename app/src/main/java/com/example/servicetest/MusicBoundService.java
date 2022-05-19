package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;

public class MusicBoundService extends Service {
    private final IBinder mBinder = new LocalIBinder();

    public class LocalIBinder extends Binder {
        MusicBoundService getService() {
            return MusicBoundService.this;
        }
    }

    private MediaPlayer mPlayer;
    private long mRemainTime;
    private ICommunicate mICommunicate;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(this, R.raw.nhac);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mICommunicate.onCompleteMusic();
                }
            });
            mRemainTime = mPlayer.getDuration();
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        pauseMediaPlayer();
        return true;
    }

    /**
     * These methods is created for other components can communicate with it
     * @return
     */
    public String getCurrentTime() {
        int time = mPlayer.getCurrentPosition();
        int minutes = time / (60 * 1000);
        int seconds = (time / 1000) % 60;
        String str = String.format("%d:%02d", minutes, seconds);
        return str;
    }

    public int getCurrentTimeInt() {
        return mPlayer.getCurrentPosition();
    }

    public long getTimeDuration() {
        return mRemainTime;
    }

    public void startMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    public void pauseMediaPlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mRemainTime = mPlayer.getDuration() - mPlayer.getCurrentPosition();
        }
    }

    public void forward10Second() {
        if (mRemainTime >= 10000) {
            mPlayer.seekTo(getCurrentTimeInt() + 10000);
        } else {
            mPlayer.seekTo(mPlayer.getDuration());
        }
    }

    public void replay10Second() {
        if (getCurrentTimeInt() >= 10000) {
            mPlayer.seekTo(getCurrentTimeInt() - 10000);
        } else {
            mPlayer.seekTo(0);
        }
    }

    public void setmICommunicate(ICommunicate mICommunicate) {
        this.mICommunicate = mICommunicate;
    }
}
