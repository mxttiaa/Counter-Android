package com.mxttiaa.mcounter;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_layout_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View mainSettingsLayout = findViewById(R.id.settings_layout_root);

        // Dentro l'onCreate di SettingsActivity
        int indexRicevuto = getIntent().getIntExtra("IDX_COLOR", 0); // 0 è il valore di default se non trova nulla

        // Ora puoi usare l'indice per impostare lo sfondo o altro nella SettingsActivity
        int coloreDaUsare = lightbackg[indexRicevuto];
        mainSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, coloreDaUsare));

        MaterialToolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);

        // Questo abilita la freccetta in alto a sinistra
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Questo dice cosa fare quando la freccia viene cliccata
        toolbar.setNavigationOnClickListener(v -> {
            finish(); // <--- LA PAROLA MAGICA: chiude l'Activity e torna "sotto"
        });
    }
}