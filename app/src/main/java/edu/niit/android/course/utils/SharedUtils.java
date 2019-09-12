package edu.niit.android.course.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharedUtils {
    private static final String SP_NAME = "userInfo";

    /**
     * 清除登录的信息
     * @param context
     */
    public static void clearLoginInfo(Context context) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isLogin", false)
                .putString("loginUser", "")
                .apply();
    }

    /**
     * 判断用户是否存在
     * @param username 用户名
     * @return true：存在，false：不存在
     */
    public static boolean isExist(Context context, String username) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return !TextUtils.isEmpty(sp.getString(username, ""));
    }
    /**
     * 保存字符串类型的值
     * @param key 关键字，类型String
     * @param value 值，类型String
     */
    public static void saveStrValue(Context context, String key, String value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    /**
     * 保存布尔类型的值
     * @param key 关键字，类型String
     * @param value 值，类型String
     */
    public static void saveBooleanValue(Context context, String key, Boolean value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static String readValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
