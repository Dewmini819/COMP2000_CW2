package uk.ac.plymouth.restaurantcw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "restaurantcw2.db";
    private static final int DB_VER = 1;

    public static class Reservation {
        public String item;
        public String date;
        public String time;
        public String status;

        public Reservation(String item, String date, String time, String status) {
            this.item = item;
            this.date = date;
            this.time = time;
            this.status = status;
        }
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guestUsername TEXT," +
                "itemName TEXT," +
                "date TEXT," +
                "time TEXT," +
                "status TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reservations");
        onCreate(db);
    }

    public long insertReservation(String guestUsername, String itemName, String date, String time, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("guestUsername", guestUsername);
        cv.put("itemName", itemName);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("status", status);
        long id = db.insert("reservations", null, cv);
        db.close();
        return id;
    }

    public List<Reservation> getReservationsForUser(String username) {
        List<Reservation> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT itemName,date,time,status FROM reservations WHERE guestUsername=?",
                new String[]{username}
        );

        while (c.moveToNext()) {
            out.add(new Reservation(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
            ));
        }

        c.close();
        db.close();
        return out;
    }
}
