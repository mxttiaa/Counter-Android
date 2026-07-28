package com.mxttiaa.mcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private Counter contatoreGap;
    private Counter contatoreIntervallo;
    private Counter contatoreColoreFisso;

    private int indexRicevuto = 0;

    private TextView txtGapValue;
    private TextView txtIntervalValue;
    private TextView txtColorNumber;

    private Button btnPrevColor;
    private Button btnNextColor;

    private SwitchMaterial switchColorChange;

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

        // 1. Recuperiamo i dati inviati dalla MainActivity
        indexRicevuto = getIntent().getIntExtra("IDX_COLOR", 0);
        int gapRicevuto = getIntent().getIntExtra("CURRENT_GAP", 1);
        boolean colorEnabledRicevuto = getIntent().getBooleanExtra("COLOR_ENABLED", true);
        int intervalRicevuto = getIntent().getIntExtra("COLOR_INTERVAL", 10);

        // NEW: Recuperiamo l'indice del colore fisso (default 0)
        int mainColorIndexRicevuto = getIntent().getIntExtra("MAIN_COLOR_INDEX", 0);

        // Impostiamo lo sfondo ereditato
        int coloreDaUsare = lightbackg[indexRicevuto];
        mainSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, coloreDaUsare));

        // 2. Inizializziamo i tre oggetti Counter
        contatoreGap = new Counter(gapRicevuto, 1, 100);
        contatoreIntervallo = new Counter(intervalRicevuto, 2, 100);
        contatoreColoreFisso = new Counter(mainColorIndexRicevuto, 0, lightbackg.length - 1);

        // 3. Componenti del Gap
        txtGapValue = findViewById(R.id.txt_gap_value);
        Button btnMinusGap = findViewById(R.id.btn_minus_gap);
        Button btnPlusGap = findViewById(R.id.btn_plus_gap);
        txtGapValue.setText(String.valueOf(contatoreGap.getValue()));

        // 4. Componenti dell'Intervallo
        switchColorChange = findViewById(R.id.switch_color_change);
        txtIntervalValue = findViewById(R.id.txt_interval_value);
        Button btnMinusInterval = findViewById(R.id.btn_minus_interval);
        Button btnPlusInterval = findViewById(R.id.btn_plus_interval);

        // 5. Componenti del Colore Fisso
        txtColorNumber = findViewById(R.id.txt_color_number);
        btnPrevColor = findViewById(R.id.btn_prev_color);
        btnNextColor = findViewById(R.id.btn_next_color);

        switchColorChange.setChecked(colorEnabledRicevuto);
        txtIntervalValue.setText(String.valueOf(contatoreIntervallo.getValue()));
        txtColorNumber.setText("Colore " + (contatoreColoreFisso.getValue() + 1));

        // Stato iniziale dell'abilitazione dei controlli
        updateControlsState(switchColorChange.isChecked(),
                btnMinusInterval, btnPlusInterval, txtIntervalValue,
                btnPrevColor, btnNextColor, txtColorNumber, mainSettingsLayout);

        // Listener sullo switch
        switchColorChange.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateControlsState(isChecked,
                    btnMinusInterval, btnPlusInterval, txtIntervalValue,
                    btnPrevColor, btnNextColor, txtColorNumber, mainSettingsLayout);
        });

        // Listener dei pulsanti Gap
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

        // Listener dei pulsanti Intervallo
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

        // Listener dei pulsanti Colore Fisso
        btnNextColor.setOnClickListener(v -> {
            if (contatoreColoreFisso.increment(1)) {
                int nuovoIndice = contatoreColoreFisso.getValue();
                txtColorNumber.setText("Colore " + (nuovoIndice + 1));
                if (!switchColorChange.isChecked()) {
                    mainSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, lightbackg[nuovoIndice]));
                }
            }
        });
        btnPrevColor.setOnClickListener(v -> {
            if (contatoreColoreFisso.decrement(1)) {
                int nuovoIndice = contatoreColoreFisso.getValue();
                txtColorNumber.setText("Colore " + (nuovoIndice + 1));
                if (!switchColorChange.isChecked()) {
                    mainSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, lightbackg[nuovoIndice]));
                }
            }
        });

        // Margini EdgeToEdge
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

        toolbar.setNavigationOnClickListener(v -> inviaDatiEChiudi());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                inviaDatiEChiudi();
            }
        });
    }

    private void inviaDatiEChiudi() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_GAP", contatoreGap.getValue());
        resultIntent.putExtra("NEW_COLOR_ENABLED", switchColorChange.isChecked());
        resultIntent.putExtra("NEW_COLOR_INTERVAL", contatoreIntervallo.getValue());

        // NEW: Inviamo a MainActivity l'indice del colore fisso scelto dall'utente
        resultIntent.putExtra("NEW_MAIN_COLOR_INDEX", contatoreColoreFisso.getValue());

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void updateControlsState(boolean isColorChangeAutoEnabled,
                                     View minusInterval, View plusInterval, View textInterval,
                                     View prevColor, View nextColor, View textColor, View layoutRoot) {

        // Gestione Intervallo
        minusInterval.setEnabled(isColorChangeAutoEnabled);
        plusInterval.setEnabled(isColorChangeAutoEnabled);
        textInterval.setEnabled(isColorChangeAutoEnabled);
        float alphaInterval = isColorChangeAutoEnabled ? 1.0f : 0.4f;
        minusInterval.setAlpha(alphaInterval);
        plusInterval.setAlpha(alphaInterval);
        textInterval.setAlpha(alphaInterval);

        // Gestione Colore Fisso
        boolean isFixedColorActive = !isColorChangeAutoEnabled;
        prevColor.setEnabled(isFixedColorActive);
        nextColor.setEnabled(isFixedColorActive);
        textColor.setEnabled(isFixedColorActive);
        float alphaColor = isFixedColorActive ? 1.0f : 0.4f;
        prevColor.setAlpha(alphaColor);
        nextColor.setAlpha(alphaColor);
        textColor.setAlpha(alphaColor);

        // Se lo switch è SPENTO, usiamo il colore fisso selezionato
        if (isFixedColorActive) {
            int coloreFisso = lightbackg[contatoreColoreFisso.getValue()];
            layoutRoot.setBackgroundColor(ContextCompat.getColor(this, coloreFisso));
        } else {
            // Se lo switch è ACCESO, ripristiniamo subito il colore dinamico di provenienza!
            int coloreDinamico = lightbackg[indexRicevuto];
            layoutRoot.setBackgroundColor(ContextCompat.getColor(this, coloreDinamico));
        }
    }
}