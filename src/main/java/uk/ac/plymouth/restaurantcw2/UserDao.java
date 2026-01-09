package uk.ac.plymouth.restaurantcw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
    private final LocalDb helper;

    public UserDao(Context ctx) {
        helper = new LocalDb(ctx);
    }

    public boolean createUser(String username, String password, String firstname, String lastname,
                              String email, String contact, String usertype) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        cv.put("email", email);
        cv.put("contact", contact);
        cv.put("usertype", usertype);

        long res;
        try {
            res = db.insertOrThrow("users", null, cv);
        } catch (Exception ignored) {
            res = -1;
        }
        db.close();
        return res != -1;
    }

    public String[] getUser(String username) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username,password,usertype FROM users WHERE username=?",
                new String[]{username});
        String[] out = null;
        if (c.moveToFirst()) {
            out = new String[]{c.getString(0), c.getString(1), c.getString(2)};
        }
        c.close();
        db.close();
        return out;
    }
}
