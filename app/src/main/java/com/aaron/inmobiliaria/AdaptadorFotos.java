package com.aaron.inmobiliaria;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Aaron on 04/12/2014.
 */
public class AdaptadorFotos extends ArrayAdapter<File> {
    private Context contexto;
    private ArrayList<File> fotos;
    private int recurso;
    private LayoutInflater i;

    public AdaptadorFotos(Context context, int resource, ArrayList<File> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.fotos=objects;
        this.recurso=resource;
        this.i= (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder{
        public TextView tvIdFoto;
        public ImageView ivFoto;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        //El if entra cuando se crea el ViewHolder por primera vez
        if(convertView == null){
            convertView= i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvIdFoto = (TextView)convertView.findViewById(R.id.tvFoto);
            vh.ivFoto = (ImageView)convertView.findViewById(R.id.ivFotos);
            convertView.setTag(vh);
        }
        else{
            vh=(ViewHolder)convertView.getTag();
        }
        if(fotos.get(position).equals(R.drawable.nofoto+"")){
            vh.tvIdFoto.setText(R.string.nofoto);
            vh.ivFoto.setImageResource(R.drawable.nofoto);
        }
        else {
            vh.tvIdFoto.setText(fotos.get(position).getName());
            Bitmap myBitmap = BitmapFactory.decodeFile(fotos.get(position).getPath());
            vh.ivFoto.setImageBitmap(myBitmap);
        }
        vh.posicion = position;
        return convertView;
    }

}

