package com.rti.sas.model;

import java.util.ArrayList;

public class Chats {

    private ArrayList<Chat> chats;

    public Chats (){
        chats = new ArrayList<Chat>();
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void addChat (Chat chat){
        this.chats.add(chat);
    }

}
