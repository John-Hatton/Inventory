package com.hattonky.inventory.data;

import okhttp3.*;
import java.io.IOException;

public class ApiClient {
    private static final String BASE_URL = "http://your-server-hostname:port/api/";
    private static final OkHttpClient client = new OkHttpClient();

    public static void login(String email, String password, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}");
        Request request = new Request.Builder()
                .url(BASE_URL + "auth/login")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void register(String jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);
        Request request = new Request.Builder()
                .url(BASE_URL + "auth/register")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    // TODO: Add more API methods as needed
}
