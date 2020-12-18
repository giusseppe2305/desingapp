package com.optic.projectofinal.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.optic.projectofinal.utils.Utils;

public class AppService extends Service {
    @Override
    public void onCreate() {
        Log.e(Utils.TAG_LOG, "onTaskRemoved: onCreate" );

        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(Utils.TAG_LOG, "onTaskRemoved: onUnbind" );

        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);
        Log.e(Utils.TAG_LOG, "onTaskRemoved: onTaskRemoved" );
        //here you will get call when app close.
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(Utils.TAG_LOG, "onTaskRemoved: onBind" );

        return null;
    }
}