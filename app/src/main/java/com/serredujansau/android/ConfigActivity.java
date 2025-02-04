package com.serredujansau.android;

import android.Manifest;
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

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class ConfigActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 1;

    private EditText etIpAddress, etPort, etWebServiceUsername, etWebServicePassword;
    private Button btnSaveConfig;
    private String loginFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkAndRequestPermissions();

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
            String ipAddress = etIpAddress.getText().toString().trim();
            String port = etPort.getText().toString().trim();
            String wsUsername = etWebServiceUsername.getText().toString().trim();
            String wsPassword = etWebServicePassword.getText().toString().trim();

            if (ipAddress.isEmpty() || port.isEmpty() || wsUsername.isEmpty() || wsPassword.isEmpty()) {
                Toast.makeText(this, "⚠️ Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSaveConfig.setEnabled(false);

            testConnection(ipAddress, port, wsUsername, wsPassword, success -> {
                if (success) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("IP_ADDRESS", ipAddress);
                    editor.putString("PORT", port);
                    editor.putString("WS_USERNAME", wsUsername);
                    editor.putString("WS_PASSWORD", wsPassword);
                    editor.apply();

                    createLoginFile(wsUsername, wsPassword);
                    Toast.makeText(this, "✅ Configuration enregistrée avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "❌ Échec de connexion au serveur. Vérifiez vos paramètres.", Toast.LENGTH_SHORT).show();
                }
                btnSaveConfig.setEnabled(true);
            });
        });
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
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

    private void createLoginFile(String username, String password) {
        try {
            File file = new File(getFilesDir(), "LoginWebS.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String content = "Username=" + username + "\nPassword=" + password;
            fos.write(content.getBytes());
            fos.close();

            loginFilePath = file.getAbsolutePath();
            Toast.makeText(this, "✅ Fichier LoginWebS.txt créé avec succès !", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "❌ Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show();
        }
    }

    private void testConnection(String ipAddress, String port, String username, String password, ConnectionCallback callback) {
        new Thread(() -> {
            try {
                String serverUrl = "http://" + ipAddress + ":" + port + "/RPC2";
                XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                config.setServerURL(new URL(serverUrl));
                XmlRpcClient client = new XmlRpcClient();
                client.setConfig(config);

                Vector<String> params = new Vector<>();
                params.add(username);
                params.add(password);

                Object result = client.execute("MgComIP.TestConnection", params);

                boolean success = result != null && result.toString().equals("OK");

                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(ConfigActivity.this, "✅ Connexion réussie à MgComIP !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ConfigActivity.this, "⚠️ Connexion refusée par le serveur !", Toast.LENGTH_SHORT).show();
                    }
                    callback.onResult(success);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(ConfigActivity.this, "❌ Erreur serveur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    callback.onResult(false);
                });
            }
        }).start();
    }

    public String getLoginFilePath() {
        return loginFilePath;
    }

    public void setLoginFilePath(String loginFilePath) {
        this.loginFilePath = loginFilePath;
    }

    private interface ConnectionCallback {
        void onResult(boolean success);
    }
}
