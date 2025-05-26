package com.hattonky.inventory.data;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;

public class UserManager {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";
    private SharedPreferences prefs;

    public UserManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveLogin(String token, JSONObject user) {
        prefs.edit().putString(KEY_TOKEN, token).putString(KEY_USER, user.toString()).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public JSONObject getUser() {
        try {
            String userStr = prefs.getString(KEY_USER, null);
            return userStr != null ? new JSONObject(userStr) : null;
        } catch (Exception e) { return null; }
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
}
