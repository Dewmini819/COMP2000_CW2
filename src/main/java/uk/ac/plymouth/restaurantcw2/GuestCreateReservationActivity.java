package uk.ac.plymouth.restaurantcw2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GuestCreateReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest_create_reservation2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etItem = findViewById(R.id.etItem);
        EditText etDate = findViewById(R.id.etDate);
        EditText etTime = findViewById(R.id.etTime);
        Button btnCreate = findViewById(R.id.btnCreateReservation);
        TextView tvStatus = findViewById(R.id.tvStatus);

        btnCreate.setOnClickListener(v -> {
            String item = etItem.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (item.isEmpty() || date.isEmpty() || time.isEmpty()) {
                tvStatus.setText("Please fill all fields");
                return;
            }

            String username = SessionManager.getUsername(this);
            if (username.isEmpty()) {
                tvStatus.setText("Please login again");
                return;
            }

            tvStatus.setText("Saving...");

            AppExecutors.diskIO().execute(() -> {
                DBHelper db = new DBHelper(this);
                long id = db.insertReservation(username, item, date, time, "CONFIRMED");

                runOnUiThread(() -> {
                    if (id > 0) {
                        tvStatus.setText("Reservation created");
                        if (NotificationPrefs.isEnabled(this)) {
                            NotificationUtil.show(this, "Reservation Confirmed", item + " - " + date + " " + time);
                        }
                        finish();
                    } else {
                        tvStatus.setText("Failed to create reservation");
                    }
                });
            });
        });
    }
}
