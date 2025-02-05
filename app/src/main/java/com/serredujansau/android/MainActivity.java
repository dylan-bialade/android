package com.serredujansau.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etEmail, etEmailConfirm;
    private Button btnSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etEmailConfirm = findViewById(R.id.etEmailConfirm);
        btnSuivant = findViewById(R.id.btnSuivant);

        btnSuivant.setOnClickListener(v -> {
            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String emailConfirm = etEmailConfirm.getText().toString().trim();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || emailConfirm.isEmpty()) {
                Toast.makeText(MainActivity.this, "⚠️ Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else if (!email.equals(emailConfirm)) {
                Toast.makeText(MainActivity.this, "⚠️ Les emails ne correspondent pas", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                intent.putExtra("nom", nom);
                intent.putExtra("prenom", prenom);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }
}
