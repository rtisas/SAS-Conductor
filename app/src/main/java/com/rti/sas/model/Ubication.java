package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;

public class Ubication {

    @SerializedName("latitud") String latitud;
    @SerializedName("longitud") String longitud;

    public Ubication(){

    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

}
