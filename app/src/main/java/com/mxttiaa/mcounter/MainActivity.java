package com.mxttiaa.mcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    int value = 0;
    int gap = 1;

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

    // In questo modo "sopravvive" tra un click e l'altro
    private int lastIdxCloolor = -1;
    private final java.util.Random random = new java.util.Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Installa la splash screen PRIMA di super.onCreate
        androidx.core.splashscreen.SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textValue = findViewById(R.id.valueCounter);

        View mainLayout = findViewById(R.id.main);
        //Collega il layout principale per potergli cambiare colore

        Button plusBotton = findViewById(R.id.plus);
        plusBotton.setOnClickListener(v ->{
            if(value + gap <= 99999){
                value += gap;
                textValue.setText(String.valueOf(value));
                //si può fare anche value + ""

                changeColorBackg(value, mainLayout);
                //cambia colore dello sfondo ogni 10
            }
            else{
                Toast.makeText(MainActivity.this, "Hai raggiunto il massimo.", Toast.LENGTH_SHORT).show();
            }
        });

        Button minusBotton = findViewById(R.id.minus);
        minusBotton.setOnClickListener(v ->{
            if(value - gap >= 0){
                value -= gap;
                textValue.setText(String.valueOf(value));
                //si può fare anche value + ""

                changeColorBackg(value, mainLayout);
                //cambia colore dello sfondo ogni 10
            }
            else{
                Toast.makeText(MainActivity.this, "Hai già raggiunto lo zero.", Toast.LENGTH_SHORT).show();
            }
        });

        Button resetBotton = findViewById(R.id.resetButt);
        resetBotton.setOnClickListener(v -> showDialogReset(mainLayout, textValue));

        //Pulsante Settings
        Button settingsBotton = findViewById(R.id.settingsButt);
        settingsBotton.setOnClickListener(v ->{
            // Creiamo l'Intent
            // Argomenti: (Dove sono ora, Dove voglio andare)
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

            // Diciamo ad Android di eseguire l'ordine
            startActivity(intent);
        });
    }

    private void changeColorBackg(int value, View mainLayout){
        if (value > 0 && value % 10 == 0) {
            int idxCoolor;

            do{
                //numero casuale tra 0 e colori totali
                idxCoolor = random.nextInt(lightbackg.length);
            }while(idxCoolor == lastIdxCloolor);

            lastIdxCloolor = idxCoolor;

            int myColor = lightbackg[idxCoolor];
            //colore estratto

            // Nota: in Android moderno serve "ContextCompat" per tradurre il colore in un formato leggibile dallo schermo
            mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, myColor)); //applicazione del colore sullo sfondo

            // Questa riga stampa un messaggio segreto nella console di Android Studio
            //android.util.Log.d("DEBUG_COLORE", "Indice estratto: " + idxCoolor);
        }
    }

    private void showDialogReset(View mainLayout, TextView textValue){
        // Il Builder ci aiuta a costruire la finestra passo dopo passo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Conferma Reset");
        builder.setMessage("Sei sicuro di voler resettare il contatore?");

        // Impostiamo il pulsante di conferma (positivo)
        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                value = 0;
                textValue.setText(String.valueOf(value));
                mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, lightbackg[0]));
            }
        });

        // Impostiamo il pulsante per annullare (negativo)
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chiudiamo semplicemente il dialog senza fare nulla
                dialog.dismiss();
            }
        });

        // Creiamo e mostriamo a schermo la finestra
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}