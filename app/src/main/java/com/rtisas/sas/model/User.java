package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("canal") private final String canal = "ANDROID";
    @SerializedName("email") private String email;
    @SerializedName("password") private String password;
    @SerializedName("registration_id") private String registration_id;
    @SerializedName("firstName") private String firstName;
    @SerializedName("lastName") private String lastName;
    @SerializedName("phone") private String phone;
    @SerializedName("clientID") private String clientID;
    @SerializedName("clientSecret") private String clientSecret;
    @SerializedName("rol") private String rol;
    @SerializedName("credits") private int credits;
    @SerializedName("id") private int id;
    @SerializedName("idRol") private int idRol;
    @SerializedName("enterpriseCredits") private int enterpriseCredits;
    @SerializedName("enterpriseId") private int idEmpresa;
    @SerializedName("promCode") private String promCode;
    @SerializedName("localPhone") private String localPhone;
    @SerializedName("activo") private boolean activo;
    @SerializedName("restorationCode") private  String restorationCode;
    @SerializedName("tipopago") private String tipopago;
    @SerializedName("tipouser") private String tipouser;

    public User() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRestorationCode(String restorationCode) {
        this.restorationCode = restorationCode;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setEnterpriseCredits(int enterpriseCredits) {
        this.enterpriseCredits = enterpriseCredits;
    }

    public String getEmail() {
        return email;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRol() {
        return rol;
    }

    public int getCredits() {
        return credits;
    }

    public int getId() {
        return id;
    }

    public int getIdRol() {
        return idRol;
    }

    public int getEnterpriseCredits() {
        return enterpriseCredits;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getPromCode() {
        return promCode;
    }

    public String getLocalPhone() {
        return localPhone;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getTipopago() {
        return tipopago="inicio";
    }

    public String getTipouser() {
        return tipouser="taxi";
    }

}
