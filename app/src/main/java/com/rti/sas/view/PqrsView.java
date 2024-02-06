package com.rti.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rti.sas.adapter.PqrsA;
import com.rti.sas.model.EventService;
import com.rti.sas.model.Pqrs;
import com.rti.sas.model.Request;
import com.rti.sas.model.Service;
import com.rti.sas.model.User;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PqrsView extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private Request request;
    private Spinner type,reason;
    private ListView listpqr;
    private TextView send,listbutton;
    private EditText men;
    private PqrsA pqrsA;
    private Pqrs pqrs;
    private User user;
    private Gson gson;
    private String typeSelect,reasonSelect,idservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pqrs_view);

        preferences = this.getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        pqrs = new Pqrs();
        user = new User();
        gson = new Gson();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);
        Intent i = getIntent();
        idservice = i.getStringExtra("idservice");

        listbutton = (TextView)findViewById(R.id.listbutton);
        listpqr = (ListView)findViewById(R.id.listpqr);
        reason = (Spinner)findViewById(R.id.reason);
        send = (TextView)findViewById(R.id.send);
        type = (Spinner)findViewById(R.id.type);
        men = (EditText)findViewById(R.id.men);

        if(idservice==null){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typepqrf, android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(adapter);
        }

        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonSelect = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelect = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listbutton.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                Send();
                break;
            case R.id.listbutton:
                Progress();
                consumeService.ConsumerService("listpqrs",user.getEmail());
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if(listpqr.getVisibility()==View.VISIBLE){
            listpqr.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventService event) {

        if(event.getAction().equals("pqrsSave")){
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), true);
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }else if(event.getAction().equals("listpqrs")){
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                if(event.getResponse().getPqrsList().size()==0){
                    AlertMessage(getResources().getString(R.string.pqrsE),false);
                }
                if(progress.isShowing()){
                    progress.dismiss();
                }
                pqrsA = new PqrsA(this,event.getResponse().getPqrsList());
                listpqr.setAdapter(pqrsA);
                listpqr.setVisibility(View.VISIBLE);
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }
    }

    private void Progress() {

        if(progress.isShowing()){
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.app_name));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    private void AlertMessage(String message,final boolean finish) {

        if(progress.isShowing()){
            progress.dismiss();
        }
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(this.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if(finish){
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

    private boolean Validator() {

        if(men.getText().length()<=1||reasonSelect.equals("Razon")||typeSelect.equals("Tipo")){
            AlertMessage(this.getString(R.string.fieldsE),false);
            return false;
        }else{
            return true;
        }

    }

    private void Send() {

        if(Validator()){
            pqrs.setDescripcionSolicitud(men.getText().toString());
            pqrs.setTipoSolicitud(typeSelect);
            pqrs.setMotivo(reasonSelect);
            request = new Request();
            if(typeSelect.equals("QUEJA")){
                Service service = new Service();
                service.setId(idservice);
                pqrs.setService(service);
            }else{
                pqrs.setUser(user);
            }
            request.setPqrs(pqrs);
            Progress();
            consumeService.ConsumerService("pqrsSave",request);
        }

    }

}
