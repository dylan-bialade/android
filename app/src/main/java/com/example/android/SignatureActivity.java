package com.example.android;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.signature.SignatureView;

public class SignatureActivity extends AppCompatActivity {
    private SignatureView signatureView;
    private Button btnEnregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        signatureView = findViewById(R.id.signatureView);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        btnEnregistrer.setOnClickListener(v -> {
            Bitmap signatureBitmap = signatureView.getSignature();
            // Enregistrer l'image dans la base de données (id utilisateur à associer)
            saveSignatureToDatabase(signatureBitmap);
        });
    }

    private void saveSignatureToDatabase(Bitmap signature) {
        // Convertir l'image en Base64 ou en fichier binaire
        // Insérer dans la base de données avec l'id utilisateur
    }
}
