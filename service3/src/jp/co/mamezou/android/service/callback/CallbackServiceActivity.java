package jp.co.mamezou.android.service.callback;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CallbackServiceActivity extends Activity {
    private static final int CALLBACK_MESSAGE = 1;
    
    private ICallbackService service;
    private Button bindButton;
    private Button unbindButton;
    private TextView callbackText;

    private OnClickListener bindListener = new OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(CallbackServiceActivity.this,
                    CallbackService.class);
            bindService(intent, conn, BIND_AUTO_CREATE);
            setEnabled(bindButton, false);
        }
    };
    private OnClickListener unbindListener = new OnClickListener() {
        public void onClick(View view) {
            try {
                service.removeListener(listener);
            } catch (RemoteException e) {
                Log.e("CallbackServiceActivity", e.getMessage(), e);
            }
            unbindService(conn);
            setEnabled(bindButton, true);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == CALLBACK_MESSAGE) {
                callbackText.setText((String)msg.obj);
            } else {
                super.dispatchMessage(msg);
            }
        }
    };

    private ICallbackListener listener = new ICallbackListener.Stub() {
        public void receiveMessage(String message) throws RemoteException {
            handler.sendMessage(handler.obtainMessage(CALLBACK_MESSAGE, message));
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bindButton = (Button) findViewById(R.id.bindButton);
        bindButton.setOnClickListener(bindListener);
        unbindButton = (Button) findViewById(R.id.unbindButton);
        unbindButton.setOnClickListener(unbindListener);
        setEnabled(unbindButton, false);
        callbackText = (TextView) findViewById(R.id.callbackText);
    }

    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName,
                IBinder binder) {
            // binderからサービスを取得
            service = ICallbackService.Stub.asInterface(binder);
            try {
                // サービスにリスナを設定
                service.addListener(listener);
            } catch (RemoteException e) {
                Log.e("CallbackServiceActivity", e.getMessage(), e);
            }
            setEnabled(unbindButton, true);
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.invalidate();
    }
}