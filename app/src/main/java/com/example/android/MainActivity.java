package com.example.android;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private EditText etNom, etPrenom, etEmail, etEmailConfirm;
    private Button btnSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etEmailConfirm = findViewById(R.id.etEmailConfirm);
        btnSuivant = findViewById(R.id.btnSuivant);

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
}