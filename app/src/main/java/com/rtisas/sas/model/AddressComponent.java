package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class AddressComponent {

    @SerializedName("long_name") private String longName;
    @SerializedName("short_name") private String shortName;
    @SerializedName("types") private ArrayList<String> types;

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
}
