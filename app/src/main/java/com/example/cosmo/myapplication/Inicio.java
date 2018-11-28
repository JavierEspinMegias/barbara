package com.example.cosmo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.PrecomputedText;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.PasswordAuthentication;
import java.security.KeyStore;

public class Inicio extends AppCompatActivity {

    EditText campEmail;
    EditText campPass;
    SharedPreferences guardado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        campEmail = (EditText) findViewById(R.id.email);
        campPass = (EditText) findViewById(R.id.pass);
        guardado = getSharedPreferences("users", Context.MODE_PRIVATE);

    }

    protected void onPause() {
        super.onPause();
        campEmail.setText("E-mail");
        campPass.setText("Contraseña");
    }

    protected void onResume() {
        super.onResume();
        guardado = getSharedPreferences("users", Context.MODE_PRIVATE);

        if (!getIntent().hasExtra("correo")) {
            campEmail = (EditText) findViewById(R.id.email);
            campPass = (EditText) findViewById(R.id.pass);

        } else {
            campEmail.setText(getIntent().getStringExtra("correo"));
            campPass.setText(getIntent().getStringExtra("pass"));
            campPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }


    public void goRegistro(View view) {
        Intent registro = new Intent(Inicio.this, Registro.class);
        startActivity(registro);

    }

    public void vaciaTextoEmail(View v){
        campEmail.setText("");
        campEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void vaciaTextoPass(View view){
        campPass.setText("");
        campPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void login(View view){
        if (!campEmail.getText().equals("") && !campPass.getText().equals("")){
            String mail = campEmail.getText().toString();
            String[] splitPass = guardado.getString(mail, "").split("-");

            if (guardado.contains(mail)){
                if (splitPass[0].equals(campPass.getText().toString())){
                    Toast.makeText(this, "Bienvenido: "+splitPass[1],Toast.LENGTH_SHORT).show();
                    Intent goMain = new Intent(Inicio.this, MainActivity.class);
                    goMain.putExtra("mailBusqueda", mail);
                    startActivity(goMain);
                }else{
                    Toast.makeText(this, "El usuario o la contraseña no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "El usuario o la contraseña no coinciden", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Faltan datos para el LOGIN", Toast.LENGTH_LONG).show();
        }
    }
}
