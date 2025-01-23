package com.example.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ConfigActivity extends AppCompatActivity {
    private EditText etIpAddress, etPort, etWebServiceUsername, etWebServicePassword;
    private Button btnSaveConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AuthUtils.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etIpAddress = findViewById(R.id.etIpAddress);
        etPort = findViewById(R.id.etPort);
        etWebServiceUsername = findViewById(R.id.etWebServiceUsername);
        etWebServicePassword = findViewById(R.id.etWebServicePassword);
        btnSaveConfig = findViewById(R.id.btnSaveConfig);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        etIpAddress.setText(prefs.getString("IP_ADDRESS", ""));
        etPort.setText(prefs.getString("PORT", ""));
        etWebServiceUsername.setText(prefs.getString("WS_USERNAME", ""));
        etWebServicePassword.setText(prefs.getString("WS_PASSWORD", ""));

        btnSaveConfig.setOnClickListener(v -> {
            String ipAddress = etIpAddress.getText().toString();
            String port = etPort.getText().toString();
            String wsUsername = etWebServiceUsername.getText().toString();
            String wsPassword = etWebServicePassword.getText().toString();

            if (ipAddress.isEmpty() || port.isEmpty() || wsUsername.isEmpty() || wsPassword.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("IP_ADDRESS", ipAddress);
            editor.putString("PORT", port);
            editor.putString("WS_USERNAME", wsUsername);
            editor.putString("WS_PASSWORD", wsPassword);
            editor.apply();

            Toast.makeText(this, "Configuration enregistrée avec succès", Toast.LENGTH_SHORT).show();
        });
    }
}
