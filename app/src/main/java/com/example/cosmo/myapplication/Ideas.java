package com.example.cosmo.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Ideas extends AppCompatActivity {
    TextView res;
    String busqueda = "";
    LinearLayout linearDocu;
    String[] datosBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intangible);


        linearDocu = (LinearLayout)findViewById(R.id.docuLinear);
        datosBusqueda = getIntent().getStringArrayExtra("busqueda");
        busqueda = datosBusqueda[1];

    }
    public void onResume() {
        super.onResume();
    }

    public void documentos(View v){
        String id =  ((Button)findViewById(v.getId())).getText().toString();
        String link = getLink(id);
        recogeBusqueda("Noticias", link);
        Intent documentos = new Intent(this, Documentos.class);
        Toast.makeText(this, "Buscando: "+busqueda+"\n en " + this.getLocalClassName() + " de Documentos", Toast.LENGTH_SHORT).show();
        documentos.putExtra("campoBusq", busqueda);
        documentos.putExtra("email", datosBusqueda[0]);
        startActivity(documentos);
    }
    public void infoDocumentos(View view){
        view.setBackgroundColor(1);
        Toast.makeText(this, "Vas a buscar solo documentos", Toast.LENGTH_SHORT).show();
    }
    public void imagen(View v){
        String id =  ((Button)findViewById(v.getId())).getText().toString();
        String link = getLink(id);
        recogeBusqueda("Imagen", link);
        Intent imagen = new Intent(this, Imagen.class);
        Toast.makeText(this, "Buscando: "+busqueda+"\n en " + this.getLocalClassName()+ " de Imagen", Toast.LENGTH_SHORT).show();
        imagen.putExtra("campoBusq", busqueda);
        imagen.putExtra("email", datosBusqueda[0]);
        startActivity(imagen);
    }
    public void infoImagen(View view){
        view.setBackgroundColor(1);
        Toast.makeText(this, "Vas a buscar solo imagenes", Toast.LENGTH_SHORT).show();
        //view.transi
        view.setBackgroundColor(1000);
    }
    public void audio(View v){
        String id =  ((Button)findViewById(v.getId())).getText().toString();
        String link = getLink(id);
        recogeBusqueda("Audio", link);
        Intent audio = new Intent(this, Audio.class);
        Toast.makeText(this, "Buscando: "+busqueda+"\n en " + this.getLocalClassName()+ " de Audio/Video", Toast.LENGTH_SHORT).show();
        audio.putExtra("campoBusq", busqueda);
        audio.putExtra("email", datosBusqueda[0]);
        startActivity(audio);
    }
    public void infoAudio(View view){
        view.setBackgroundColor(1);
        Toast.makeText(this, "Vas a buscar solo audio o video", Toast.LENGTH_SHORT).show();
    }
    public void noticias(View v){
        String id =  ((Button)findViewById(v.getId())).getText().toString();
        String link = getLink(id);
        recogeBusqueda("Noticias", link);
        Intent noticias = new Intent(this, Noticias.class);
        Toast.makeText(this, "Buscando: "+busqueda+ " en Noticias", Toast.LENGTH_SHORT).show();
        noticias.putExtra("campoBusq", busqueda);
        noticias.putExtra("email", datosBusqueda[0]);
        startActivity(noticias);
    }
    public void infoNoticias(View view){
        view.setBackgroundColor(1);
        Toast.makeText(this, "Vas a buscar solo noticias", Toast.LENGTH_SHORT).show();
    }
    public void archivos(View v){
        String id =  ((Button)findViewById(v.getId())).getText().toString();
        String link = getLink(id);
        recogeBusqueda("Archivos", link);
        Toast.makeText(this, "Buscando: "+busqueda+"\n en Archivos", Toast.LENGTH_SHORT).show();
        Intent archivos = new Intent(this, Archivos.class);
        archivos.putExtra("campoBusq", busqueda);
        archivos.putExtra("email", datosBusqueda[0]);
        startActivity(archivos);
    }
    public void infoArchivos(View view){
        view.setBackgroundColor(1);
        Toast.makeText(this, datosBusqueda[0], Toast.LENGTH_SHORT).show();
    }
/*
    public void abrirWeb(View v){
        String id =  ((Button)findViewById(v.getId())).getText().toString();
        String link = getLink(id);
        if (datosBusqueda[1]!=""){
            Toast.makeText(this, "Has buscado: "+busqueda+" en "+ link, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "No hay campo de busqueda. Datos no guardados", Toast.LENGTH_LONG).show();
        }

        Intent goWeb = new Intent(this, Noticias.class);
        Toast.makeText(this, "Buscando: "+busqueda+"\n en " + this.getLocalClassName()+ " de Noticias", Toast.LENGTH_SHORT).show();
        goWeb.putExtra("campoBusq", busqueda);
        startActivity(goWeb);
    }*/

    public String getLink(String id){
        String web = "";
        switch (id){
            case "Archivos":
                web = "https://es.aliexpress.com/wholesale?SearchText=";
                break;
            case "Docu":
                web = "https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=";
                break;
            case "Imagen":
                web = "https://www.ebay.com/sch/i.html?_from=R40&_trksid=m570.l1313&_nkw=";
                break;
            case "Noticias":
                web = "https://www.google.es/search?tbm=shop&hl=es&source=hp&biw=&bih=&q=+pepe&oq=+";
                break;
            case "Audio":
                web = "https://www.fnac.es/SearchResult/ResultList.aspx?SCat=0%211&Search=";
                break;
        }
        return web;
    }

    public void recogeBusqueda(String opcion, String link) {
        if (datosBusqueda[1]!= ""){

            datosBusqueda[4] = opcion;
            datosBusqueda[5] = link;
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null, 1);
            SQLiteDatabase baseDatos = admin.getWritableDatabase();

            //Creamos contenedor de valores y guardamos datos del usuario en él
            //codBus int primary key, email text, busqueda text, modo text, clase text, opcion text, link text)
            ContentValues datos = new ContentValues();
            datos.put("email", datosBusqueda[0]);
            datos.put("busqueda", datosBusqueda[1]);
            datos.put("modo", datosBusqueda[2]);
            datos.put("clase", datosBusqueda[3]);
            datos.put("opcion", datosBusqueda[4]);
            datos.put("link", datosBusqueda[5]+datosBusqueda[1]);

            //Insertamos los datos en la BD
            baseDatos.insert("busquedas", null, datos);

            //Cerramos conexión
            baseDatos.close();

            Toast.makeText(this, "Has buscado "+ datosBusqueda[1]+" en "+opcion, Toast.LENGTH_LONG).show();
            //Vaciamos campos
            //codigo.setText("");
            //nombre.setText("");
            //precio.setText("");
        }
    }
}
