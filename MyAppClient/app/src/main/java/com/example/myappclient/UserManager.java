package com.example.myappclient;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_LOGGED_IN = "loggedIn";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean registerUser(String username, String password) {
        if (!userExists(username)) {
            editor.putString(username + KEY_USERNAME, username);
            editor.putString(username + KEY_PASSWORD, password);
            editor.apply();
            return true;
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        String savedPassword = sharedPreferences.getString(username + KEY_PASSWORD, "");
        if (savedPassword.equals(password)) {
            editor.putBoolean(KEY_LOGGED_IN, true);
            editor.apply();
            return true;
        }
        return false;
    }

    public void logoutUser() {
        editor.remove(KEY_LOGGED_IN);
        editor.apply();
    }

    private boolean userExists(String username) {
        String savedUsername = sharedPreferences.getString(username + KEY_USERNAME, "");
        return !savedUsername.isEmpty();
    }
}

