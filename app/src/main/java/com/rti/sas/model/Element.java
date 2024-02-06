package com.rti.sas.model;

import com.google.gson.annotations.SerializedName;

public class Element {

    @SerializedName("distance") private Distance distance;
    @SerializedName("duration") private Duration duration;
    @SerializedName("status") private String status;

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }
}
