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
        showMessage(R.string.start_message);
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
        showMessage(R.string.stop_message);
    }

    private void showMessage(int messageId) {
        showMessage(getText(messageId));
    }

    private void showMessage(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
