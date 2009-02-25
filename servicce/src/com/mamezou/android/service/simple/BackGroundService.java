package com.mamezou.android.service.simple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class BackGroundService extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, R.string.start_message, Toast.LENGTH_SHORT).show();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20 * 1000);
                    stopSelf();
                } catch (InterruptedException e) {
                }

            }
        };
        t.start();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.stop_message, Toast.LENGTH_SHORT).show();
    }
}
