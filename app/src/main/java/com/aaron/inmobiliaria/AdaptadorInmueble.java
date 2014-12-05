package com.aaron.inmobiliaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Aarón on 01/12/2014.
 */
public class AdaptadorInmueble extends ArrayAdapter<Vivienda> {
    private Context contexto;
    private ArrayList<Vivienda> viviendas;
    private int recurso;
    private LayoutInflater i;

    public AdaptadorInmueble(Context context, int resource, ArrayList<Vivienda> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.viviendas=objects;
        this.recurso=resource;
        this.i= (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder{
        public TextView tvL,tvP,tvI,tvD;
        public ImageView ivM;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        //El if entra cuando se crea el ViewHolder por primera vez
        if(convertView == null){
            convertView= i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvL = (TextView)convertView.findViewById(R.id.tvLocalidad);
            vh.tvD = (TextView)convertView.findViewById(R.id.tvCalle);
            vh.tvI = (TextView)convertView.findViewById(R.id.tvID);
            vh.tvP = (TextView)convertView.findViewById(R.id.tvPrecio);
            vh.ivM = (ImageView)convertView.findViewById(R.id.ivIcono);
            convertView.setTag(vh);
        }
        else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.tvL.setText(viviendas.get(position).getLocalidad());
        vh.tvI.setText(viviendas.get(position).getId());
        vh.tvD.setText(viviendas.get(position).getDireccion());
        vh.tvP.setText(viviendas.get(position).getPrecio()+" "+this.getContext().getString(R.string.moneda));
        vh.ivM.setImageResource(viviendas.get(position).getTipo());
        vh.posicion=position;
        return convertView;
    }

}