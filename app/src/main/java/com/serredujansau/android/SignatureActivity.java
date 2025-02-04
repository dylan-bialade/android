package com.serredujansau.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.serredujansau.android.signature.SignatureView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignatureActivity extends AppCompatActivity {
    private SignatureView signatureView;
    private Button btnEnregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        // Configurer la barre d'outils
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialiser les vues
        signatureView = findViewById(R.id.signatureView);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        // Action du bouton Enregistrer
        btnEnregistrer.setOnClickListener(v -> {
            Bitmap signatureBitmap = signatureView.getSignature();
            if (signatureBitmap != null) {
                saveSignatureToServer(signatureBitmap);
            } else {
                Toast.makeText(this, "Veuillez fournir une signature avant d'enregistrer.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Enregistrer la signature sur le serveur
    private void saveSignatureToServer(Bitmap signatureBitmap) {
        // Convertir la signature en Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        // Récupérer les paramètres du serveur
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String ipAddress = prefs.getString("IP_ADDRESS", "");
        String port = prefs.getString("PORT", "");

        if (ipAddress.isEmpty() || port.isEmpty()) {
            Toast.makeText(this, "Veuillez configurer l'adresse IP et le port dans les paramètres.", Toast.LENGTH_SHORT).show();
            return;
        }

        String serverUrl = "http://" + ipAddress + ":" + port + "/api/saveSignature";

        // Envoyer une requête POST avec Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                response -> Toast.makeText(this, "Signature enregistrée avec succès.", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Erreur lors de l'enregistrement de la signature.", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", "1"); // Remplacez par l'ID utilisateur approprié
                params.put("signature", encodedImage);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // Création du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Gestion des actions du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ConfigActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
