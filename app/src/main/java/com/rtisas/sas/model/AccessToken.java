package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("access_token") private String access_token;
    @SerializedName("token_type") private String token_type;
    @SerializedName("refresh_token") private String refresh_token;
    @SerializedName("expires_in") private int expires_in;
    @SerializedName("scope") private String scope;


    public String getAccess_token() {
        return access_token;
    }

}
