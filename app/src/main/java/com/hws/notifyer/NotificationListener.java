package com.hws.notifyer;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.hws.notifyer.notification.NotifyerNotification;

public class NotificationListener extends NotificationListenerService {
    private String code;

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        code = intent.getStringExtra("CODE");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        if(!isNotificationAllowed(packageName)) {
            System.out.println("Package " + packageName + " not in allowed list");
            return;
        }

        Notification notification = sbn.getNotification();
        if (notification != null && notification.extras != null) {
            String title = notification.extras.getString(Notification.EXTRA_TITLE);
            String description = notification.extras.getString(Notification.EXTRA_TEXT);
            if (title != null && description != null) {
                NotifyerNotification notifyerNotification = new NotifyerNotification(title, description, code);
                AddNotification req = new AddNotification();
                req.sendPostRequest(notifyerNotification.strinfigy(), new AddNotification.OkHttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("AddNotification ENDPOINT: " + response);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("AddNotification: Failed to POST to endpoint -> " + e.getMessage());
                    }
                });
            }
        }
    }

    private boolean isNotificationAllowed(String packageName) {
        String allowed = "com.eldring.exominer"; //Exominer app
        //String allowed = "com.whatsapp"; //Exominer app

        return packageName.equals(allowed);
    }
}
