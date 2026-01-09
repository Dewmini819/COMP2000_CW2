package uk.ac.plymouth.restaurantcw2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GuestReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservations);

        ListView list = findViewById(R.id.listReservations);
        TextView tvEmpty = findViewById(R.id.tvEmpty);

        DBHelper db = new DBHelper(this);
        String username = SessionManager.getUsername(this);

        List<String> rows = new ArrayList<>();

        for (DBHelper.Reservation r : db.getReservationsForUser(username)) {
            rows.add(r.item + " | " + r.date + " " + r.time + " | " + r.status);
        }

        if (rows.isEmpty()) {
            tvEmpty.setText("No reservations yet");
            tvEmpty.setVisibility(TextView.VISIBLE);
        } else {
            tvEmpty.setVisibility(TextView.GONE);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);

        list.setAdapter(adapter);
    }
}
