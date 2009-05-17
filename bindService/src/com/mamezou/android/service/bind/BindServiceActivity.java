package com.mamezou.android.service.bind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BindServiceActivity extends Activity {
    private Button bindButton;
    private Button unbindButton;
    private Button callButton;
    private IBindService service;

    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName,
                IBinder binder) {
            // binderからサービスを取得
            service = IBindService.Stub.asInterface(binder);
            setButtonEnabled(callButton, true);
            setButtonEnabled(unbindButton, true);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            setButtonEnabled(bindButton, true);
            setButtonEnabled(callButton, false);
            setButtonEnabled(unbindButton, false);
        }
    };

    private void unbind() {
        setButtonEnabled(bindButton, true);
        setButtonEnabled(callButton, false);
        setButtonEnabled(unbindButton, false);
        unbindService(conn);
    }

    private OnClickListener bindListener = new OnClickListener() {
        public void onClick(View view) {
            setButtonEnabled(bindButton, false);
            Intent intent = new Intent(IBindService.class.getName());
            bindService(intent, conn, BIND_AUTO_CREATE);
        }
    };

    private OnClickListener unbindListener = new OnClickListener() {
        public void onClick(View view) {
            unbind();
        }

    };
    private OnClickListener callListener = new OnClickListener() {
        public void onClick(View view) {
            try {
                String echoResult = service.echo("hoge");
                Toast.makeText(BindServiceActivity.this, 
                        "echo result = "
                        + echoResult, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                Log.e("ServiceExample", e.getMessage(), e);
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        bindButton = (Button) findViewById(R.id.bindButton);
        bindButton.setOnClickListener(bindListener);

        unbindButton = (Button) findViewById(R.id.unbindButton);
        unbindButton.setOnClickListener(unbindListener);

        callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(callListener);

        setButtonEnabled(unbindButton, false);
        setButtonEnabled(callButton, false);
    }

    @Override
    protected void onPause() {
        unbind();
        super.onPause();
    }
    
    private void setButtonEnabled(Button button, boolean enabled) {
        button.setEnabled(enabled);
        button.invalidate();
    }
}