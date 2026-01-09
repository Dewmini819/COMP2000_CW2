package uk.ac.plymouth.restaurantcw2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "local.db";
    private static final int DB_VER = 1;

    public LocalDb(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY," +
                "password TEXT," +
                "firstname TEXT," +
                "lastname TEXT," +
                "email TEXT," +
                "contact TEXT," +
                "usertype TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
