package uk.ac.plymouth.restaurantcw2;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF = "session_pref";
    private static final String KEY_LOGGED = "logged_in";
    private static final String KEY_USER = "username";
    private static final String KEY_ROLE = "role";

    public static void login(Context ctx, String username, String role) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit()
                .putBoolean(KEY_LOGGED, true)
                .putString(KEY_USER, username)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public static boolean isLoggedIn(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getBoolean(KEY_LOGGED, false);
    }

    public static String getUsername(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_USER, "");
    }

    public static String getRole(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_ROLE, "");
    }

    public static String getUsertype(Context ctx) {
        return getRole(ctx);
    }

    public static void logout(Context ctx) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }
}
