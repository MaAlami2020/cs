package com.example.frutiapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2_Sumar2 extends AppCompatActivity {

    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_Atres, iv_vidas;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    int score, numAleatorio_uno, numAleatorio_dos, numAleatorio_tres, resultado;
    int vidas = 3;
    String nombre_jugador, string_score, string_vidas;

    String numero [] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //int resultado = this.resultado;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_sumar);

        Toast.makeText(this,"Nivel 2 - Sumas moderadas",Toast.LENGTH_SHORT).show();

        tv_nombre = (TextView) findViewById(R.id.textView_nombre);
        tv_score = (TextView) findViewById(R.id.textView_score);
        iv_vidas = (ImageView) findViewById(R.id.imageView_vidas);
        iv_Auno = (ImageView) findViewById(R.id.imageView_NumUno);
        iv_Ados = (ImageView)findViewById(R.id.imageView_NumDos);
        et_respuesta = (EditText)findViewById(R.id.editText_resultado);

        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);
        if(vidas == 3){
            iv_vidas.setImageResource(R.drawable.tresvidas);
        }if(vidas == 2){
            iv_vidas.setImageResource(R.drawable.dosvidas);
        }if(vidas == 1){
            iv_vidas.setImageResource(R.drawable.unavida);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        numAleatorio();
    }

    public void Comprobar(View view){
        String respuesta = et_respuesta.getText().toString();
        if(!respuesta.equals("")){
            int respuesta_jugador = Integer.parseInt(respuesta);
            if(respuesta_jugador == resultado){
                mp_great.start();
                score++;
                tv_score.setText("Score: " + score);
                et_respuesta.setText("");
                base_datos();
            }else{
                mp_bad.start();
                vidas--;
                base_datos();
                switch(vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this,"Te quedan 2 manzanas",Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this,"Te queda 1 manzana",Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this,"Has perdido todas tus manzanas",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }
                et_respuesta.setText("");
            }
            numAleatorio();
        }else{
            Toast.makeText(this,"escribe tu respuesta",Toast.LENGTH_SHORT).show();
        }
    }

    public void numAleatorio(){
        if(score <= 19){
            numAleatorio_uno = (int) (Math.random() * 10);
            numAleatorio_dos = (int) (Math.random() * 10);
            numAleatorio_tres = (int) (Math.random() * 10);

            resultado = numAleatorio_uno + numAleatorio_dos + numAleatorio_tres;
            
                for(int i = 0; i < numero.length; i++){
                    int id = getResources().getIdentifier(numero[i],"drawable", getPackageName());
                    if(numAleatorio_uno == i){
                        iv_Auno.setImageResource(id);
                    }if(numAleatorio_dos == i){
                        iv_Ados.setImageResource(id);
                    }if(numAleatorio_tres == i) {
                        iv_Atres.setImageResource(id);
                    }
                }
        }else{
            Intent intent = new Intent(this, MainActivity2_Menu.class);
            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);
            intent.putExtra("jugador",nombre_jugador);
            intent.putExtra("score",string_score);
            intent.putExtra("vidas",string_vidas);

            startActivity(intent);
            finish();
            mp.stop();
            mp.release();
        }
    }

    public void base_datos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_score);
            if(score > bestScore){
                ContentValues modification = new ContentValues();
                modification.put("nombre",nombre_jugador);
                modification.put("score",score);
                BD.update("puntaje",modification,"score=" + bestScore,null);
            }
            BD.close();
        }else{
            ContentValues introduce = new ContentValues();
            introduce.put("nombre",nombre_jugador);
            introduce.put("score",score);
            BD.insert("puntaje",null,introduce);
            BD.close();
        }
    }

}