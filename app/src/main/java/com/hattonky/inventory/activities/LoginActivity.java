package com.hattonky.inventory.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.hattonky.inventory.MainActivity;
import com.hattonky.inventory.R;
import com.hattonky.inventory.data.ApiClient;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameOrEmailEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameOrEmailEditText = findViewById(R.id.editTextUsernameOrEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

        loginButton.setOnClickListener(v -> {
            String usernameOrEmail = usernameOrEmailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username/email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiClient.login(this, usernameOrEmail, password, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    // Parse JWT from response (assume JSON: { token: "...", role: "..." })
                    try {
                        JSONObject json = new JSONObject(response);
                        String token = json.getString("token");
                        String role = json.optString("role", "user");
                        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
                        prefs.edit().putString("jwt", token).putString("role", role).apply();
                        // Go to MainActivity (or wherever appropriate)
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(LoginActivity.this, "Login failed: Invalid response", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
                @Override
                public void onFailure(java.io.IOException e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
