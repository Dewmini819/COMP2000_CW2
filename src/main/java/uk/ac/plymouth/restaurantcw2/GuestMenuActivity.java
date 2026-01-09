package uk.ac.plymouth.restaurantcw2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GuestMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        ListView list = findViewById(R.id.listMenu);
        List<String> display = new ArrayList<>();

        try {
            MenuDao dao = new MenuDao(this);
            List<MenuDao.MenuItem> items = dao.all();
            for (MenuDao.MenuItem m : items) {
                display.add(m.name + " - £" + String.format("%.2f", m.price));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Menu load failed", Toast.LENGTH_LONG).show();
        }

        if (display.isEmpty()) display.add("No menu items yet.");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display);
        list.setAdapter(adapter);
    }
}
