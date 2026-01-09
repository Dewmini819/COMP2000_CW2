package uk.ac.plymouth.restaurantcw2;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MyReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        ListView list = findViewById(R.id.listReservations);

        List<String> reservations = new ArrayList<>();
        reservations.add("Fish & Chips - 10 Feb 2026 - 7:00 PM");
        reservations.add("Sunday Roast - 12 Feb 2026 - 1:00 PM");
        reservations.add("Burger Table - 15 Feb 2026 - 6:30 PM");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        reservations);

        list.setAdapter(adapter);
    }
}
