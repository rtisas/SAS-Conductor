package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("canal") private final String canal = "ANDROID";
    @SerializedName("id") private String id;
    @SerializedName("estado") private String estado;
    @SerializedName("tipoPago") private String tipoPago;
    @SerializedName("latitudInicial") private String latitudInicial;
    @SerializedName("longitudInicial") private String longitudInicial;
    @SerializedName("latitudFinal") private String latitudFinal;
    @SerializedName("longitudFinal") private String longitudFinal;
    @SerializedName("descOrigen") private String descOrigen;
    @SerializedName("descDestino") private String descDestino;
    @SerializedName("nombreConductor") private String nombreConductor;
    @SerializedName("apellidoConductor") private String apellidoConductor;
    @SerializedName("calificacionConductor") private String calificacionConductor;
    @SerializedName("telefonoConductor") private String telefonoConductor;
    @SerializedName("calificacionPasajero") private String calificacionPasajero;
    @SerializedName("telefonoPasajero") private String telefonoPasajero;
    @SerializedName("modeloVehiculo") private String modeloVehiculo;
    @SerializedName("nombreAfe") private String nombreAfe;
    @SerializedName("vehicle") private String vehicle;
    @SerializedName("idTipoVehiculo") private String idTipoVehiculo;
    @SerializedName("personaEsperada") private String personaEsperada;
    //@SerializedName("ciudadorigen") private String ciudadorigen;
    @SerializedName("ciudadOrigen") private String ciudadorigen;
    @SerializedName("ciudad") private String ciudad;
    @SerializedName("tipoObjeto") private String tipoObjeto;
    @SerializedName("tarjeta") private String tarjeta;
    @SerializedName("observaciones") private String observaciones;
    @SerializedName("descripcionTrayecto") private String descripcionTrayecto;
    @SerializedName("detalleTarifa") private DetalleTarifa detalleTarifa;
    @SerializedName("fechaFinal") private String fechaFinal;
    @SerializedName("fechaInicial") private String fechaInicial;
    @SerializedName("fechaServicio") private String fechaServicio;
    @SerializedName("emailPasajero") private String emailPasajero;
    @SerializedName("emailConductor") private String emailConductor;
    @SerializedName("registrationIdPasajero") private String registrationIdPasajero;
    @SerializedName("canalConductor") private String canalConductor;
    @SerializedName("registrationIdConductor") private String registrationIdConductor;
    @SerializedName("iconVehiculo") private String iconVehiculo;
    @SerializedName("placa") private String placa;
    @SerializedName("fotoConductor") private String fotoConductor;
    @SerializedName("distancia") private Double distancia;
    @SerializedName("tiempoEstimado") private long tiempoEstimado;
    @SerializedName("tiempoRecogida") private long tiempoRecogida;
    @SerializedName("horaRecogida") private int horaRecogida;
    @SerializedName("diasExtension") private long diasExtension;
    @SerializedName("recarga") private boolean recarga;
    @SerializedName("idayvuelta") private boolean idayvuelta;
    @SerializedName("reservaDia") private boolean reservaDia;
    //@SerializedName("aeropuertoorigen") private boolean aeropuertoorigen;
    @SerializedName("aeropuertoOrigen") private boolean aeropuertoorigen;
    //@SerializedName("aeropuertodestino") private boolean aeropuertodestino;
    @SerializedName("aeropuertoDestino") private boolean aeropuertodestino;
    //@SerializedName("ciudaddestino") private String ciudaddestino;
    @SerializedName("ciudadDestino") private String ciudaddestino;
    //@SerializedName("idapozo") private boolean idapozo;
    @SerializedName("pozo") private boolean idapozo;
    @SerializedName("aeropuerto") private boolean aeropuerto;
    @SerializedName("fueraCiudad") private boolean fueraCiudad;
    @SerializedName("tipoSeguro") private boolean tipoSeguro;
    @SerializedName("tipoServicio") private int tipoServicio;
    @SerializedName("idTipoServicio") private int idTipoServicio;
    @SerializedName("valor") private double valor;
    @SerializedName("cedula") private String  cedula;
    @SerializedName("appVersionPassanger") private String appVersionPassanger;
    @SerializedName("appVersionDriver") private String  appVersionDriver;

    public Service () {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getLatitudInicial() {
        return latitudInicial;
    }

    public void setLatitudInicial(String latitudInicial) {
        this.latitudInicial = latitudInicial;
    }

    public String getLongitudInicial() {
        return longitudInicial;
    }

    public void setLongitudInicial(String longitudInicial) {
        this.longitudInicial = longitudInicial;
    }

    public String getLatitudFinal() {
        return latitudFinal;
    }

    public void setLatitudFinal(String latitudFinal) {
        this.latitudFinal = latitudFinal;
    }

    public String getLongitudFinal() {
        return longitudFinal;
    }

    public void setLongitudFinal(String longitudFinal) {
        this.longitudFinal = longitudFinal;
    }

    public String getDescOrigen() {
        return descOrigen;
    }

    public void setDescOrigen(String descOrigen) {
        this.descOrigen = descOrigen;
    }

    public String getDescDestino() {
        return descDestino;
    }

    public void setDescDestino(String descDestino) {
        this.descDestino = descDestino;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getApellidoConductor() {
        return apellidoConductor;
    }

    public void setApellidoConductor(String apellidoConductor) {
        this.apellidoConductor = apellidoConductor;
    }

    public String getCalificacionConductor() {
        return calificacionConductor;
    }

    public void setCalificacionConductor(String calificacionConductor) {
        this.calificacionConductor = calificacionConductor;
    }

    public String getCalificacionPasajero() {
        return calificacionPasajero;
    }

    public String getTelefonoConductor() {
        return telefonoConductor;
    }

    public void setTelefonoConductor(String telefonoConductor) {
        this.telefonoConductor = telefonoConductor;
    }

    public String getTelefonoPasajero() {
        return telefonoPasajero;
    }

    public void setTelefonoPasajero(String telefonoPasajero) {
        this.telefonoPasajero = telefonoPasajero;
    }

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setNombreAfe(String nombreAfe) {
        this.nombreAfe = nombreAfe;
    }

    public void setCalificacionPasajero(String calificacionPasajero) {
        this.calificacionPasajero = calificacionPasajero;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getidTipoVehiculo() {
        return idTipoVehiculo;
    }

    public void setidTipoVehiculo(String idTipoVehiculo) {
        this.idTipoVehiculo = idTipoVehiculo;
    }

    public String getPersonaEsperada() {
        return personaEsperada;
    }

    public void setPersonaEsperada(String personaEsperada) {
        this.personaEsperada = personaEsperada;
    }

    public String getCiudadOrigen() {
        return ciudadorigen;
    }

    public void setCiudadOrigen(String ciudadorigen) {
        this.ciudadorigen = ciudadorigen;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDescripcionTrayecto() {
        return descripcionTrayecto;
    }

    public void setDescripcionTrayecto(String descripcionTrayecto) {
        this.descripcionTrayecto = descripcionTrayecto;
    }

    public DetalleTarifa getDetalleTarifa() {
        return detalleTarifa;
    }

    public void setDetalleTarifa(DetalleTarifa detalleTarifa) {
        this.detalleTarifa = detalleTarifa;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(String fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getEmailPasajero() {
        return emailPasajero;
    }

    public void setEmailPasajero(String emailPasajero) {
        this.emailPasajero = emailPasajero;
    }

    public String getEmailConductor() {
        return emailConductor;
    }

    public void setEmailConductor(String emailConductor) {
        this.emailConductor = emailConductor;
    }

    public String getRegistrationIdPasajero() {
        return registrationIdPasajero;
    }

    public void setRegistrationIdPasajero(String registrationIdPasajero) {
        this.registrationIdPasajero = registrationIdPasajero;
    }

    public String getCanal() {
        return canal;
    }

    public String getCanalConductor() {
        return canalConductor;
    }

    public void setCanalConductor(String canalConductor) {
        this.canalConductor = canalConductor;
    }

    public String getRegistrationIdConductor() {
        return registrationIdConductor;
    }

    public void setRegistrationIdConductor(String registrationIdConductor) {
        this.registrationIdConductor = registrationIdConductor;
    }

    public String getIconVehiculo() {
        return iconVehiculo;
    }

    public void setIconVehiculo(String iconVehiculo) {
        this.iconVehiculo = iconVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFotoConductor() {
        return fotoConductor;
    }

    public void setFotoConductor(String fotoConductor) {
        this.fotoConductor = fotoConductor;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public long getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(long tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public long getTiempoRecogida() {
        return tiempoRecogida;
    }

    public void setTiempoRecogida(long tiempoRecogida) {
        this.tiempoRecogida = tiempoRecogida;
    }

    public int getHoraRecogida() {
        return horaRecogida;
    }

    public void setHoraRecogida(int horaRecogida) {
        this.horaRecogida = horaRecogida;
    }

    public long getDiasExtension() {
        return diasExtension;
    }

    public void setDiasExtension(long diasExtension) {
        this.diasExtension = diasExtension;
    }

    public boolean isRecarga() {
        return recarga;
    }

    public void setRecarga(boolean recarga) {
        this.recarga = recarga;
    }

    public boolean isIdayvuelta() {
        return idayvuelta;
    }

    public void setIdayvuelta(boolean idayvuelta) {
        this.idayvuelta = idayvuelta;
    }

    public boolean isReservaDia() {
        return reservaDia;
    }

    public void setReservaDia(boolean reservaDia) {
        this.reservaDia = reservaDia;
    }

    public String getCiudadDestino() {
        return ciudaddestino;
    }

    public void setCiudadDestino(String ciudaddestino) {
        this.ciudaddestino = ciudaddestino;
    }

    public boolean isAeropuertoOrigen() {
        return aeropuertoorigen;
    }

    public void setAeropuertoOrigen(boolean aeropuertoorigen) {
        this.aeropuertoorigen = aeropuertoorigen;
    }

    public boolean isAeropuertoDestino() {
        return aeropuertodestino;
    }

    public void setAeropuertoDestino(boolean aeropuertodestino) {
        this.aeropuertodestino = aeropuertodestino;
    }

    public boolean idaPozo() {
        return idapozo;
    }

    public void setIdaPozo(boolean idapozo) {
        this.idapozo = idapozo;
    }
    public boolean isAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(boolean aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public boolean isFueraCiudad() {
        return fueraCiudad;
    }

    public void setFueraCiudad(boolean fueraCiudad) {
        this.fueraCiudad = fueraCiudad;
    }

    public boolean isTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(boolean tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public int getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(int tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getidTipoServicio() {
        return idTipoServicio;
    }

    public void setidTipoServicio(int idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getAppVersionPassanger() {
        return appVersionPassanger;
    }

    public void setAppVersionPassanger(String appVersionPassanger) {
        this.appVersionPassanger = appVersionPassanger;
    }

    public String getAppVersionDriver() {
        return appVersionDriver;
    }

    public void setAppVersionDriver(String appVersionDriver) {
        this.appVersionDriver = appVersionDriver;
    }
}
