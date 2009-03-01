package com.mamezou.android.service.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BackGroundServiceActivity extends Activity {
    private Button startButton;

    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(BackGroundServiceActivity.this,
                    BackGroundService.class);
            
            startService(intent);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(startListener);

    }
}