package uk.ac.plymouth.restaurantcw2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ApiClient.postAsync("/create_student/" + Config.STUDENT_ID, null, new ApiClient.JsonCallback() {
            @Override
            public void onSuccess(JSONObject json) { }

            @Override
            public void onError(String message) { }
        });

        if (SessionManager.isLoggedIn(this)) {
            goToHome(SessionManager.getRole(this));
            finish();
            return;
        }

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvStatus = findViewById(R.id.tvStatus);

        btnLogin.setOnClickListener(v -> {
            String u = etUsername.getText().toString().trim();
            String p = etPassword.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                tvStatus.setText("Please enter username and password");
                return;
            }

            tvStatus.setText("Logging in...");

            String endpoint = "/read_user/" + Config.STUDENT_ID + "/" + u;

            ApiClient.getAsync(endpoint, new ApiClient.JsonCallback() {
                @Override
                public void onSuccess(JSONObject json) {
                    try {
                        JSONObject userObj = json.getJSONObject("user");
                        String apiPassword = userObj.getString("password");
                        String role = userObj.getString("usertype");

                        if (!p.equals(apiPassword)) {
                            tvStatus.setText("Invalid password");
                            return;
                        }

                        SessionManager.login(LoginActivity.this, u, role);
                        goToHome(role);
                        finish();

                    } catch (Exception e) {
                        tvStatus.setText("Login failed");
                    }
                }

                @Override
                public void onError(String message) {
                    if (message != null && message.toLowerCase().contains("404")) {
                        tvStatus.setText("User not found");
                    } else {
                        tvStatus.setText("Login failed: " + message);
                    }
                }
            });
        });

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void goToHome(String role) {
        if ("staff".equalsIgnoreCase(role)) {
            startActivity(new Intent(this, StaffHomeActivity.class));
        } else {
            startActivity(new Intent(this, GuestHomeActivity.class));
        }
    }
}
