package com.example.cosmo.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {
    SharedPreferences guardado;
    EditText correo, pass, pass2, nick, nombre;
    boolean existeEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        guardado = getSharedPreferences("users", Context.MODE_PRIVATE);
        correo = (EditText) findViewById(R.id.emailReg);
        pass = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        nick = (EditText)findViewById(R.id.nick);
        nombre = (EditText)findViewById(R.id.nombre);
        existeEmail = true;

    }

    public void vaciarTexto(View v){
        ((EditText)findViewById(v.getId())).setText("");
    }

    public void crearCuenta(View v){
        comprobarEmail(v);
        if (!existeEmail && comprobarPass() && comprobarNombreNick()){
            SharedPreferences.Editor obj_editor = guardado.edit();

            String values = pass.getText().toString() + "-" + nick.getText().toString() + "-" + nombre.getText().toString();

            obj_editor.putString(correo.getText().toString(),values);
            obj_editor.commit();

            Toast.makeText(this, "Cuenta creada con exito!", Toast.LENGTH_LONG).show();
            Intent volverInicio = new Intent(Registro.this, Inicio.class);
            volverInicio.putExtra("correo", correo.getText().toString());
            volverInicio.putExtra("pass", pass.getText().toString());
            startActivity(volverInicio);
        }

    }
    public void comprobarEmail(View v){
        String email = correo.getText().toString();
        if (guardado.contains(email)){
            existeEmail=true;
            Toast.makeText(this, "El correo ya existe", Toast.LENGTH_LONG).show();
            correo.setText("");
        }else{
            if (!email.contains("@") && !email.contains(".")){
                Toast.makeText(this, "No me trolees con el correo", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "El correo es valido", Toast.LENGTH_SHORT).show();
                existeEmail=false;
            }
        }
    }
    public boolean comprobarPass(){
        boolean passIgual = false;
        if (pass.getText().toString().contains(" ")){
            Toast.makeText(this, "Las contraseñas no pueden llevar espacios", Toast.LENGTH_LONG).show();
        }else{
            if (pass.getText().toString().equals(pass2.getText().toString())){
                passIgual = true;
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        }
        return passIgual;
    }
    public boolean comprobarNombreNick(){
        boolean puede = true;
        if (nick.getText().toString().isEmpty() && nombre.getText().toString().isEmpty()){
            puede = false;
            Toast.makeText(this, "No puede haber campos vacios!", Toast.LENGTH_LONG).show();
        }
        return puede;
    }

}
