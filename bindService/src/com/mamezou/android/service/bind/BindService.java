package com.mamezou.android.service.bind;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BindService extends Service {

    private final IBindService.Stub stub = new IBindService.Stub() {
        public String echo(String message) throws android.os.RemoteException {

            showMessageToStatusBar("receive message [" + message + "]");
            return message;
        }
    };

    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void showMessageToStatusBar(String message) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.attention,
                message, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, BindServiceActivity.class), 0);

        notification.setLatestEventInfo(this, message, message, contentIntent);
        manager.notify(R.string.app_name, notification);
    }

}
