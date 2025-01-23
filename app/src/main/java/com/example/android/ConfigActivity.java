package com.example.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ConfigActivity extends AppCompatActivity {
    private EditText etIpAddress, etPort, etWebServiceUsername, etWebServicePassword;
    private Button btnSaveConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Charger les paramètres existants
        etIpAddress.setText(prefs.getString("IP_ADDRESS", ""));
        etPort.setText(prefs.getString("PORT", ""));
        etWebServiceUsername.setText(prefs.getString("WS_USERNAME", ""));
        etWebServicePassword.setText(prefs.getString("WS_PASSWORD", ""));

        btnSaveConfig.setOnClickListener(v -> {
            String ipAddress = etIpAddress.getText().toString().trim();
            String port = etPort.getText().toString().trim();
            String wsUsername = etWebServiceUsername.getText().toString().trim();
            String wsPassword = etWebServicePassword.getText().toString().trim();

            if (ipAddress.isEmpty() || port.isEmpty() || wsUsername.isEmpty() || wsPassword.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Désactiver le bouton pour éviter plusieurs clics
            btnSaveConfig.setEnabled(false);

            // Tester la connexion avant de sauvegarder
            testConnection(ipAddress, port, success -> {
                if (success) {
                    // Sauvegarde des paramètres
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("IP_ADDRESS", ipAddress);
                    editor.putString("PORT", port);
                    editor.putString("WS_USERNAME", wsUsername);
                    editor.putString("WS_PASSWORD", wsPassword);
                    editor.apply();

                    Toast.makeText(this, "Configuration enregistrée avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Échec de connexion au serveur. Vérifiez vos paramètres.", Toast.LENGTH_SHORT).show();
                }
                // Réactiver le bouton
                btnSaveConfig.setEnabled(true);
            });
        });
    }

    private void testConnection(String ipAddress, String port, ConnectionCallback callback) {
        String url = "http://" + ipAddress + ":" + port + "/api/test";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> callback.onResult(true),
                error -> callback.onResult(false));

        queue.add(stringRequest);
    }

    private interface ConnectionCallback {
        void onResult(boolean success);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ConfigActivity.class));
            return true;
        } else if (id == R.id.action_home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
