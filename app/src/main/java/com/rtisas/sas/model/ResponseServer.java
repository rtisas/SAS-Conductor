package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ResponseServer {

    //google
    @SerializedName("results") private ArrayList<Result> results = new ArrayList<Result>();
    @SerializedName("rows") private ArrayList<Row> rows = new ArrayList<Row>();
    @SerializedName("status") private String status;

    //server
    @SerializedName("functionalMessage") private FunctionalMessage functionalMessage;
    @SerializedName("user") private User user;
    @SerializedName("vehicle") private Vehicle vehicle;
    @SerializedName("cards") private ArrayList<Card> cards;
    @SerializedName("ubicacion") private Ubication ubication;
    @SerializedName("typeVehicle") private Vehicle typeVehicle;
    @SerializedName("servicio") private Service servicio;
    @SerializedName("pqrsList") private ArrayList<Pqrs> pqrsList;
    @SerializedName("vehicles") private ArrayList<Vehicle> vehicles;
    @SerializedName("servicios") private ArrayList<Service> servicios;
    @SerializedName("detalleTarifa") private DetalleTarifa detalleTarifa;
    @SerializedName("favoriteLocations") private ArrayList<Localization> localizations;
    @SerializedName("parameter") private Parameter parameter;

    //otros
    @SerializedName("nombresAfes") private ArrayList<String> nombresAfes;
    @SerializedName("codigo") private String code;
    @SerializedName("valor") private float valor;
    @SerializedName("id") private int id;

    public ArrayList<Result> getResults() {
        return results;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public String getStatus() {
        return status;
    }

    public FunctionalMessage getFunctionalMessage() {
        return functionalMessage;
    }

    public User getUser() {
        return user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Service getServicio() {
        return servicio;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Ubication getUbication() {
        return ubication;
    }

    public Vehicle getTypeVehicle() {
        return typeVehicle;
    }

    public ArrayList<Pqrs> getPqrsList() {
        return pqrsList;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ArrayList<Service> getServicios() {
        return servicios;
    }

    public DetalleTarifa getDetalleTarifa() {
        return detalleTarifa;
    }

    public ArrayList<Localization> getLocalizations() {
        return localizations;
    }

    public ArrayList<String> getNombresAfes() {
        return nombresAfes;
    }

    public String getCode() {
        return code;
    }

    public float getValor() {
        return valor;
    }

    public int getId() {
        return id;
    }

    public Parameter getParameter() {
        return parameter;
    }
}
