package com.mxttiaa.mcounter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    private Counter miocontatore;
    int gap = 1;

    private boolean isColorChangeEnabled = true;
    private int colorChangeInterval = 10;

    private ActivityResultLauncher<Intent> settingsLauncher;

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

    private int lastIdxCloolor = 0;
    int myColor = lightbackg[0];
    private final java.util.Random random = new java.util.Random();

    // Nome del file di salvataggio locale
    private static final String PREFS_NAME = "MCounterPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.core.splashscreen.SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. CARICAMENTO DATI SALVATI dalle SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int valoreSalvato = prefs.getInt("COUNTER_VALUE", 0);
        gap = prefs.getInt("GAP_VALUE", 1);
        isColorChangeEnabled = prefs.getBoolean("COLOR_ENABLED", true);
        colorChangeInterval = prefs.getInt("COLOR_INTERVAL", 10);
        lastIdxCloolor = prefs.getInt("COLOR_INDEX", 0);
        myColor = lightbackg[lastIdxCloolor];

        // Inizializziamo il contatore con il valore recuperato dal salvataggio
        miocontatore = new Counter(valoreSalvato, 0, 99999);

        TextView textValue = findViewById(R.id.valueCounter);
        View mainLayout = findViewById(R.id.main);

        // Mostriamo i dati recuperati a schermo
        textValue.setText(String.valueOf(miocontatore.getValue()));
        mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, myColor));

        settingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        gap = result.getData().getIntExtra("NEW_GAP", gap);
                        isColorChangeEnabled = result.getData().getBooleanExtra("NEW_COLOR_ENABLED", isColorChangeEnabled);
                        colorChangeInterval = result.getData().getIntExtra("NEW_COLOR_INTERVAL", colorChangeInterval);

                        // Salviamo subito le nuove impostazioni ricevute
                        salvaStatoApp();

                        Toast.makeText(this, "Impostazioni applicate!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Button plusBotton = findViewById(R.id.plus);
        plusBotton.setOnClickListener(v -> {
            if(miocontatore.increment(gap)){
                textValue.setText(String.valueOf(miocontatore.getValue()));
                changeColorBackg(mainLayout);
                salvaStatoApp(); // Salviamo dopo ogni incremento
            } else {
                Toast.makeText(MainActivity.this, "Hai raggiunto il massimo.", Toast.LENGTH_SHORT).show();
            }
        });

        Button minusBotton = findViewById(R.id.minus);
        minusBotton.setOnClickListener(v -> {
            if(miocontatore.decrement(gap)){
                textValue.setText(String.valueOf(miocontatore.getValue()));
                changeColorBackg(mainLayout);
                salvaStatoApp(); // Salviamo dopo ogni decremento
            } else {
                Toast.makeText(MainActivity.this, "Hai già raggiunto lo zero.", Toast.LENGTH_SHORT).show();
            }
        });

        Button resetBotton = findViewById(R.id.resetButt);
        resetBotton.setOnClickListener(v -> showDialogReset(mainLayout, textValue));

        Button settingsBotton = findViewById(R.id.settingsButt);
        settingsBotton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("IDX_COLOR", lastIdxCloolor);
            intent.putExtra("CURRENT_GAP", gap);
            intent.putExtra("COLOR_ENABLED", isColorChangeEnabled);
            intent.putExtra("COLOR_INTERVAL", colorChangeInterval);
            settingsLauncher.launch(intent);
        });
    }

    // 2. METODO DI SALVATAGGIO: Scrive i dati nell'archivio locale
    private void salvaStatoApp() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("COUNTER_VALUE", miocontatore.getValue());
        editor.putInt("GAP_VALUE", gap);
        editor.putBoolean("COLOR_ENABLED", isColorChangeEnabled);
        editor.putInt("COLOR_INTERVAL", colorChangeInterval);
        editor.putInt("COLOR_INDEX", lastIdxCloolor);

        editor.apply(); // Conferma e applica i salvataggi in background
    }

    private void changeColorBackg(View mainLayout){
        if (!isColorChangeEnabled) {
            return;
        }

        int value = miocontatore.getValue();
        if (value > 0 && value % colorChangeInterval == 0) {
            int idxCoolor;
            do {
                idxCoolor = random.nextInt(lightbackg.length);
            } while(idxCoolor == lastIdxCloolor);

            lastIdxCloolor = idxCoolor;
            myColor = lightbackg[idxCoolor];
            mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, myColor));
        }
    }

    private void showDialogReset(View mainLayout, TextView textValue){
        com.google.android.material.dialog.MaterialAlertDialogBuilder builder = new com.google.android.material.dialog.MaterialAlertDialogBuilder(this);

        int colorDeepSpaceBlue = androidx.core.content.ContextCompat.getColor(this, R.color.deepSpaceBlue);
        int colorSteelBlue = androidx.core.content.ContextCompat.getColor(this, R.color.steelBlue);
        int colorStrawberryRed = androidx.core.content.ContextCompat.getColor(this, R.color.strawberryRed);

        android.text.SpannableString titleText = new android.text.SpannableString("Conferma Reset");
        titleText.setSpan(new android.text.style.ForegroundColorSpan(colorDeepSpaceBlue), 0, titleText.length(), 0);
        builder.setTitle(titleText);

        android.text.SpannableString messageText = new android.text.SpannableString("Sei sicuro di voler resettare il contatore?");
        messageText.setSpan(new android.text.style.ForegroundColorSpan(colorDeepSpaceBlue), 0, messageText.length(), 0);
        builder.setMessage(messageText);

        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                miocontatore.reset();
                textValue.setText(String.valueOf(miocontatore.getValue()));
                lastIdxCloolor = 0; // Resettiamo anche l'indice del colore allo stato iniziale
                myColor = lightbackg[0];
                mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, myColor));

                salvaStatoApp(); // Salviamo lo stato azzerato
            }
        });

        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_custom_background);
        }

        dialog.setOnShowListener(dialogInterface -> {
            int paddingVertical = (int) (12 * getResources().getDisplayMetrics().density);
            int paddingHorizontal = (int) (24 * getResources().getDisplayMetrics().density);

            Button positiveButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setBackgroundTintList(null);
                positiveButton.setTextColor(colorDeepSpaceBlue);

                android.graphics.drawable.GradientDrawable shapePositive = new android.graphics.drawable.GradientDrawable();
                shapePositive.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                shapePositive.setCornerRadius(30);
                shapePositive.setColor(colorStrawberryRed);
                shapePositive.setStroke((int) (3 * getResources().getDisplayMetrics().density), colorDeepSpaceBlue);

                positiveButton.setBackground(shapePositive);
                positiveButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            }

            Button negativeButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                negativeButton.setBackgroundTintList(null);
                negativeButton.setTextColor(colorSteelBlue);

                android.graphics.drawable.GradientDrawable shapeNegative = new android.graphics.drawable.GradientDrawable();
                shapeNegative.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                shapeNegative.setCornerRadius(30);
                shapeNegative.setColor(colorDeepSpaceBlue);
                shapeNegative.setStroke((int) (3 * getResources().getDisplayMetrics().density), colorDeepSpaceBlue);

                negativeButton.setBackground(shapeNegative);
                negativeButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                params.setMarginEnd((int) (16 * getResources().getDisplayMetrics().density));
                negativeButton.setLayoutParams(params);
            }
        });

        dialog.show();
    }
}