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
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private Counter contatoreGap;
    // Dichiariamo il nuovo counter per l'intervallo dei clic
    private Counter contatoreIntervallo;

    private TextView txtGapValue;
    private TextView txtIntervalValue;

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

        // 1. Recuperiamo tutti i dati inviati dalla MainActivity
        int indexRicevuto = getIntent().getIntExtra("IDX_COLOR", 0);
        int gapRicevuto = getIntent().getIntExtra("CURRENT_GAP", 1);
        boolean colorEnabledRicevuto = getIntent().getBooleanExtra("COLOR_ENABLED", true);
        int intervalRicevuto = getIntent().getIntExtra("COLOR_INTERVAL", 10);

        // Impostiamo lo sfondo ereditato
        int coloreDaUsare = lightbackg[indexRicevuto];
        mainSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, coloreDaUsare));

        // 2. Inizializziamo i due oggetti Counter
        contatoreGap = new Counter(gapRicevuto, 1, 100);
        contatoreIntervallo = new Counter(intervalRicevuto, 2, 100); // Minimo 2 clic, massimo 100

        // 3. Colleghiamo i componenti grafici del Gap
        txtGapValue = findViewById(R.id.txt_gap_value);
        Button btnMinusGap = findViewById(R.id.btn_minus_gap);
        Button btnPlusGap = findViewById(R.id.btn_plus_gap);
        txtGapValue.setText(String.valueOf(contatoreGap.getValue()));

        // 4. Colleghiamo i componenti del Cambio Colore
        SwitchMaterial switchColorChange = findViewById(R.id.switch_color_change);
        txtIntervalValue = findViewById(R.id.txt_interval_value);
        Button btnMinusInterval = findViewById(R.id.btn_minus_interval);
        Button btnPlusInterval = findViewById(R.id.btn_plus_interval);

        // Impostiamo lo stato iniziale dello switch e del testo dell'intervallo
        switchColorChange.setChecked(colorEnabledRicevuto);
        txtIntervalValue.setText(String.valueOf(contatoreIntervallo.getValue()));

        // Funzione di supporto per attivare/disattivare visivamente i tasti dell'intervallo
        updateIntervalControlsState(switchColorChange.isChecked(), btnMinusInterval, btnPlusInterval, txtIntervalValue);

        // Listener sullo switch: se lo spegni, i tasti sotto si bloccano
        switchColorChange.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateIntervalControlsState(isChecked, btnMinusInterval, btnPlusInterval, txtIntervalValue);
        });

        // 5. Listener dei pulsanti del Gap
        btnPlusGap.setOnClickListener(v -> {
            if (contatoreGap.increment(1)) {
                txtGapValue.setText(String.valueOf(contatoreGap.getValue()));
            }
        });
        btnMinusGap.setOnClickListener(v -> {
            if (contatoreGap.decrement(1)) {
                txtGapValue.setText(String.valueOf(contatoreGap.getValue()));
            }
        });

        // 6. Listener dei pulsanti dell'Intervallo
        btnPlusInterval.setOnClickListener(v -> {
            if (contatoreIntervallo.increment(1)) {
                txtIntervalValue.setText(String.valueOf(contatoreIntervallo.getValue()));
            }
        });
        btnMinusInterval.setOnClickListener(v -> {
            if (contatoreIntervallo.decrement(1)) {
                txtIntervalValue.setText(String.valueOf(contatoreIntervallo.getValue()));
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

        // 7. Ritorno dei dati: Mettiamo tutto nella busta di risposta
        toolbar.setNavigationOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_GAP", contatoreGap.getValue());
            resultIntent.putExtra("NEW_COLOR_ENABLED", switchColorChange.isChecked());
            resultIntent.putExtra("NEW_COLOR_INTERVAL", contatoreIntervallo.getValue());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // Metodo helper per cambiare lo stato di attivazione dei controlli dell'intervallo
    private void updateIntervalControlsState(boolean isEnabled, View minus, View plus, View text) {
        minus.setEnabled(isEnabled);
        plus.setEnabled(isEnabled);
        text.setEnabled(isEnabled);
        // Cambiamo leggermente l'opacità per far capire che sono disattivati
        float alpha = isEnabled ? 1.0f : 0.4f;
        minus.setAlpha(alpha);
        plus.setAlpha(alpha);
        text.setAlpha(alpha);
    }
}