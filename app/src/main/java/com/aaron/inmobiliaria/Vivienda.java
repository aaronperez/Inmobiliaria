package com.aaron.inmobiliaria;


/**
 * Created by Aaron on 01/12/2014.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aaron Perez on 20/11/2014.
 */

public class Vivienda implements Parcelable,Comparable<Vivienda> {
    private String id,localidad, direccion;
    private float  precio;
    private int tipo;


    public Vivienda() {
    }

    public Vivienda(String id, String localidad, String direccion, float precio, int tipo) {
        this.id = id;
        this.localidad = localidad;
        this.direccion = direccion;
        this.precio = precio;
        this.tipo = tipo;
    }

    public Vivienda(Parcel parcel){
        this.id = parcel.readString();
        this.localidad = parcel.readString();
        this.direccion = parcel.readString();
        this.precio = parcel.readFloat();
        this.tipo = parcel.readInt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vivienda)) return false;

        Vivienda that = (Vivienda) o;

        if (id != that.id) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        return result;
    }

    @Override
    public int compareTo(Vivienda viviendas) {
        return this.getId().toLowerCase().compareTo(viviendas.getId().toLowerCase());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(localidad);
        dest.writeString(direccion);
        dest.writeFloat(precio);
        dest.writeInt(tipo);
    }

    public static final Creator<Vivienda> CREATOR =
            new Creator<Vivienda>() {
                public Vivienda createFromParcel(Parcel parcel) {
                    return new Vivienda(parcel);
                }

                public Vivienda[] newArray(int size) {
                    return new Vivienda[size];
                }
            };

}