package com.aaron.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Eliminar extends Activity {

    private TextView tvLocalidad,tvDireccion,tvPrecio;
    private ImageView ivTipo;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        initComponent();
    }

    public void initComponent(){
        tvLocalidad=(TextView)findViewById(R.id.tvLocalidad);
        tvDireccion=(TextView)findViewById(R.id.tvDireccion);
        tvPrecio=(TextView)findViewById(R.id.tvPrecio);
        ivTipo=(ImageView)findViewById(R.id.ivTipo);
        Bundle b = getIntent().getExtras();
        Vivienda v=b.getParcelable("vivienda");
        if(b !=null){
            tvLocalidad.setHint(v.getLocalidad());
            tvDireccion.setHint(v.getDireccion());
            tvPrecio.setHint(v.getPrecio()+" "+getString(R.string.moneda));
            ivTipo.setImageResource(v.getTipo());
            index=b.getInt("index");
        }
    }


    public void aceptar(View v){
        Intent i = new Intent();
        Bundle bundle= new Bundle();
        bundle.putInt("index", index);
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void cancelar(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
