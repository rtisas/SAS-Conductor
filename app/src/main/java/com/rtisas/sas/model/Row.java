package com.rtisas.sas.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Row {

    @SerializedName("elements") private ArrayList<Element> elements = new ArrayList<Element>();

    public ArrayList<Element> getElements() {
        return elements;
    }
}
