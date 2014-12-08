package com.aaron.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Main extends Activity {

    private ArrayList<Vivienda> viviendas = new ArrayList<Vivienda>();
    private ListView lv;
    private AdaptadorInmueble ad;
    private int index;
    private final int ACTIVIDAD_ELIMINAR = 1;
    private final int ACTIVIDAD_EDITAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
            initComponent();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        final Detalle fdetalle=(Detalle)getFragmentManager().findFragmentById(R.id.fDetalle);
        final boolean horizontal=fdetalle!=null && fdetalle.isInLayout();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String t=(String)lv.getItemAtPosition(i);
                if(horizontal){
                    fdetalle.inicia(viviendas.get(i).getId());
                }
                else{
                    Intent intent=new Intent(Main.this, Actividad.class);
                    intent.putExtra("id", viviendas.get(i).getId());
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.agregar) {
            edicion(index, "add");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Desplegar menú contextual*/
    @Override
    public void onCreateContextMenu(ContextMenu main, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(main, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.contextual, main);
    }

    /* Al seleccionar elemento del menú contextual */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        if (id == R.id.action_editar) {
            edicion(index, "edit");
            return true;
        }else if (id == R.id.action_eliminar) {
            edicion(index, "delete");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void initComponent()throws IOException, XmlPullParserException {
        ad = new AdaptadorInmueble(this, R.layout.elementoinmobiliaria, viviendas);
        lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        leer();
    }

    /***********Activities************/
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case ACTIVIDAD_ELIMINAR:
                    index= data.getIntExtra("index",0);
                    viviendas.remove(index);
                    break;
                case ACTIVIDAD_EDITAR:
                    index= data.getIntExtra("index",0);
                    String opcion=data.getStringExtra("opcion");
                    Vivienda v=data.getParcelableExtra("vivienda");
                    if(opcion.contains("edit")){
                        viviendas.get(index).setLocalidad(v.getLocalidad());
                        viviendas.get(index).setDireccion(v.getDireccion());
                        viviendas.get(index).setTipo(v.getTipo());
                        viviendas.get(index).setPrecio(v.getPrecio());
                    }
                    else{
                        viviendas.add(v);
                    }
                    ordenar();
                    break;
            }
            try {
                escribir();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ad.notifyDataSetChanged();
        }
        else{
            Log.v("data", (data == null) + "");
            tostada(R.string.mensajeCancelar);
        }
    }

    //Ordena las viviendas por ID
    public void ordenar(){
        Collections.sort(viviendas);
    }

    /* Mostramos un mensaje flotante a partir de un recurso string*/
    public void tostada(int s){
        Toast.makeText(this, getText(s), Toast.LENGTH_SHORT).show();
    }

    /*        Menús          */
    /*************************/
    public void edicion(int index,String opcion) {
        Intent i = new Intent(this, Edicion.class);
        Bundle b = new Bundle();
        Vivienda v;
        if (viviendas.size() > 0) {
            v = viviendas.get(index);
        } else {
            v = new Vivienda();
        }
        b.putString("opcion", opcion);
        b.putInt("index", index);
        b.putParcelable("vivienda", v);
        i.putExtras(b);
        if (opcion.equals("delete")) {
            startActivityForResult(i, ACTIVIDAD_ELIMINAR);
        } else {
            startActivityForResult(i, ACTIVIDAD_EDITAR);
        }
    }

    /********XML***********/
    public void escribir() throws IOException {
        File f = new File(getExternalFilesDir(null), "viviendas.xml");
        FileOutputStream fosxml = new FileOutputStream(f);
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        docxml.startTag(null, "raiz");
        for(Vivienda v : viviendas){
            docxml.startTag(null, "vivienda");
            docxml.startTag(null, "id");
            docxml.text(v.getId());
            docxml.endTag(null, "id");
            docxml.startTag(null, "localidad");
            docxml.text(v.getLocalidad());
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text(v.getDireccion());
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text(String.valueOf(v.getTipo()));
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text(String.valueOf(v.getPrecio()));
            docxml.endTag(null, "precio");
            docxml.endTag(null, "vivienda");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }

    public void leer() throws IOException, XmlPullParserException {
        XmlPullParser lectorxml= Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"viviendas.xml")),"utf-8");
        int evento = lectorxml.getEventType();
        String id="",local="",direc="",tipo="", precio="";
        while(evento != XmlPullParser.END_DOCUMENT){
            if(evento == XmlPullParser.START_TAG){
                String etiqueta = lectorxml.getName();
                if(etiqueta.compareTo("vivienda")==0){}
                if(etiqueta.compareTo("id")==0){
                    id = lectorxml.nextText();
                }
                if(etiqueta.compareTo("localidad")==0){
                    local = lectorxml.nextText();
                }
                if(etiqueta.compareTo("direccion")==0){
                    direc = lectorxml.nextText();
                }
                if(etiqueta.compareTo("tipo")==0){
                    tipo = lectorxml.nextText();
                }
                if(etiqueta.compareTo("precio")==0){
                    precio = lectorxml.nextText();
                    viviendas.add(new Vivienda(id, local, direc, Float.parseFloat(precio) ,Integer.parseInt(tipo)));
                }
            }
            evento = lectorxml.next();
        }
    }
}
