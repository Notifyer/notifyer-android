package com.hws.notifyer.notification;

import com.google.gson.Gson;

public class NotifyerNotification {
    private String title;
    private String description;
    private String idSession;

    public NotifyerNotification(String title, String description, String idSession) {
        this.title = title;
        this.description = description;
        this.idSession = idSession;
    }

    public String strinfigy() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
