package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity2_Menu extends AppCompatActivity {

    String nombre_jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_menu);
    }

    public void Sumar(View view){
            Intent intent = new Intent(this, MainActivity2_Sumar.class);

            nombre_jugador = getIntent().getStringExtra("jugador");
            intent.putExtra("jugador",nombre_jugador);
            startActivity(intent);
            finish();
    }

    public void Home(View view){
        Intent intent = new Intent(this, MainActivity.class);

        nombre_jugador = getIntent().getStringExtra("jugador");
        intent.putExtra("jugador",nombre_jugador);
        startActivity(intent);
        finish();
    }
}