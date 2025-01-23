package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.databinding.ActivityMainBinding;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private EditText etNom, etPrenom, etEmail, etEmailConfirm;
    private Button btnSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etEmailConfirm = findViewById(R.id.etEmailConfirm);
        btnSuivant = findViewById(R.id.btnSuivant);

        // Configuration de la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bouton suivant : validation et redirection
        btnSuivant.setOnClickListener(v -> {
            if (validerEmails()) {
                Intent intent = new Intent(MainActivity.this, RGPDActivity.class);
                intent.putExtra("nom", etNom.getText().toString());
                intent.putExtra("prenom", etPrenom.getText().toString());
                intent.putExtra("email", etEmail.getText().toString());
                startActivity(intent);
            }
        });
    }

    // Validation des champs email
    private boolean validerEmails() {
        String email = etEmail.getText().toString();
        String emailConfirm = etEmailConfirm.getText().toString();

        if (email.isEmpty() || emailConfirm.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.equals(emailConfirm)) {
            Toast.makeText(this, "Les adresses e-mail ne correspondent pas", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Chargement du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu); // Remplacez par toolbar_menu
        return true;
    }

    // Gestion des clics sur les éléments du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // Récupère l'ID du menu sélectionné

        if (id == R.id.action_settings) {
            // Ouvrir ConfigActivity
            startActivity(new Intent(this, ConfigActivity.class));
            return true;
        } else if (id == R.id.action_home) {
            // Retour à MainActivity
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            // Bouton retour
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
