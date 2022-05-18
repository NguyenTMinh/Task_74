package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicBoundService extends Service {
//    private final IBinder mBinder = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
