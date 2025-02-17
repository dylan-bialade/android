package com.serredujansau.android;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        signatureView = findViewById(R.id.signatureView);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        btnEnregistrer.setOnClickListener(v -> {
            Bitmap signatureBitmap = signatureView.getSignature();
            sendSignatureToServer(signatureBitmap);
        });
    }

    private void sendSignatureToServer(Bitmap signature) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedSignature = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String ipAddress = prefs.getString("IP_ADDRESS", "");
        String port = prefs.getString("PORT", "");
        String wsUsername = prefs.getString("WS_USERNAME", "");
        String wsPassword = prefs.getString("WS_PASSWORD", "");

        String serverUrl = "http://" + ipAddress + ":" + port + "/";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                response -> {
                    // Réponse du serveur
                },
                error -> {
                    // Gestion des erreurs
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("method", "client.write");
                params.put("username", wsUsername);
                params.put("password", wsPassword);
                params.put("client_data", "Nom;Prenom;Email");
                params.put("signature", encodedSignature);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
