package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("id") private int id;
    @SerializedName("tipo") private int tipo;
    @SerializedName("numero") private String numero;
    @SerializedName("descTipo") private String descTipo;
    @SerializedName("vigencia") private String vigencia;
    @SerializedName("nombreTarjeta") private String nombreTarjeta;
    @SerializedName("codigoSeguridad") private String codigoSeguridad;

    public Card() {

    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getDescTipo() {
        return descTipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

}
