package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;

public class DetalleTarifa {

    @SerializedName("valorKilometro") private double valorKilometro;
    @SerializedName("valorMinuto") private double valorMinuto;
    @SerializedName("valorHora") private double valorHora;
    @SerializedName("valorDia") private double valorDia;
    @SerializedName("distancia") private double distancia;
    @SerializedName("horaRecogida") private int horaRecogida;
    @SerializedName("tiempoEstimado") private int tiempoEstimado;
    @SerializedName("idTipoVehiculo") private int idTipoVehiculo;
    @SerializedName("valorBase") private double valorBase;
    @SerializedName("valorArranque") private double valorArranque;
    @SerializedName("recargos") private Recargos recargos;


    public DetalleTarifa() {

    }

    public double getValorKilometro() {
        return valorKilometro;
    }

    public double getValorMinuto() {
        return valorMinuto;
    }

    public double getValorHora() {
        return valorHora;
    }

    public double getValorDia() {
        return valorDia;
    }

    public double getDistancia() {
        return distancia;
    }

    public int getHoraRecogida() {
        return horaRecogida;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public int getIdTipoVehiculo() {
        return idTipoVehiculo;
    }

    public double getValorBase() {
        return valorBase;
    }

    public double getValorArranque() {
        return valorArranque;
    }

    public Recargos getRecargos() {
        return recargos;
    }

}
