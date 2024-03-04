package com.hws.notifyer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ENABLE_NOTIFICATION_ACCESS = 1;

    private Button startServiceButton;
    private Button stopServiceButton;
    private EditText codeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServiceButton = findViewById(R.id.startServiceButton);
        stopServiceButton = findViewById(R.id.stoptServiceButton);

        codeInput = findViewById(R.id.notifyerCode);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNotificationServiceEnabled()) {
                    // Prompt user to grant permission to listen to notifications
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE_ENABLE_NOTIFICATION_ACCESS);
                } else {
                    // Start the NotificationListenerService
                    startNotificationListenerService();
                }
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopNotificationListenerService();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_NOTIFICATION_ACCESS) {
            if (isNotificationServiceEnabled()) {
                startNotificationListenerService();
            } else {
                Toast.makeText(this, "Notification access not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNotificationServiceEnabled() {
        ComponentName cn = new ComponentName(this, NotificationListener.class);
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return flat != null && flat.contains(cn.flattenToString());
    }

    private void startNotificationListenerService() {
        //If the service is running, we stop it and then we start it
        if(isServiceRunning()) {
            stopNotificationListenerService();
        }

        String code = codeInput.getText().toString().toUpperCase();

        Intent intent = new Intent(this, NotificationListener.class);
        intent.putExtra("CODE", code);
        startService(intent);

        codeInput.getText().clear();

        Toast.makeText(this, "Notifyer activated", Toast.LENGTH_SHORT).show();
    }

    private void stopNotificationListenerService() {
        Intent intent = new Intent(this, NotificationListener.class);
        stopService(intent);

        Toast.makeText(this, "Notifyer deactivated", Toast.LENGTH_SHORT).show();
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationListener.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
