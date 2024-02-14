package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;

public class FunctionalMessage {

    @SerializedName("code") private String code;
    @SerializedName("message") private String message;
    @SerializedName("causeMessage") private String causemessage;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
