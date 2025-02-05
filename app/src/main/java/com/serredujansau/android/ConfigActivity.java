package com.serredujansau.android;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
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
import java.net.URL;
import java.util.Vector;

public class ConfigActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 1;
    private EditText etIpAddress, etPort, etUsername, etPassword;
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

        // Vérifier et demander les permissions
        checkAndRequestPermissions();

        etIpAddress = findViewById(R.id.etIpAddress);
        etPort = findViewById(R.id.etPort);
        etUsername = findViewById(R.id.etWebServiceUsername);
        etPassword = findViewById(R.id.etWebServicePassword);
        btnSaveConfig = findViewById(R.id.btnSaveConfig);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        etIpAddress.setText(prefs.getString("IP_ADDRESS", ""));
        etPort.setText(prefs.getString("PORT", ""));
        etUsername.setText(prefs.getString("WS_USERNAME", ""));
        etPassword.setText(prefs.getString("WS_PASSWORD", ""));

        btnSaveConfig.setOnClickListener(v -> {
            String ipAddress = etIpAddress.getText().toString().trim();
            String port = etPort.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (ipAddress.isEmpty() || port.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "⚠️ Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSaveConfig.setEnabled(false);

            new Thread(() -> {
                boolean success = testConnection(ipAddress, port, username, password);
                runOnUiThread(() -> {
                    if (success) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("IP_ADDRESS", ipAddress);
                        editor.putString("PORT", port);
                        editor.putString("WS_USERNAME", username);
                        editor.putString("WS_PASSWORD", password);
                        editor.apply();

                        createLoginFile(username, password);
                        Toast.makeText(this, "✅ Configuration enregistrée", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "❌ Échec de connexion au serveur", Toast.LENGTH_SHORT).show();
                    }
                    btnSaveConfig.setEnabled(true);
                });
            }).start();
        });
    }

    // Vérifier et demander les permissions nécessaires
    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
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

    // Vérifier la connexion au serveur XML-RPC
    private boolean testConnection(String ipAddress, String port, String username, String password) {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://" + ipAddress + ":" + port + "/"));
            config.setBasicUserName(username);
            config.setBasicPassword(password);

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Vector<Object> params = new Vector<>();
            params.add(username);
            params.add(password);

            Object result = client.execute("client.read", params);

            return result != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Créer le fichier `LoginWebS.txt`
    private void createLoginFile(String username, String password) {
        try {
            File file = new File(getFilesDir(), "LoginWebS.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String content = "Username=" + username + "\nPassword=" + password;
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "✅ Fichier LoginWebS.txt créé", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "❌ Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(this, "⚠️ Permissions refusées", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "✅ Permissions accordées", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
