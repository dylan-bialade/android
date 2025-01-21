package com.example.android;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RGPDActivity extends AppCompatActivity {
    private CheckBox cbAccept;
    private Button btnSuivantRGPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgpd);

        cbAccept = findViewById(R.id.cbAccept);
        btnSuivantRGPD = findViewById(R.id.btnSuivantRGPD);

        btnSuivantRGPD.setOnClickListener(v -> {
            if (cbAccept.isChecked()) {
                Intent intent = new Intent(RGPDActivity.this, SignatureActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Vous devez accepter les conditions pour continuer", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

