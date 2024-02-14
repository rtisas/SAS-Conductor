package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Vehicle {

    @SerializedName("id") String id;
    @SerializedName("type") String type;
    @SerializedName("countPerson") int countPerson;
    @SerializedName("url_img_out") String url_img_out;
    @SerializedName("url_img_over") String url_img_over;
    @SerializedName("calificacion") String qualification;
    @SerializedName("modelos") ArrayList<String> modelos;
    @SerializedName("idTipoServicio") long idTipoServicio;

    public Vehicle() {

    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getCountPerson() {
        return countPerson;
    }

    public String getUrl_img_out() {
        return url_img_out;
    }

    public String getUrl_img_over() {
        return url_img_over;
    }

    public String getQualification() {
        return qualification;
    }

    public ArrayList<String> getModelos() {
        return modelos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setModelos(ArrayList<String> modelos) {
        this.modelos = modelos;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setIdTipoServicio(long idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

}