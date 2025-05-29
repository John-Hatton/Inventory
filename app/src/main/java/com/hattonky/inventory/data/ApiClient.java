package com.hattonky.inventory.data;

import okhttp3.*;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import java.io.InputStream;

public class ApiClient {
    private static final OkHttpClient client = new OkHttpClient();

    public interface ApiCallback {
        void onSuccess(String response);
        void onFailure(IOException e);
    }

    private static String getBaseUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String url = prefs.getString("server_url", null);
        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("Server URL not configured");
        }
        if (!url.endsWith("/")) url = url + "/";
        return url;
    }

    private static OkHttpClient getSafeClient(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources().openRawResource(
                context.getResources().getIdentifier("server", "raw", context.getPackageName()));
            X509Certificate ca = (X509Certificate) cf.generateCertificate(caInput);
            caInput.close();

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void login(Context context, String username, String password, ApiCallback callback) {
        String baseUrl = getBaseUrl(context);
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(baseUrl + "api/auth/login")
                .post(formBody)
                .build();
        OkHttpClient safeClient = getSafeClient(context);
        safeClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    public static void register(Context context, String username, String email, String password, ApiCallback callback) {
        String baseUrl = getBaseUrl(context);
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("email", email)
                .add("password", password)
                .build();
        // Log the request body for debugging
        if (formBody instanceof FormBody) {
            FormBody fb = (FormBody) formBody;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fb.size(); i++) {
                sb.append(fb.name(i)).append("=").append(fb.value(i)).append("&");
            }
            android.util.Log.d("ApiClient", "Register request body: " + sb.toString());
        }
        Request request = new Request.Builder()
                .url(baseUrl + "api/auth/register")
                .post(formBody)
                .build();
        OkHttpClient safeClient = getSafeClient(context);
        safeClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    // Fetch all users (admin only)
    public static void getAllUsers(Context context, ApiCallback callback) {
        String baseUrl = getBaseUrl(context);
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        if (jwt == null) {
            callback.onFailure(new IOException("JWT token not found"));
            return;
        }
        Request request = new Request.Builder()
                .url(baseUrl + "api/users")
                .get()
                .addHeader("Authorization", "Bearer " + jwt)
                .build();
        OkHttpClient safeClient = getSafeClient(context);
        safeClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    // Update user by ID (admin only)
    public static void updateUserById(Context context, String userId, String role, ApiCallback callback) {
        String baseUrl = getBaseUrl(context);
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        if (jwt == null) {
            callback.onFailure(new IOException("JWT token not found"));
            return;
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = "{\"role\":\"" + role + "\"}";
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(baseUrl + "api/users/" + userId)
                .put(body)
                .addHeader("Authorization", "Bearer " + jwt)
                .addHeader("Content-Type", "application/json")
                .build();
        OkHttpClient safeClient = getSafeClient(context);
        safeClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    // Delete user by ID (admin only)
    public static void deleteUserById(Context context, String userId, ApiCallback callback) {
        String baseUrl = getBaseUrl(context);
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        if (jwt == null) {
            callback.onFailure(new IOException("JWT token not found"));
            return;
        }
        Request request = new Request.Builder()
                .url(baseUrl + "api/users/" + userId)
                .delete()
                .addHeader("Authorization", "Bearer " + jwt)
                .build();
        OkHttpClient safeClient = getSafeClient(context);
        safeClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    // Add more API methods as needed
}

