package com.serredujansau.android;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigActivity extends AppCompatActivity {
    private EditText etIpAddress, etPort, etWebServiceUsername, etWebServicePassword;
    private Button btnSaveConfig;
    private SharedPreferences prefs;

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
        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

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

            // Tester la connexion avec le serveur
            testConnection(ipAddress, port, wsUsername, wsPassword);
        });
    }

    private void testConnection(String ipAddress, String port, String username, String password) {
        String serverUrl = "http://" + ipAddress + ":" + port + "/";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                response -> {
                    // ✅ Si la connexion réussit, enregistrer les paramètres
                    saveConfig(ipAddress, port, username, password);
                },
                error -> {
                    // ❌ Si la connexion échoue, afficher un message d'erreur
                    Toast.makeText(this, "⚠️ Échec de connexion au serveur. Vérifiez vos paramètres.", Toast.LENGTH_LONG).show();
                    btnSaveConfig.setEnabled(true);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("method", "mg.version");
                params.put("username", username);
                params.put("password", password);
                params.put("check", "1"); // Peut être modifié selon les besoins du WebService
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void saveConfig(String ipAddress, String port, String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("IP_ADDRESS", ipAddress);
        editor.putString("PORT", port);
        editor.putString("WS_USERNAME", username);
        editor.putString("WS_PASSWORD", password);
        editor.apply();

        // Création du fichier LoginWebS.txt
        createLoginWebFile(username, password);

        Toast.makeText(this, "✅ Configuration enregistrée avec succès", Toast.LENGTH_SHORT).show();
        btnSaveConfig.setEnabled(true);
    }

    private void createLoginWebFile(String username, String password) {
        File file = new File(getFilesDir(), "LoginWebS.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String content = username + "\n" + password;
            fos.write(content.getBytes());
            Toast.makeText(this, "📂 Fichier LoginWebS.txt créé", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "⚠️ Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show();
        }
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
