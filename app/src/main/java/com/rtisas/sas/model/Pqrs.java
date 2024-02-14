package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;

public class Pqrs {

    @SerializedName("id") private String id;
    @SerializedName("tipoSolicitud") private String tipoSolicitud;
    @SerializedName("motivo") private String motivo;
    @SerializedName("descripcionSolicitud") private String descripcionSolicitud;
    @SerializedName("respuestaSolicitud") private String respuestaSolicitud;
    @SerializedName("usuarioRespuesta") private String usuarioRespuesta;
    @SerializedName("fechaSolicitud") private String fechaSolicitud;
    @SerializedName("fechaRespuesta") private String fechaRespuesta;
    @SerializedName("user") private User user;
    @SerializedName("servicio") private Service service;

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setDescripcionSolicitud(String descripcionSolicitud) {
        this.descripcionSolicitud = descripcionSolicitud;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDescripcionSolicitud() {
        return descripcionSolicitud;
    }

    public String getRespuestaSolicitud() {
        return respuestaSolicitud;
    }

    public String getUsuarioRespuesta() {
        return usuarioRespuesta;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

}