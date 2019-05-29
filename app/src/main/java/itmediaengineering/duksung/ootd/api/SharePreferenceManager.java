package itmediaengineering.duksung.ootd.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/*
이곳에는 앱 내에서 저장하고 있어야 할 목록을 관리
- 사용자 token
- 사용자 nickname
- 사용자 자기소개?
*/
public class SharePreferenceManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static SharedPreferences getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("Store", Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            return sharedPreferences;
        }
        return sharedPreferences;
    }

    public static void putString(String key, String data) {
        editor.putString(key, data);
        editor.commit();
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, "default");
    }

    public static void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
}
