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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conferma Reset");
        builder.setMessage("Sei sicuro di voler resettare il contatore?");

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

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}