package itmediaengineering.duksung.ootd.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_CONNECTED = "connected";
    
    private static final String KEY_GROUP_CHANNEL_DISTINCT = "channelDistinct";
    private static final String KEY_AUTH = "auth";
    private static final String KEY_PID = "pid";

    private static Context mAppContext;

    // Prevent instantiation
    private PreferenceUtils() {
    }

    public static void init(Context appContext) {
        mAppContext = appContext;
    }

    private static SharedPreferences getSharedPreferences() {
        return mAppContext.getSharedPreferences("sendbird", Context.MODE_PRIVATE);
    }

    public static void setUserId(String userId) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_USER_ID, userId).apply();
    }

    public static String getUserId() {
        return getSharedPreferences().getString(KEY_USER_ID, "");
    }

    public static void setNickname(String nickname) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_NICKNAME, nickname).apply();
    }

    public static String getNickname() {
        return getSharedPreferences().getString(KEY_NICKNAME, "");
    }

    public static void setProviderUserId(String pid){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_PID, pid).apply();
    }

    public static String getProviderUserId(){
        return getSharedPreferences().getString(KEY_PID, "");
    }

    public static void setAuth(String auth){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_AUTH, auth).apply();
    }

    public static String getAuth(){
        return getSharedPreferences().getString(KEY_AUTH, "");
    }


    public static void setConnected(boolean tf) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_CONNECTED, tf).apply();
    }

    public static boolean getGroupChannelDistinct() {
        return getSharedPreferences().getBoolean(KEY_GROUP_CHANNEL_DISTINCT, true);
    }
}
