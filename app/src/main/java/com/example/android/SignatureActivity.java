package com.example.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.signature.SignatureView;

public class SignatureActivity extends AppCompatActivity {

    private SignatureView signatureView;
    private Button btnEnregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AuthUtils.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_signature);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signatureView = findViewById(R.id.signatureView);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        btnEnregistrer.setOnClickListener(v -> {
            Bitmap signatureBitmap = signatureView.getSignature();
            if (signatureBitmap != null) {
                Toast.makeText(this, "Signature enregistrée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Aucune signature détectée", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
