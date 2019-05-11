package com.rtstudio.projetomeuapp.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Service extends android.app.Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                        Log.d("ServiceTeste", "run: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("ServiceTeste", e.getMessage());
                    }
                }
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }
}
