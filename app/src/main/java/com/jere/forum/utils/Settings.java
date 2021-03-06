package com.jere.forum.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jere.forum.App;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author jere
 */
public class Settings {
    private volatile static Settings instance;
    private SharedPreferences mSp;
    private static final String SETTINGS_SP_KEY = "SETTINGS_SP";
    private static final String NICKNAME_KEY = "NICKNAME";
    private static final String AVATAR_URL_KEY = "AVATAR_URL";
    private static final String IS_LOGIN_KEY = "IS_LOGIN";
    private static final String PHONE_NUMBER_KEY = "PHONE_NUMBER";
    private static final String PASSWORD_KEY = "PASSWORD";
    private static final String RE_PASSWORD_KEY = "RE_PASSWORD";
    private static final String IS_INIT_CHATS_DB_KEY = "IS_INIT_CHATS_DB";


    private Settings(Context context) {
        mSp = context.getSharedPreferences(SETTINGS_SP_KEY, MODE_PRIVATE);
    }

    public static Settings getInstance() {
        if (instance == null) {
            synchronized (Settings.class) {
                if (instance == null) {
                    instance = new Settings(App.getInstance().getApplicationContext());
                }
            }
        }
        return instance;
    }

    public String getNickname() {
        return mSp.getString(NICKNAME_KEY, "nickname");
    }

    public void setNickname(String nickname) {
        mSp.edit().putString(NICKNAME_KEY, nickname).apply();
    }

    public String getAvatarUrl() {
        return mSp.getString(AVATAR_URL_KEY, "");
    }

    public void setAvatarUrl(String avatarUrl) {
        mSp.edit().putString(AVATAR_URL_KEY, avatarUrl).apply();
    }

    public boolean getIsLogin() {
        return mSp.getBoolean(IS_LOGIN_KEY, false);
    }

    public void setIsLogin(boolean isLogin) {
        mSp.edit().putBoolean(IS_LOGIN_KEY, isLogin).apply();
    }

    public String getPhoneNumber() {
        return mSp.getString(PHONE_NUMBER_KEY, "");
    }

    public void setPhoneNumber(String phoneNumber) {
        mSp.edit().putString(PHONE_NUMBER_KEY, phoneNumber).apply();
    }

    public String getPassword() {
        return mSp.getString(PASSWORD_KEY, "");
    }

    public void setPassword(String password) {
        mSp.edit().putString(PASSWORD_KEY, password).apply();
    }

    public String getRePassword() {
        return mSp.getString(RE_PASSWORD_KEY, "");
    }

    public void setRePassword(String rePassword) {
        mSp.edit().putString(RE_PASSWORD_KEY, rePassword).apply();
    }

    public boolean getIsInitChatsDb() {
        return mSp.getBoolean(IS_INIT_CHATS_DB_KEY, false);
    }

    public void setIsInitChatsDb(boolean isInit) {
        mSp.edit().putBoolean(IS_INIT_CHATS_DB_KEY, isInit).apply();
    }
}