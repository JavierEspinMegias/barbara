package com.example.cosmo.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android.util.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class Admin extends AppCompatActivity {
    EditText nombreArchivo;
    Button aceptar;
    String correo;
    LinearLayout botones;

    AdminSQLiteOpenHelper admin;
    SQLiteDatabase baseDatos;
    Cursor fila;
    ListView listabusquedas;
    ArrayList<Busquedas> listaBusquedasJson;
    Busquedas[] busquedasTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        botones = (LinearLayout)findViewById(R.id.botones);
        listabusquedas = (ListView)findViewById(R.id.listabusquedas);


        //Cargamos datos de usuario
        correo = getIntent().getStringExtra("correo");
        listaBusquedasJson = new ArrayList<Busquedas>();
        busquedasTotal = new Busquedas[]{};
    }


    public void leerInterno(View v){
        String nomArchivo = "datos.txt";
        String archivos [] = fileList();

        if(archivoExiste(archivos, nomArchivo)){
           try {

               InputStreamReader lectura = new InputStreamReader(openFileInput(nomArchivo));
               List<String> lista = new ArrayList<String>();

               //Leemos en Strings
               BufferedReader br = new BufferedReader(lectura);

               //Leemos datos en String
               String leida="";

               //Leemos datos para parsear a JSON y despues a obj. Busquedas
               String total = "";
               while((leida = br.readLine())!= null){
                   if (!leida.equals("[") && !leida.equals("]")){
                       lista.add(leida);
                   }
                   total=total+leida;
               }
               br.close();
               lectura.close();


               // ------------ Lectura en JSON ------
               // Cargamos un txt - Convertimos a JSON -
               // Convertimos a objetos Busqueda
               // Cargamos en memoria las Busquedas
               InputStream is = new ByteArrayInputStream(Charset.forName("UTF-8").encode(total).array());
               List<Busquedas> busquedasJson = readJsonStream(is);

               // Mostramos los datos cargados desde el txt
               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.layoutbusq, lista );
               listabusquedas.setAdapter(arrayAdapter);

           } catch (FileNotFoundException e) {
                e.printStackTrace();
           } catch (IOException e) {
                e.printStackTrace();
           }
        }else{
            Toast.makeText(this, "No hay datos guardados con ese nombre", Toast.LENGTH_SHORT).show();
        }

    }

    public void guardarInterno(View view)  {

        //Cargamos en memoria las busquedas del usuario
        buscar();


        //Actualizamos Array de Busquedas
        for(int i = 0; i < busquedasTotal.length;i++){
            listaBusquedasJson.add(busquedasTotal[i]);
        }

        //BOTONES -> ALERTA
        //botones.setVisibility(View.INVISIBLE);
        //marcoTexto.setVisibility(View.VISIBLE);
        //marcoAlerta.setVisibility(View.INVISIBLE);

        String nomArchivo = "datos.txt";
        if (busquedasTotal.length>0){
            Toast.makeText(this, "El usuario tiene "+busquedasTotal.length+" busquedas",Toast.LENGTH_SHORT).show();
            try {
                //Abrimos flujo de escritura
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(nomArchivo, Activity.MODE_PRIVATE));


                //Escribimos el contenido en JSON
                //Abrimos el array de busquedas
                String busAux = "[\n";

                //Parseamos a JSON el objeto busqueda
                for (int i = 0; i<busquedasTotal.length;i++){
                    if (i == busquedasTotal.length-1){
                        busAux=busAux+busquedasTotal[i].toJSON();
                    }else{
                        busAux=busAux+busquedasTotal[i].toJSON()+",\n";
                    }
                }

                //Cerramos array de busquedas
                busAux=busAux+"\n]";

                //Escribimos en txt
                archivo.write(busAux);

                //Limpiamos el buffer
                archivo.flush();

                //Cerramos el flujo de escritura
                archivo.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //Notificamos
            Toast.makeText(this, "El usuario no tiene nada guardado",Toast.LENGTH_SHORT).show();

        }
        //Notificamos
        Toast.makeText(this, "Archivo guardado correctamente",Toast.LENGTH_SHORT).show();



    }
    public void buscar() {
        //Comprobamos correo
        if (!correo.isEmpty()) {

            //Preparamos Base de Datos
            admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
            baseDatos = admin.getWritableDatabase();


            //Consulta de Base de Datos del usuario registrado
            //fila = baseDatos.rawQuery("select * from busquedas where email='" + correo + "'", null);
            //Consulta de toda la Base de Datos
            fila = baseDatos.rawQuery("select * from busquedas", null);

            //Preparamos recepcion de datos
            String[] res = {"", "", "", "", "", "", ""};
            int cantBus = fila.getCount();
            busquedasTotal = new Busquedas[cantBus];
            if (cantBus > 0) {
                //Leemos al reves en funcion del orden de insercion -> La ultima busqueda es la primera en aparecer
                for (int i = cantBus - 1; i >= 0; i--) {
                    fila.moveToNext();
                    res[0] = ((fila.getString(1)));
                    res[1] = ((fila.getString(2)));
                    res[2] = ((fila.getString(3)));
                    res[3] = ((fila.getString(4)));
                    res[4] = ((fila.getString(5)));
                    res[5] = ((fila.getString(6)));

                    //Ingresamos datos para crear los objetos Busquedas
                    Busquedas aux = new Busquedas(res[0],res[1],res[2],res[3],res[4],res[5]);
                    busquedasTotal[i] = aux;
                }
                Toast.makeText(this, "Busquedas cargadas en memoria", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "No hay busquedas", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Fallo al capturar email. Vuelve a registrarte", Toast.LENGTH_SHORT).show();
        }


    }


    private boolean archivoExiste(String archivos [], String nom_archivo){
        boolean existe = false;

        //Comprobamos si el archivo existe
        for (int i=0;i<archivos.length;i++){
            if(nom_archivo.equals(archivos[i])){
                existe = true;
            }
        }
        return existe;
    }



    private boolean continuarBorrado(View v){
        String respuesta = ((Button)findViewById(v.getId())).getText().toString();
        if (respuesta.equals("aceptar")){
            return true;
        }else{
            return false;
        }
    }
    public void buscarTodas() {
        //Comprobamos correo
        if (!correo.isEmpty() || correo != null) {

            //Preparamos Base de Datos
            admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
            baseDatos = admin.getWritableDatabase();


            //Consulta de Base de Datos
            fila = baseDatos.rawQuery("select * from busquedas", null);

            //Preparamos recepcion de datos
            String[] res = {"", "", "", "", "", "", "", ""};
            int cantBus = fila.getCount();
            String[] busquedasAnteriores = new String[cantBus];

            if (cantBus > 0) {
                //Leemos al reves en funcion del orden de insercion
                for (int i = cantBus - 1; i >= 0; i--) {
                    fila.moveToNext();
                    res[2] = ((fila.getString(2)));
                    res[3] = ((fila.getString(3)));
                    res[5] = ((fila.getString(5)));
                    //Ingresamos datos para crear las busquedas                                       /////////////// por completar
                    //arrayBusquedas.add(new String(" "));
                }

            } else {
                Toast.makeText(this, "Fallo al capturar email. Vuelve a registrate", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void leerExterno(View view){
        //Recuperamos nombre del archivo que queremos leer
        String nom_archivo = nombreArchivo.getText().toString();

        //Recuperamos la ruta donde está la tarjeta SD
        //File tarjetaSD = Environment.getExternalStorageDirectory();

        //Creamos el archivo
        // File rutaArchivo = new File (tarjetaSD.getPath(), nom_archivo);

        try {
            //Abrimos flujo de lectura a nuestro archivo
            InputStreamReader leerArchivo = new InputStreamReader(openFileInput(nom_archivo));

            //Leemos nuestro archivo
            BufferedReader br = new BufferedReader(leerArchivo);

            String linea="";
            String cont="";

            while((linea = br.readLine()) != null){
                cont += linea;
            }

            br.close();
            leerArchivo.close();

            //Mostramos el contenido
            //contenido.setText(cont);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void guardarExterno(View view){

        //Recuperamos el nombre del archivo en el que queremos guardar y el contenido a guardar
        String nom_archivo = nombreArchivo.getText().toString();
        String cont = "";

        //Recuperamos la ruta donde está la tarjeta SD
        //File tarjetaSD = Environment.getExternalStorageDirectory();

        //Mostramos la ruta donde está la tarjeta SD
        //Toast.makeText(this, tarjetaSD.getPath(),Toast.LENGTH_SHORT).show();

        //Creamos el archivo
        //File rutaArchivo = new File (tarjetaSD.getPath(), nom_archivo);


        try {
            //Abrimos flujo de lectura a nuestro archivo
            OutputStreamWriter crearArchivo = new OutputStreamWriter(openFileOutput(nom_archivo, Activity.MODE_PRIVATE));

            //Escribir archivo
            crearArchivo.write(cont);

            //Borramos buffer
            crearArchivo.flush();

            //Cerramos
            crearArchivo.close();

            Toast.makeText(this, "Fichero guardado correctamente", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error.Fichero no guardado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error.Fichero no guardado", Toast.LENGTH_SHORT).show();
        }
    }



    public List<Busquedas> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return leerArrayBusquedas(reader);
        } finally {
            reader.close();
        }
    }

    public List<Busquedas> leerArrayBusquedas(JsonReader reader) throws IOException {
        List<Busquedas> busquedasList = new ArrayList<Busquedas>();

        reader.beginArray();
        while (reader.hasNext()) {
            busquedasList.add(leerBusquedas(reader));
        }
        reader.endArray();
        return busquedasList;
    }

    public Busquedas leerBusquedas(JsonReader reader) throws IOException {
        String correoBus="";
        Busquedas aux = new Busquedas(correoBus,"","","","","");
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("emailBus")) {
                aux.setEmailBus(reader.nextString());
            } else if (name.equals("bus")) {
                aux.setBus(reader.nextString());
            } else if (name.equals("modoBus")) {
                aux.setModoBus(reader.nextString());
            } else if (name.equals("claseBus")) {
                aux.setClaseBus(reader.nextString());
            } else if (name.equals("opcionBus")) {
                aux.setOpcionBus(reader.nextString());
            } else if (name.equals("linkBus")) {
                aux.setLinkBus(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return aux;
    }

}
