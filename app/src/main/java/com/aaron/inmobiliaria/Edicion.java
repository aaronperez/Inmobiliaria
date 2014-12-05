package com.aaron.inmobiliaria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class Edicion extends Activity {

    private Spinner spinner;
    private int index;
    private EditText etLocal,etCalle,etPrecio;
    private Vivienda v;
    private String opcion, identificador;
    private TextView titulo;
    private Button bOK;
    private SharedPreferences prefs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);
        initComponent();
    }

    public void initComponent(){
        prefs =getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        etLocal=(EditText)findViewById(R.id.etLocalidad);
        etCalle=(EditText)findViewById(R.id.etDireccion);
        etPrecio=(EditText)findViewById(R.id.etPrecio);
        titulo=(TextView)findViewById(R.id.tvTitulo);
        bOK=(Button)findViewById(R.id.bOK);
        iniciarSpinner();
        Bundle b = getIntent().getExtras();
        opcion=b.getString("opcion");
        if(b !=null){
            if(opcion.contains("edit")){
                v=b.getParcelable("vivienda");
                titulo.setText(getText(R.string.editari));
                bOK.setText(getText(R.string.editar));
                etLocal.setText(v.getLocalidad());
                etCalle.setText(v.getDireccion());
                etPrecio.setText(v.getPrecio() + "");
                identificador=v.getId();
                for (int i = 0; i < 4; i++) {
                    if (spinner.getItemAtPosition(i).toString().substring(1, 3).equals(this.getResources().getResourceEntryName(v.getTipo()).toString().substring(1, 3))) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            }
            else{
                titulo.setText(getText(R.string.agregari));
                bOK.setText(getText(R.string.agregar));
                etLocal.setText("");
                etCalle.setText("");
                etPrecio.setText("");
            }
            index=b.getInt("index");
        }
    }

    public void aceptar(View v){
        Intent i = new Intent();
        Bundle bundle= new Bundle();
        bundle.putInt("index", index);
        bundle.putString("opcion", opcion);
        if(!opcion.contains("edit")){
            identificador=generarId();
        }
        Vivienda casa=rellena();
        bundle.putParcelable("vivienda",casa);
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public Vivienda rellena(){
        int img=spinnerDraw();
        return new Vivienda(identificador, etLocal.getText().toString(),etCalle.getText().toString(),Float.parseFloat(etPrecio.getText().toString()),img);
    }

    public void cancelar(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /* Método para generar un ID nuevo desde Preferencias compartidas*/
    public String generarId(){
        int id = prefs.getInt("id", 0);
        id++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("id", id);
        editor.commit();
        return id+"";
    }

    /*  Método para iniciar Spinner y escucharlo  */
    private void iniciarSpinner(){
        ArrayAdapter<CharSequence> stringArrayAdapter=ArrayAdapter.createFromResource(this,R.array.Tipo,android.R.layout.simple_spinner_dropdown_item);
        spinner =(Spinner)findViewById(R.id.spinnerM);
        spinner.setAdapter(stringArrayAdapter);
    }


    /* Método que convierte la posición en
    * el spinner en la imagen que le corresponde*/
    private int spinnerDraw(){
        Context n=Edicion.this.getApplicationContext();
        Resources resources = n.getResources();
        String nombre=spinner.getSelectedItem().toString().toLowerCase();
        final int resourceId = resources.getIdentifier(nombre, "drawable",n.getPackageName());
        return resourceId;
    }

}