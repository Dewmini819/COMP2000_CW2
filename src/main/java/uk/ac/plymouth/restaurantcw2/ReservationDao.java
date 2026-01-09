package uk.ac.plymouth.restaurantcw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReservationDao {
    private final LocalDb helper;

    public ReservationDao(Context ctx) {
        helper = new LocalDb(ctx);
    }

    public static class Reservation {
        public int id;
        public String username;
        public String date;      // YYYY-MM-DD
        public String time;      // HH:MM
        public int partySize;
        public String status;    // BOOKED / CANCELLED
        public String notes;

        public Reservation(int id, String username, String date, String time, int partySize, String status, String notes) {
            this.id = id;
            this.username = username;
            this.date = date;
            this.time = time;
            this.partySize = partySize;
            this.status = status;
            this.notes = notes;
        }
    }

    public long createReservation(String username, String date, String time, int partySize, String notes) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("partySize", partySize);
        cv.put("status", "BOOKED");
        cv.put("notes", notes);
        long id = db.insert("reservations", null, cv);
        db.close();
        return id;
    }

    public List<Reservation> getReservationsForUser(String username) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id,username,date,time,partySize,status,notes " +
                        "FROM reservations WHERE username=? ORDER BY date DESC, time DESC",
                new String[]{username}
        );

        List<Reservation> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new Reservation(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getInt(4),
                    c.getString(5),
                    c.getString(6)
            ));
        }
        c.close();
        db.close();
        return list;
    }

    public boolean cancelReservation(int id, String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", "CANCELLED");
        int rows = db.update("reservations", cv, "id=? AND username=?", new String[]{String.valueOf(id), username});
        db.close();
        return rows > 0;
    }

    public boolean deleteReservation(int id, String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete("reservations", "id=? AND username=?", new String[]{String.valueOf(id), username});
        db.close();
        return rows > 0;
    }
}
