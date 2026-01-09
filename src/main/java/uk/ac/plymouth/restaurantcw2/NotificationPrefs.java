package uk.ac.plymouth.restaurantcw2;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationPrefs {

    private static final String PREF = "notif_pref";
    private static final String KEY = "enabled";

    public static boolean isEnabled(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getBoolean(KEY, true);
    }

    public static void setEnabled(Context ctx, boolean enabled) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY, enabled).apply();
    }
}

