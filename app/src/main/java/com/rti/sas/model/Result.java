package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Result {

    @SerializedName("formatted_address") private String formattedAddress;
    @SerializedName("address_components") private ArrayList<AddressComponent> addressComponents;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public ArrayList<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

}
