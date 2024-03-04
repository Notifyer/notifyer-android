package com.hws.notifyer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNotification {
    private OkHttpClient client;

    public AddNotification() {
        client = new OkHttpClient();
    }

    public void sendPostRequest(String requestBody, final OkHttpCallback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);

        Request request = new Request.Builder()
                .url("https://notifyer.capicua.org.es/data")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.message());
                } else {
                    callback.onFailure(new IOException("Unexpected response code " + response.code() + ": " + response.message()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
        });
    }

    public interface OkHttpCallback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }
}
