package com.mxttiaa.mcounter;

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

public class MainActivity extends AppCompatActivity {

    int value = 0;
    int gap = 1;

    int[] lightbackg = {
            R.color.honeydew,
            R.color.lightStrawberry,
            R.color.lightSteelBlue,
            R.color.lightSand,
            R.color.lightLavender,
            R.color.lightMint
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textValue = findViewById(R.id.valueCounter);

        Button plusBotton = findViewById(R.id.plus);
        plusBotton.setOnClickListener(v ->{
            if(value + gap <= 99999){
                value += gap;
                textValue.setText(String.valueOf(value)); //si può fare anche value + ""

                View mainLayout = findViewById(R.id.main); //Colleghiamo il layout principale per potergli cambiare colore

                if (value > 0 && value % 10 == 0) {
                    java.util.Random random = new java.util.Random(); //oggetto random

                    int idxCoolor = random.nextInt(lightbackg.length); //numero casuale tra 0 e colori totali

                    int myColor = lightbackg[idxCoolor]; //colore estratto

                    // Nota: in Android moderno serve "ContextCompat" per tradurre il colore in un formato leggibile dallo schermo
                    mainLayout.setBackgroundColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, myColor)); //applicazione del colore sullo sfondo
                }
            }
            else{
                Toast.makeText(MainActivity.this, "Hai raggiunto il massimo.", Toast.LENGTH_SHORT).show();
            }
        });

        Button minusBotton = findViewById(R.id.minus);
        minusBotton.setOnClickListener(v ->{
            if(value - gap >= 0){
                value -= gap;
                textValue.setText(String.valueOf(value)); //si può fare anche value + ""
            }
            else{
                Toast.makeText(MainActivity.this, "Hai già raggiunto lo zero.", Toast.LENGTH_SHORT).show();
            }
        });

        Button resetBotton = findViewById(R.id.resetButt);
        resetBotton.setOnClickListener(v ->{
            value = 0;
            textValue.setText(String.valueOf(value));
        });
    }

}