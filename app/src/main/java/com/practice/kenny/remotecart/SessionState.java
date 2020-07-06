package com.practice.kenny.remotecart;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionState {
    static final String PREF_USER_EMAIL = "logged_in_email";
    static final String PREF_USER_ID = "loggeg_in_id";
    static final String PREF_USER_STATUS = "loggeg_in_status";
    static final String PREF_USER_PASSWORD = "loggeg_in_pw";
    static final String PREF_USER_REF = "logged_in_ref";
    static final String PREF_USER_DEL = "logged_in_opt";
    static final String PREF_USER_PRICE = "logged_in_price";


    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedInUserEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_EMAIL, email);
        editor.commit();
    }

    public static String getLoggedInEmailUser(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_EMAIL, "");
    }

    public static void setLoggedInUserID(Context context, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ID, id);
        editor.commit();
    }

    public static String getLoggedInIDUser(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }

    public static void setLoggedInUserPassword(Context context, String pw) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_PASSWORD, pw);
        editor.commit();
    }

    public static String getLoggedInPasswordUser(Context context)
    {
        return getSharedPreferences(context).getString(PREF_USER_PASSWORD, "");
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_STATUS, false);
    }

    public static void clearLoggedInEmailAddress(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_STATUS);
        editor.remove(PREF_USER_PASSWORD);
        editor.remove(PREF_USER_EMAIL);
        editor.remove(PREF_USER_ID);
        editor.commit();
    }

    public static void setPrefUserRef(Context ctx, boolean status){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_REF, status);
        editor.commit();
    }

    public static boolean getPrefUserRef(Context ctx){
        return getSharedPreferences(ctx).getBoolean(PREF_USER_REF, false);
    }

    public static void setPrefUserDel(Context ctx, String status){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_DEL, status);
        editor.commit();
    }

    public static String getPrefUserRDel(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_DEL, "");
    }

    public static void setPrefUserPrice(Context ctx, String status){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PRICE, status);
        editor.commit();
    }

    public static String getPrefUserPrice(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_PRICE, "");
    }

}
