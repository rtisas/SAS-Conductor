package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;

public class Duration {

    @SerializedName("text") private String text;
    @SerializedName("value") private int value;

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

}
