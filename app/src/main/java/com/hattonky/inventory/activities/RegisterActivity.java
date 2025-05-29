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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.hattonky.inventory.R;
import com.hattonky.inventory.data.ApiClient;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, nameEditText, addressEditText, phoneEditText, countryEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        nameEditText = findViewById(R.id.editTextName);
        addressEditText = findViewById(R.id.editTextAddress);
        phoneEditText = findViewById(R.id.editTextPhone);
        countryEditText = findViewById(R.id.editTextCountry);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            // You can collect more fields if your API supports them
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiClient.register(this, username, email, password, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String token = json.getString("token");
                        String role = json.optString("role", "user");
                        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
                        prefs.edit().putString("jwt", token).putString("role", role).apply();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, com.hattonky.inventory.MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(RegisterActivity.this, "Registration failed: Invalid response", Toast.LENGTH_LONG).show()
                        );
                    }
                }
                @Override
                public void onFailure(java.io.IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Registration failed")
                            .setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                    });
                }
            });
        });
    }
}
