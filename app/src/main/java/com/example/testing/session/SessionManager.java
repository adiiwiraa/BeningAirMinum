package com.example.testing.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.testing.activities.LoginActivity;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginSession";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userId";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String email, int id) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_USER_ID, id);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
