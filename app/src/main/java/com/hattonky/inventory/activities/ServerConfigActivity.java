package com.hattonky.inventory.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.hattonky.inventory.R;

public class ServerConfigActivity extends AppCompatActivity {
    private EditText serverUrlEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);
        serverUrlEditText = findViewById(R.id.editTextServerUrl);
        saveButton = findViewById(R.id.buttonSaveServerUrl);

        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String currentUrl = prefs.getString("server_url", "");
        serverUrlEditText.setText(currentUrl);

        saveButton.setOnClickListener(v -> {
            String url = serverUrlEditText.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a server address", Toast.LENGTH_SHORT).show();
                return;
            }
            prefs.edit().putString("server_url", url).apply();
            Toast.makeText(this, "Server address saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}

