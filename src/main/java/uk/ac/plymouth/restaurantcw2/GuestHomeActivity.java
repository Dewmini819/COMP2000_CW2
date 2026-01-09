package uk.ac.plymouth.restaurantcw2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuestHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);


        if (!SessionManager.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        Button btnBrowse = findViewById(R.id.btnBrowse);
        Button btnRes = findViewById(R.id.btnReservations);
        Button btnNoti = findViewById(R.id.btnNotifications);
        Button btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Welcome, " + SessionManager.getUsername(this));

        btnBrowse.setOnClickListener(v ->
                startActivity(new Intent(this, GuestMenuActivity.class)));

        btnRes.setOnClickListener(v ->
                startActivity(new Intent(this, MyReservationsActivity.class)));

        btnNoti.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationSettingsActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SessionManager.logout(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
