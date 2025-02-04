package com.serredujansau.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RGPDActivity extends AppCompatActivity {

    private CheckBox cbAccept;
    private Button btnSuivantRGPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgpd);

        // Configuration de la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialisation des vues
        cbAccept = findViewById(R.id.cbAccept);
        btnSuivantRGPD = findViewById(R.id.btnSuivantRGPD);

        // Gestion du bouton "Suivant"
        btnSuivantRGPD.setOnClickListener(v -> {
            if (cbAccept.isChecked()) {
                // Redirection vers l'activité de signature
                Intent intent = new Intent(RGPDActivity.this, SignatureActivity.class);
                startActivity(intent);
            } else {
                // Afficher un message si la case n'est pas cochée
                Toast.makeText(this, "Vous devez accepter les conditions pour continuer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Ajout du menu de la Toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Ouvrir l'activité de configuration
            startActivity(new Intent(this, ConfigActivity.class));
            return true;
        } else if (id == R.id.action_home) {
            // Retour à MainActivity
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            // Retour à l'activité précédente
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
