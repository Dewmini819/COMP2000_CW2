package uk.ac.plymouth.restaurantcw2;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username = SessionManager.getUsername(this);
        if (username.isEmpty()) {
            finish();
            return;
        }

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);
        scroll.addView(root);

        TextView title = new TextView(this);
        title.setText("Notification Settings");
        title.setTextSize(20f);

        Switch swEnabled = new Switch(this);
        swEnabled.setText("Enable notifications");

        CheckBox cbGuest = new CheckBox(this);
        cbGuest.setText("Guest: reservation status changes");

        CheckBox cbStaff = new CheckBox(this);
        cbStaff.setText("Staff: new / changed reservations");

        Button btnSave = new Button(this);
        btnSave.setText("Save");

        TextView tvStatus = new TextView(this);

        root.addView(title);
        root.addView(swEnabled);
        root.addView(cbGuest);
        root.addView(cbStaff);
        root.addView(btnSave);
        root.addView(tvStatus);

        setContentView(scroll);

        NotificationPrefsDao dao = new NotificationPrefsDao(this);
        NotificationPrefsDao.Prefs prefs = dao.getOrDefault(username);

        swEnabled.setChecked(prefs.enabled);
        cbGuest.setChecked(prefs.guestStatusChanges);
        cbStaff.setChecked(prefs.staffNewReservations);

        // Disable checkboxes when notifications are off
        Runnable applyEnabled = () -> {
            boolean on = swEnabled.isChecked();
            cbGuest.setEnabled(on);
            cbStaff.setEnabled(on);
        };
        applyEnabled.run();

        swEnabled.setOnCheckedChangeListener((b, isChecked) -> applyEnabled.run());

        btnSave.setOnClickListener(v -> {
            dao.save(
                    username,
                    swEnabled.isChecked(),
                    cbGuest.isChecked(),
                    cbStaff.isChecked()
            );
            tvStatus.setText("Saved ✅");
        });
    }
}
