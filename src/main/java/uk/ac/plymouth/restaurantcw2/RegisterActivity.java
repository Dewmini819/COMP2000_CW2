package uk.ac.plymouth.restaurantcw2;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etFirst = findViewById(R.id.etFirst);
        EditText etLast = findViewById(R.id.etLast);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etContact = findViewById(R.id.etContact);
        EditText etRole = findViewById(R.id.etRole);
        Button btnCreate = findViewById(R.id.btnCreate);
        TextView tvStatus = findViewById(R.id.tvStatus);

        btnCreate.setOnClickListener(v -> {
            String u = etUsername.getText().toString().trim();
            String p = etPassword.getText().toString().trim();
            String role = etRole.getText().toString().trim().toLowerCase();

            if (u.isEmpty() || p.isEmpty() || role.isEmpty()) {
                tvStatus.setText("Enter username, password, and role (guest/staff).");
                return;
            }
            if (!role.equals("guest") && !role.equals("staff")) {
                tvStatus.setText("Role must be 'guest' or 'staff'.");
                return;
            }

            tvStatus.setText("Creating user... (API)");

            AppExecutors.networkIO().execute(() -> {
                try {
                    JSONObject body = new JSONObject();
                    body.put("username", u);
                    body.put("password", p);
                    body.put("firstname", etFirst.getText().toString().trim());
                    body.put("lastname", etLast.getText().toString().trim());
                    body.put("email", etEmail.getText().toString().trim());
                    body.put("contact", etContact.getText().toString().trim());
                    body.put("usertype", role);

                    ApiClient.post("/create_user/" + Config.STUDENT_ID, body);

                    runOnUiThread(() -> tvStatus.setText("Account created ✅ Go back and login."));
                } catch (Exception e) {
                    runOnUiThread(() -> tvStatus.setText("Create failed: " + e.getMessage()));
                }
            });
        });
    }
}
