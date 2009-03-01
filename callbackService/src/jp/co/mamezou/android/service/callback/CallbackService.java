package jp.co.mamezou.android.service.callback;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class CallbackService extends Service {

    private static final int TIMEOUT_MESSAGE = 1;
    private static final int INTERVAL = 1;
    
    private int count = 0;
    private RemoteCallbackList<ICallbackListener> listeners = new RemoteCallbackList<ICallbackListener>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TIMEOUT_MESSAGE) {
                int numListeners = listeners.beginBroadcast();
                for (int i = 0; i < numListeners; i++) {
                    try {
                        listeners.getBroadcastItem(i).receiveMessage(
                                "message NO." + count);
                    } catch (RemoteException e) {
                        Log.e("CallbackService", e.getMessage(), e);
                    }
                }
                listeners.finishBroadcast();
                count++;
                // 次回のメッセージを10秒後に送信
                handler.sendEmptyMessageDelayed(TIMEOUT_MESSAGE, INTERVAL * 1000);
            } else {
                super.dispatchMessage(msg);
            }
        }
    };

    @Override
    public void onCreate() {
        Log.d("CallbackService", "on create");
        handler.sendEmptyMessage(TIMEOUT_MESSAGE);
    }

    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private final ICallbackService.Stub stub = new ICallbackService.Stub() {
        public void addListener(ICallbackListener listener)
                throws RemoteException {
            listeners.register(listener);
        }

        public void removeListener(ICallbackListener listener)
                throws RemoteException {
            listeners.unregister(listener);
        }
    };
}
