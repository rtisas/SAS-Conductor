package com.rtisas.sas.model;

public class EventService {

    private final ResponseServer response;
    private final String result;
    private final String action;

    public EventService (String action, String result){

        this.response = null;
        this.result = result;
        this.action = action;

    }

    public EventService (ResponseServer response, String action, String result){

        this.response = response;
        this.result = result;
        this.action = action;

    }

    public ResponseServer getResponse() {
        return response;
    }

    public String getResult() {
        return result;
    }

    public String getAction() {
        return action;
    }

}
