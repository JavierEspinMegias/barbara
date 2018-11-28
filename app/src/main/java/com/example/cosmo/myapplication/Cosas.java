package com.example.cosmo.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class
Cosas extends AppCompatActivity {

    TextView res;
    String busqueda, amazon;
    LinearLayout red, linearNavega, listaWebs, listaTag;
    WebView web;
    Button botonBusqueda, botonNueva, botonVolver, elcorte, audio, media, aliexpress, google, ebay, fnac;
    CheckBox belleza, motor, deportes, moda, hogar, ocio, electronica, viajar, salud;
    EditText campWeb;
    String[] datosBusqueda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tangible);

        campWeb = (EditText)findViewById(R.id.campWeb);
        listaTag = (LinearLayout)findViewById(R.id.listaTag);

        red = (LinearLayout)findViewById(R.id.redBusquedas);
        web = (WebView)findViewById(R.id.webViewCosas);
        botonBusqueda = (Button)findViewById(R.id.busquedaTangible);

        linearNavega = (LinearLayout)findViewById(R.id.lineaNavega);
        listaWebs = (LinearLayout) findViewById(R.id.listaWebs);

        belleza = (CheckBox)findViewById(R.id.belleza);
        deportes = (CheckBox)findViewById(R.id.deportes);
        moda = (CheckBox)findViewById(R.id.moda);
        ocio = (CheckBox)findViewById(R.id.ocio);
        electronica = (CheckBox)findViewById(R.id.electronica);
        viajar = (CheckBox)findViewById(R.id.viajar);

        listaWebs.setVisibility(View.INVISIBLE);

        web.setWebViewClient(new WebViewClient());
        elcorte = (Button)findViewById(R.id.elcorte);
        audio = (Button)findViewById(R.id.audio);


        datosBusqueda = getIntent().getStringArrayExtra("busqueda");
        busqueda = datosBusqueda[1];
    }
    public void vaciaTexto(View view){campWeb.setText("");}
    public void buscar(View view){

        String buscarWeb = campWeb.getText().toString();
        if (!buscarWeb.equals("") || !buscarWeb.equals(findViewById(R.id.campWeb).toString())) {

            listaTag.setVisibility(View.INVISIBLE);
            red.setVisibility(View.INVISIBLE);
            listaWebs.setVisibility(View.VISIBLE);
            botonBusqueda.setVisibility(View.INVISIBLE);
            linearNavega.setVisibility(View.VISIBLE);
            filtrar(view);
        }else{
            listaTag.setVisibility(View.INVISIBLE);
            red.setVisibility(View.INVISIBLE);
            botonBusqueda.setVisibility(View.INVISIBLE);
            linearNavega.setVisibility(View.VISIBLE);
            web.setVisibility(View.VISIBLE);
            web.loadUrl("https://www.google.com/search?q=site%20"+buscarWeb+" "+busqueda);
            recogeBusqueda("GoogleSite", web.getUrl());
        }

    }
    public void volver(View view){
        listaTag.setVisibility(View.VISIBLE);
        listaWebs.setVisibility(View.INVISIBLE);
        red.setVisibility(View.VISIBLE);
        botonBusqueda.setVisibility(View.VISIBLE);
        linearNavega.setVisibility(View.INVISIBLE);
        web.setVisibility(View.INVISIBLE);
    }
    public void nueva(View view){
        Toast.makeText(this, "Nueva busqueda", Toast.LENGTH_SHORT).show();
        Intent nueva = new Intent(this, MainActivity.class);
        nueva.putExtra("mailBusqueda", datosBusqueda[0]);
        startActivity(nueva);
    }
    public void filtrar(View view){
        if(!moda.isChecked() && !belleza.isChecked() && !viajar.isChecked()){
            elcorte.setVisibility(View.INVISIBLE);
        }
        if (!electronica.isChecked()){
            audio.setVisibility(View.INVISIBLE);
        }
    }
    public void abrirWeb(View v){
        String link = getLink(v);
        String id = ((Button)findViewById(v.getId())).getText().toString();
        listaWebs.setVisibility(View.INVISIBLE);
        web.setVisibility(View.VISIBLE);
        web.setInitialScale(100);
        web.loadUrl(link+busqueda);
        recogeBusqueda(id, link);
        if (busqueda!=""){
            Toast.makeText(this, "Has buscado: "+busqueda+" en "+ id, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "No hay campo de busqueda. Datos no guardados", Toast.LENGTH_LONG).show();
        }

    }
    public String getLink(View v){
        String web = "";
        String id = ((Button)findViewById(v.getId())).getText().toString();
        switch (id){
            case "Aliexpress":
                web = "https://es.aliexpress.com/wholesale?SearchText=";
                break;
            case "Amazon":
                web = "https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=";
                break;
            case "Ebay":
                web = "https://www.ebay.com/sch/i.html?_from=R40&_trksid=m570.l1313&_nkw=";
                break;
            case "Google Shooping":
                web = "https://www.google.es/search?tbm=shop&hl=es&source=hp&biw=&bih=&q=+pepe&oq=+";
                break;
            case "Fnac":
                web = "https://www.fnac.es/SearchResult/ResultList.aspx?SCat=0%211&Search=";
                break;
            case "Elcorte":
                web = "https://www.elcorteingles.es/search/?s=";
                break;
            case "Audio":
                web = "https://www.audiotronics.es/products.aspx?search=";
                break;
            case "MediaMarkt":
                web = "https://www.mediamarkt.es/es/search.html?searchProfile=onlineshop&query=";
                break;
        }
        return web;
    }
    public void recogeBusqueda(String pOpcion, String link){
        if (datosBusqueda[1]!=""){
            datosBusqueda[4] = pOpcion;
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


            //Vaciamos campos
            //codigo.setText("");
            //nombre.setText("");
            //precio.setText("");

        }
    }
}
