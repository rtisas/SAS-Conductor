package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;

public class Localization {

    @SerializedName("id") private String id;
    @SerializedName("nombre") private String nombre;
    @SerializedName("direccion") private String direccion;
    @SerializedName("latitud") private String latitud;
    @SerializedName("longitud") private String longitud;
    @SerializedName("ciudad") private String ciudad;

    public Localization(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {return longitud; }

    public void setLongitud(String longitud) {this.longitud = longitud; }

    public String getCiudad() {return ciudad; }

    public void setCiudad(String ciudad) {this.ciudad = ciudad; }
}
