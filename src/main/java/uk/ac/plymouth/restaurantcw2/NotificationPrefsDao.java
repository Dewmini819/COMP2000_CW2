package uk.ac.plymouth.restaurantcw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NotificationPrefsDao {

    private final LocalDb helper;

    public NotificationPrefsDao(Context ctx) {
        helper = new LocalDb(ctx);
    }

    public static class Prefs {
        public boolean enabled;
        public boolean guestStatusChanges;
        public boolean staffNewReservations;

        public Prefs(boolean enabled, boolean guestStatusChanges, boolean staffNewReservations) {
            this.enabled = enabled;
            this.guestStatusChanges = guestStatusChanges;
            this.staffNewReservations = staffNewReservations;
        }
    }

    public Prefs getOrDefault(String username) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT enabled, guestStatusChanges, staffNewReservations FROM notif_prefs WHERE username=?",
                new String[]{username}
        );

        Prefs prefs;
        if (c.moveToFirst()) {
            prefs = new Prefs(
                    c.getInt(0) == 1,
                    c.getInt(1) == 1,
                    c.getInt(2) == 1
            );
        } else {

            prefs = new Prefs(true, true, true);

            save(username, prefs.enabled, prefs.guestStatusChanges, prefs.staffNewReservations);
        }

        c.close();
        db.close();
        return prefs;
    }

    public void save(String username, boolean enabled, boolean guestStatusChanges, boolean staffNewReservations) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("enabled", enabled ? 1 : 0);
        cv.put("guestStatusChanges", guestStatusChanges ? 1 : 0);
        cv.put("staffNewReservations", staffNewReservations ? 1 : 0);

        db.insertWithOnConflict("notif_prefs", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
}
