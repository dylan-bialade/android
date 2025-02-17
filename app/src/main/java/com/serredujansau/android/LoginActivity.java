package com.serredujansau.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "⚠️ Veuillez entrer vos identifiants", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.equals("admin") && password.equals("1234")) {
                // Sauvegarde de l'utilisateur connecté
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("LOGGED_USER", username);
                editor.apply();

                Toast.makeText(this, "✅ Connexion réussie", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ConfigActivity.class));
                finish();
            } else {
                Toast.makeText(this, "❌ Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
