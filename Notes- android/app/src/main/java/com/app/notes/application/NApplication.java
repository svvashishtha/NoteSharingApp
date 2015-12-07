package com.app.notes.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.notes.baseObjects.UserObject;
import com.app.notes.extras.AppConstants;

/**
 * Created by Saurabh on 11-11-2015.
 */
public class NApplication implements AppConstants {
    static NApplication sInstance;
    int semNo = -1;

    public static NApplication getInstance() {
        if (sInstance == null)
            sInstance = new NApplication();
        return sInstance;
    }

    public static String getBaseUrl() {
        return "http://192.168.43.244:3000/api/notes/";
    }

    public boolean isLogin(Context context) {
        return context.getSharedPreferences(PREFERENCES, 0).getBoolean(IS_LOGIN, false);
    }

    public void setLoggedIn(Context context, boolean login) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCES, 0).edit();
            edit.putBoolean(IS_LOGIN, login);
            edit.apply();
        }
    }

    public void setAvatarUrl(Context context, String url) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCES, 0).edit();
            edit.putString(AVATAR_URL, url);
            edit.apply();
        }
    }

    public String getAvatarUrl(Context context) {
        return context.getSharedPreferences(PREFERENCES, 0).getString(AVATAR_URL, "");
    }

    public void setUserId(Context context, String id) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCES, 0).edit();
            edit.putString(USER_ID, id);
            edit.apply();
        }
    }

    public String getUserId(Context context) {
        return context.getSharedPreferences(PREFERENCES, 0).getString(USER_ID, "");
    }

    public void setSemNo(Context context, int semNo) {
        this.semNo = semNo;
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCES, 0).edit();
            edit.putInt(SEMESTER, semNo);
            edit.apply();
        }
    }

    public int getSemNo(Context context) {
        if (semNo == -1)
            semNo = context.getSharedPreferences(PREFERENCES, 0).getInt(SEMESTER, -1);
        return semNo;
    }

    public void setUserName(Context context, String name) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCES, 0).edit();
            edit.putString(USERNAME, name);
            edit.apply();
        }
    }

    public String getUserName(Context context) {
        return context.getSharedPreferences(PREFERENCES, 0).getString(USERNAME, "Saurabh Vashisht");
    }

    public void setUserEmail(Context context, String email) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCES, 0).edit();
            edit.putString(EMAIL, email);
            edit.apply();
        }
    }

    public String getUserEmail(Context context) {
        return context.getSharedPreferences(PREFERENCES, 0).getString(EMAIL, "svvashishtha@gmail.com");
    }


    public void login(Context context, UserObject userObject) {
        if (context != null) {
            setUserId(context, userObject.get_id());
            setAvatarUrl(context, userObject.getAvatarUrl());
            setLoggedIn(context, true);
            setUserName(context, userObject.getFirstName() + " " + userObject.getLastName());
            setUserEmail(context, userObject.getEmail());
        }
    }

    public void logout(Context context) {
        if (context != null) {
            setLoggedIn(context, false);
            setSemNo(context, -1);
        }
    }
}
