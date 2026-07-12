package com.mxttiaa.mcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
// 1. IMPORTANTE: Servono questi due nuovi import per gestire il ritorno dei dati
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

    // 2. DICHIARIAMO IL RICEVITORE: Diventa una variabile della classe
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

        miocontatore = new Counter(0, 0, 99999);
        TextView textValue = findViewById(R.id.valueCounter);
        View mainLayout = findViewById(R.id.main);

        // 3. REGISTRIAMO IL RICEVITORE: Va fatto OBBLIGATORIAMENTE dentro l'onCreate, prima che l'activity parta
        settingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Controlliamo che l'utente sia tornato salvando (RESULT_OK) e che il pacchetto non sia vuoto
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Estraiamo il nuovo gap inserito dall'utente. Se non trova nulla, tiene il valore attuale di gap
                        gap = result.getData().getIntExtra("NEW_GAP", gap);

                        // Opzionale: un piccolo messaggio a schermo che conferma il cambio
                        Toast.makeText(this, "Salto aggiornato a: " + gap, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Button plusBotton = findViewById(R.id.plus);
        plusBotton.setOnClickListener(v -> {
            if(miocontatore.increment(gap)){
                textValue.setText(String.valueOf(miocontatore.getValue()));
                changeColorBackg(mainLayout);
            } else {
                Toast.makeText(MainActivity.this, "Hai raggiunto il massimo.", Toast.LENGTH_SHORT).show();
            }
        });

        Button minusBotton = findViewById(R.id.minus);
        minusBotton.setOnClickListener(v -> {
            if(miocontatore.decrement(gap)){
                textValue.setText(String.valueOf(miocontatore.getValue()));
                changeColorBackg(mainLayout);
            } else {
                Toast.makeText(MainActivity.this, "Hai già raggiunto lo zero.", Toast.LENGTH_SHORT).show();
            }
        });

        Button resetBotton = findViewById(R.id.resetButt);
        resetBotton.setOnClickListener(v -> showDialogReset(mainLayout, textValue));

        // Pulsante Settings
        Button settingsBotton = findViewById(R.id.settingsButt);
        settingsBotton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("IDX_COLOR", lastIdxCloolor);
            intent.putExtra("CURRENT_GAP", gap);

            // 4. LA PAROLA MAGICA: Sostituiamo startActivity(intent) con il lancio tramite il ricevitore
            settingsLauncher.launch(intent);
        });
    }

    private void changeColorBackg(View mainLayout){
        int value = miocontatore.getValue();
        if (value > 0 && value % 10 == 0) {
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

        // Impostiamo il colore dei testi interni
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
                mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, lightbackg[0]));
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
            // Conversione dei pixel per il padding interno dei pulsanti (12dp sopra/sotto, 24dp lati)
            int paddingVertical = (int) (12 * getResources().getDisplayMetrics().density);
            int paddingHorizontal = (int) (24 * getResources().getDisplayMetrics().density);

            // ==========================================
            // TASTO CONFERMA
            // ==========================================
            Button positiveButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                // Sblocca il background del MaterialButton nativo
                positiveButton.setBackgroundTintList(null);
                positiveButton.setTextColor(colorDeepSpaceBlue);

                android.graphics.drawable.GradientDrawable shapePositive = new android.graphics.drawable.GradientDrawable();
                shapePositive.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                shapePositive.setCornerRadius(30);
                shapePositive.setColor(colorStrawberryRed); // Sfondo Rosso
                shapePositive.setStroke((int) (3 * getResources().getDisplayMetrics().density), colorDeepSpaceBlue); // Bordo Blu Scuro

                positiveButton.setBackground(shapePositive);
                positiveButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            }

            // ==========================================
            // TASTO ANNULLA (Invertito)
            // ==========================================
            Button negativeButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                // Sblocca il background del MaterialButton nativo
                negativeButton.setBackgroundTintList(null);
                negativeButton.setTextColor(colorSteelBlue); // Scritta colore del Dialog

                android.graphics.drawable.GradientDrawable shapeNegative = new android.graphics.drawable.GradientDrawable();
                shapeNegative.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                shapeNegative.setCornerRadius(30);
                shapeNegative.setColor(colorDeepSpaceBlue); // Sfondo Blu Scuro
                shapeNegative.setStroke((int) (3 * getResources().getDisplayMetrics().density), colorDeepSpaceBlue); // Bordo Blu Scuro

                negativeButton.setBackground(shapeNegative);
                negativeButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                // Distanziamo i due bottoni per non farli toccare
                android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                params.setMarginEnd((int) (16 * getResources().getDisplayMetrics().density));
                negativeButton.setLayoutParams(params);
            }
        });

        dialog.show();
    }
}