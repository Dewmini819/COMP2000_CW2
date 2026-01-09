package uk.ac.plymouth.restaurantcw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MenuDao {

    private final MenuDb helper;

    public MenuDao(Context ctx) {
        helper = new MenuDb(ctx);
        seedIfEmpty();
    }

    public static class MenuItem {
        public int id;
        public String name;
        public double price;

        public MenuItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    public List<MenuItem> all() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id,name,price FROM menu_items ORDER BY id DESC", null);
        List<MenuItem> out = new ArrayList<>();
        while (c.moveToNext()) {
            out.add(new MenuItem(c.getInt(0), c.getString(1), c.getDouble(2)));
        }
        c.close();
        db.close();
        return out;
    }

    public long add(String name, double price) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        long id = db.insert("menu_items", null, cv);
        db.close();
        return id;
    }

    public int delete(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete("menu_items", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    private void seedIfEmpty() {
        if (!all().isEmpty()) return;
        add("Fish & Chips", 10.99);
        add("Sunday Roast", 12.50);
        add("Beef Burger", 9.50);
        add("Sausage Roll", 3.00);
        add("Cheese & Onion Sandwich", 2.50);
        add("Milk Tea", 2.00);
    }
}
