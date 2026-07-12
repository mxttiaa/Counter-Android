package com.mxttiaa.mcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    // 1. Dichiariamo l'oggetto Counter specifico per il Gap e i componenti grafici
    private Counter contatoreGap;
    private TextView txtGapValue;

    int[] lightbackg = {
            R.color.honeydew,
            R.color.lightStrawberry,
            R.color.lightSteelBlue,
            R.color.lightSand,
            R.color.lightLavender,
            R.color.lightMint,
            R.color.lightPeach,
            R.color.lightLemon,
            R.color.lightSage,
            R.color.lightSky,
            R.color.lightLilac,
            R.color.lightRose,
            R.color.lightApricot,
            R.color.lightCloud,
            R.color.lightHoney,
            R.color.lightVanilla
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        View mainSettingsLayout = findViewById(R.id.settings_layout_root);
        MaterialToolbar toolbar = findViewById(R.id.toolbarSettings);

        // Recuperiamo i dati passati dalla MainActivity
        int indexRicevuto = getIntent().getIntExtra("IDX_COLOR", 0);
        int gapRicevuto = getIntent().getIntExtra("CURRENT_GAP", 1); // Recuperiamo il gap attuale (default 1)

        // Impostiamo lo sfondo coerente
        int coloreDaUsare = lightbackg[indexRicevuto];
        mainSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, coloreDaUsare));

        // 2. Inizializziamo il nostro Counter per il gap (Minimo 1, Massimo 100)
        contatoreGap = new Counter(gapRicevuto, 1, 999);

        // 3. Colleghiamo i componenti XML del mini-contatore
        txtGapValue = findViewById(R.id.txt_gap_value);
        Button btnMinusGap = findViewById(R.id.btn_minus_gap);
        Button btnPlusGap = findViewById(R.id.btn_plus_gap);

        // Mostriamo subito a schermo il valore del gap ereditato
        txtGapValue.setText(String.valueOf(contatoreGap.getValue()));

        // 4. Gestiamo i click dei pulsanti usando i metodi interni di Counter
        btnPlusGap.setOnClickListener(v -> {
            if (contatoreGap.increment(1)) { // aumenta il gap di 1
                txtGapValue.setText(String.valueOf(contatoreGap.getValue()));
            }
        });

        btnMinusGap.setOnClickListener(v -> {
            if (contatoreGap.decrement(1)) { // diminuisce il gap di 1
                txtGapValue.setText(String.valueOf(contatoreGap.getValue()));
            }
        });

        // Gestione dei margini della Toolbar (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(mainSettingsLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            toolbar.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 5. Quando si torna indietro, restituiamo il nuovo valore modificato
        toolbar.setNavigationOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_GAP", contatoreGap.getValue());
            setResult(RESULT_OK, resultIntent); // Impacchetta il risultato positivo
            finish(); // Chiude la schermata
        });
    }
}