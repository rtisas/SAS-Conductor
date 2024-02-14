package com.rtisas.sas.model;


import com.google.gson.annotations.SerializedName;

public class Parameter {
    @SerializedName("valor") private String valor;
    public Parameter() {

    }

    public String getValor() {
        return valor;
    }
}
