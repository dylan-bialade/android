package com.serredujansau.android;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 1;
    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkAndRequestPermissions(); // Vérifier et demander les permissions

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> authenticateUser());
    }

    // Vérifier les identifiants
    private void authenticateUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.equals("admin") && password.equals("1234")) {
            Toast.makeText(this, "✅ Connexion réussie", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ConfigActivity.class));
            finish();
        } else {
            Toast.makeText(this, "❌ Identifiants incorrects", Toast.LENGTH_SHORT).show();
        }
    }

    // Vérifier et demander les permissions
    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (!allGranted) {
                Toast.makeText(this, "⚠️ Permissions refusées, certaines fonctionnalités ne fonctionneront pas !", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "✅ Permissions accordées !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
