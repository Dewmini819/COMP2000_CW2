package uk.ac.plymouth.restaurantcw2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!"staff".equals(SessionManager.getRole(this))) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);

        Button btnMenu = new Button(this);
        btnMenu.setText("Manage Menu");

        Button btnReservations = new Button(this);
        btnReservations.setText("View Reservations");

        Button btnNotifications = new Button(this);
        btnNotifications.setText("Notification Settings (next)");

        Button btnLogout = new Button(this);
        btnLogout.setText("Logout");

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationSettingsActivity.class)));


        root.addView(btnMenu);
        root.addView(btnReservations);
        root.addView(btnNotifications);
        root.addView(btnLogout);

        setContentView(root);

        btnMenu.setOnClickListener(v ->
                startActivity(new Intent(this, StaffMenuManageActivity.class)));

        btnReservations.setOnClickListener(v ->
                startActivity(new Intent(this, StaffReservationsActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SessionManager.logout(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
