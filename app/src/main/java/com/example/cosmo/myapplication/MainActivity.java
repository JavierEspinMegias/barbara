package com.example.cosmo.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText campoBusq;
    ListView listaBus;
    Button botIntangi, botTangi;
    RadioButton silencio;

    String correo;

    String[] datosBusqueda;
    ArrayList datosBusArray[];

    AdminSQLiteOpenHelper admin;
    SQLiteDatabase baseDatos;
    Cursor fila;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        silencio = (RadioButton) findViewById(R.id.silencio);
        botIntangi = (Button) findViewById(R.id.intangible);
        botTangi = (Button) findViewById(R.id.tangible);
        campoBusq = (EditText)findViewById(R.id.campoBusq);

        //Carga de datos usuario
        datosBusqueda =  new String[6];
        correo = getIntent().getStringExtra("mailBusqueda");
        listaBus = (ListView)findViewById(R.id.listaBus);



    }
    public void onResume() {
        super.onResume();

    }
    public void buscar(View view){

        //Comprobamos visibilidad Botones
        if (listaBus.getVisibility()== listaBus.VISIBLE){
            listaBus.setVisibility(listaBus.INVISIBLE);
        }else{
            listaBus.setVisibility(listaBus.VISIBLE);
        }

        //Comprobamos correo
        if(!correo.isEmpty() || correo != null){

            //Preparamos Base de Datos
            admin = new AdminSQLiteOpenHelper(this, "administracion",null, 1);
            baseDatos = admin.getWritableDatabase();


            //Consulta de Base de Datos
            fila = baseDatos.rawQuery("select * from busquedas where email='"+correo+"'", null);

            //Preparamos recepcion de datos
            String[] res = {"","","","","","","",""};
            int cantBus = fila.getCount();
            String[] busquedasAnteriores = new String[cantBus];

            if (cantBus > 0) {
                //Leemos al reves en funcion del orden de insercion
                for (int i = cantBus - 1; i >= 0; i--) {
                    fila.moveToNext();
                    res[2] = ((fila.getString(2)));
                    res[3] = ((fila.getString(3)));
                    res[5] = ((fila.getString(5)));
                    busquedasAnteriores[i] = res[2] + " > " + res[5] + " > " + res[3];
                }

                //Preparamos e insertamos datos en la lista de busquedas
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layoutbusq, busquedasAnteriores);
                listaBus.setAdapter(adapter);
                abrirEnlace();

                Toast.makeText(this, cantBus+" busquedas en total", Toast.LENGTH_LONG).show();
                baseDatos.close();
            }else{
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layoutbusq, busquedasAnteriores);
                listaBus.setAdapter(adapter);
                Toast.makeText(this, "El usuario aun no ha realizado busquedas", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Fallo al capturar email. Vuelve a registrate", Toast.LENGTH_SHORT).show();
        }
    }
    public void vaciaTexto(View v){
        campoBusq.setText("");
    }
    public String modoBusqueda(){
        if (silencio.isChecked()){
            return "Silencioso";
        }else{
            return "Normal";
        }
    }
    public boolean busquedaVacia(View view){
        String buscar = campoBusq.getText().toString();
        if (buscar.equals("") || buscar.equals("Buscar...") || buscar.equals("No has escrito nada")){
            campoBusq.setText("No has escrito nada");
            return false;
        }
        return true;


    }
    public void intangi(View view){
        if (busquedaVacia(view)) {
            //Ir a Ideas con datos
            Intent intangi = new Intent(MainActivity.this, Ideas.class);
            infoBusqueda("ideas");
            intangi.putExtra("busqueda", datosBusqueda);
            vaciaTexto(view);
            startActivity(intangi);
        }
    }
    public void tangi(View view){
        if (busquedaVacia(view)){
            //Ir a Cosas con datos
            Intent tangi = new Intent(MainActivity.this, Cosas.class);
            infoBusqueda("cosas");
            tangi.putExtra("busqueda", datosBusqueda);
            vaciaTexto(view);
            startActivity(tangi);
        }
    }
    public void infoBusqueda(String clase){
        //Actualizamos informacion hacia otras Activities
        datosBusqueda[0]= getIntent().getStringExtra("mailBusqueda");
        datosBusqueda[1]= campoBusq.getText().toString();
        datosBusqueda[2]= modoBusqueda();
        datosBusqueda[3]= clase;
    }
    public void abrirEnlace(){

        //Reescribimos el click dentro de cada item de la lista
        listaBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Preparamos datos[]AUX
                String[] divide = ((String) listaBus.getAdapter().getItem(position).toString()).split(" > ");
                if (divide[1]=="cosas"){
                    infoBusqueda("cosas");
                    Intent tangi = new Intent(MainActivity.this, Cosas.class);
                    tangi.putExtra("busqueda", datosBusqueda);
                    vaciaTexto(view);
                }else{
                    infoBusqueda("ideas");
                    Intent intangi = new Intent(MainActivity.this, Ideas.class);
                    intangi.putExtra("busqueda", datosBusqueda);
                    vaciaTexto(view);
                }
                datosBusqueda[1]=divide[0];

                //Escogemos que tipo de busqueda se hace//////////////////////////////////////////////////////////////////////POR HACER
                Intent tangi = new Intent(MainActivity.this, Cosas.class);
                tangi.putExtra("busqueda", datosBusqueda);
                startActivity(tangi);
                listaBus.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void goAdmin(View v){
        Intent admin = new Intent(MainActivity.this, Admin.class);
        admin.putExtra("correo", correo);
        vaciaTexto(v);
        startActivity(admin);
    }

}


