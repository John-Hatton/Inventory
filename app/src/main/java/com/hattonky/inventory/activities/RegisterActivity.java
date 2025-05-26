package com.hattonky.inventory.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.hattonky.inventory.R;

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
        // TODO: Implement registration logic
    }
}
