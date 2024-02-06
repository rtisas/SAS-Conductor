package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("currentlocation") Localization localization;
    @SerializedName("tipoVehiculo") private Vehicle tipoVehiculo;
    @SerializedName("servicio") private Service service;
    @SerializedName("vehicle") private Vehicle vehicle;
    @SerializedName("pqrs") private Pqrs pqrs;
    @SerializedName("card") private Card card;
    @SerializedName("user") private User user;

    //otros
    @SerializedName("tokenReceptor") private String tokenReceptor;
    @SerializedName("canalReceptor") private String canalReceptor;
    @SerializedName("description") private String description;
    @SerializedName("tokenEmisor") private String tokenEmisor;
    @SerializedName("calificacion") private int calificacion;
    @SerializedName("comentario") private String comentario;
    @SerializedName("idServicio") private String servicio;
    @SerializedName("registro") private boolean registro;
    @SerializedName("service") private String idService;
    @SerializedName("mensaje") private String mensaje;
    @SerializedName("reason") private String reason;
    @SerializedName("sendTo") private String sendTo;
    @SerializedName("codigo") private String codigo;


    public Request(){

    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }

    public void setTipoVehiculo(Vehicle tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setPqrs(Pqrs pqrs) {
        this.pqrs = pqrs;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTokenReceptor(String tokenReceptor) {
        this.tokenReceptor = tokenReceptor;
    }

    public void setCanalReceptor(String canalReceptor) {
        this.canalReceptor = canalReceptor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTokenEmisor(String tokenEmisor) {
        this.tokenEmisor = tokenEmisor;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public void setRegistro(boolean registro) {
        this.registro = registro;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
