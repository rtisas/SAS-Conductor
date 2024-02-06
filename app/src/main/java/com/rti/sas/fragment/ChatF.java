package com.rti.sas.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rti.sas.adapter.ChatA;
import com.rti.sas.model.Chat;
import com.rti.sas.model.Chats;
import com.rti.sas.model.Request;
import com.rti.sas.model.Service;
import com.rti.sas.model.User;
import com.rti.sas.R;
import com.rti.sas.service.ConsumeService;

import java.text.DecimalFormat;
import java.util.Calendar;
import static android.content.Context.MODE_PRIVATE;

public class ChatF extends Fragment implements View.OnClickListener {

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private ConsumeService consumeService;
    private Request request;
    private TextView send,men1,men2,men3;
    private ListView listchat;
    private EditText men;
    private Chats chats;
    private ChatA chata;
    private Chat chat;
    private Service service;
    private User user;
    private Gson gson;


    public ChatF() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getActivity().getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        String c = preferences.getString("Chat","chat");
        consumeService = new ConsumeService();
        editor = preferences.edit();
        service = new Service();
        chats = new Chats();
        chat = new Chat();
        gson = new Gson();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);
        if(!preferences.getString("Service", "service").equals("service")){
            service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
        }


        if(!c.equals("chat")){
            chats = gson.fromJson(c,Chats.class);
            chata = new ChatA(getActivity().getApplicationContext(),chats);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        listchat = (ListView)view.findViewById(R.id.listchat);
        send = (TextView)view.findViewById(R.id.send);
        men1 = (TextView)view.findViewById(R.id.men1);
        men2 = (TextView)view.findViewById(R.id.men2);
        men3 = (TextView)view.findViewById(R.id.men3);
        men = (EditText)view.findViewById(R.id.men);

        if(chata!=null){
            listchat.setAdapter(chata);
            listchat.setSelection(chata.getCount());
        }
        send.setOnClickListener(this);
        men1.setOnClickListener(this);
        men2.setOnClickListener(this);
        men3.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.send:
                if(!men.getText().toString().equals("")){
                    Send();
                }
                break;
            case R.id.men1:
                men.setText(men1.getText());
                break;
            case R.id.men2:
                men.setText(men2.getText());
                break;
            case R.id.men3:
                men.setText(men3.getText());
                break;
        }
    }

    private void Send() {

        request = new Request();
        request.setUser(user);
        request.setSendTo("CONDUCTOR");
        request.setTokenReceptor(service.getRegistrationIdConductor());
        request.setTokenEmisor(service.getRegistrationIdPasajero());
        request.setCanalReceptor(service.getCanalConductor());
        request.setMensaje(men.getText().toString());
        consumeService.ConsumerService("sendmessage",request);

        String c = preferences.getString("Chat","chat");
        chats = new Chats();
        chat = new Chat();
        if(!c.equals("chat")){
            chats = gson.fromJson(c,Chats.class);
        }

        chat.setPropio(true);
        chat.setMensaje(men.getText().toString());
        chat.setFecha(Date());
        chats.addChat(chat);
        chata = new ChatA(getActivity().getApplicationContext(),chats);
        listchat.setAdapter(chata);
        listchat.setSelection(chata.getCount());
        men.setText("");

    }

    private String Date() {
        Calendar calendar = Calendar.getInstance();
        /*int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);*/
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        DecimalFormat mFormat = new DecimalFormat("00");
        String fechaActual = String.valueOf(mFormat.format(Double.valueOf(hora)) + ":" + mFormat.format(Double.valueOf(min)));
        return fechaActual;
    }

    public void save (){
        String c = gson.toJson(chats);
        editor.putString("Chat",c);
        editor.commit();
    }

}