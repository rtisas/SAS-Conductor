package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.gson.Gson;
import com.rtisas.sas.broadcast.ConnectivityReceiver;
import com.rtisas.sas.model.EventService;
import com.rtisas.sas.model.Request;
import com.rtisas.sas.model.Service;
import com.rtisas.sas.model.User;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SearchView extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String PREF = "preferences";
    private AlertDialog.Builder alertDialog;
    public static Runnable runnable;
    public static Handler handler;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private ConnectivityReceiver connectivityReceiver;
    private ConsumeService consumeService;
    private Request request;
    private TextView cancel,triger;
    private Service service;
    private User user;
    private Gson gson;
    private CircleProgressView progress;
    private Boolean stateQuery;
    private String state;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        preferences = this.getSharedPreferences(PREF, this.MODE_PRIVATE);
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityReceiverListener(this);
        consumeService = new ConsumeService();
        editor = preferences.edit();
        service = new Service();
        user = new User();
        gson = new Gson();
        service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        progress = (CircleProgressView)findViewById(R.id.progress);
        cancel = (TextView)findViewById(R.id.cancel);
        triger = (TextView)findViewById(R.id.triger);

        progress.setTextEnabled(false);
        progress.setInterpolator(new AccelerateDecelerateInterpolator());
        progress.setStartAngle(270);
        progress.setProgressWithAnimation(100, 240000);

        count = getIntent().getIntExtra("timer",0);

        if(count == 0){
            Calendar calendario = new GregorianCalendar();
            editor.putLong("timer",calendario.getTimeInMillis()).commit();
        }

        Loading();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelClient();
            }
        });

    }

    @Override
    public void onBackPressed() {
        CancelClient();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //finish();
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
        handler.removeCallbacks(runnable);
        //finish();
    }

    @Override
    public void onNetworkConnection(boolean isConnected) {

        if (!isConnected) {
            triger.setVisibility(View.VISIBLE);
            AlertMessage(getResources().getString(R.string.internetES),false);
        } else {
            triger.setVisibility(View.GONE);
            Query();
            AlertMessage(getResources().getString(R.string.internetES),false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventService event) {
        if(event.getAction().equals("state")){
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                State(event.getResponse().getServicio());
            }
        }
    }

    private void Loading() {

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (count++ < 8){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.search), Toast.LENGTH_LONG).show();
                    handler.postDelayed(this, 30000);
                }else {
                    AlertMessage(getResources().getString(R.string.finSearch),true);
                    Cancel();
                    Save();
                }
            }
        };
        handler.post(runnable);
    }

    private void State(Service serv) {

        switch (serv.getEstado()){
            case "CANCELADO":
                Cancel();
                break;
            case "ACEPTADO":
                service = serv;
                state = "arrived";
                Save();
                startActivity(new Intent(getApplicationContext(),MapView.class));
                handler.removeCallbacks(runnable);
                finish();
                break;
            case "EN_TRAYECTO":
                service = serv;
                state = "finish";
                startActivity(new Intent(getApplicationContext(),MapView.class));
                handler.removeCallbacks(runnable);
                finish();
                Save();
                break;
        }

    }

    private void Cancel() {

        handler.removeCallbacks(runnable);
        request = new Request();
        request.setUser(user);
        request.setDescription(getResources().getString(R.string.finSearch));
        request.setReason(getResources().getString(R.string.hightime));
        consumeService.ConsumerService("cancel",request);

    }

    private void CancelClient (){
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(getResources().getString(R.string.cancelS));
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state = "start";
                Save();
                Cancel();
                finish();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }

    private void AlertMessage(String message,final boolean finish) {

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

    private void Save() {

        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User",u);
        String s = gson.toJson(service);
        editor.putString("Service",s);
        editor.putString("state",state);
        editor.commit();

    }

    private void Query (){
        stateQuery = true;
        if(stateQuery){
            editor.putBoolean("query",false);
            editor.commit();
            request = new Request();
            if(service.getId()==null){
                request.setUser(user);
            }else{
                request.setService(service);
            }
            consumeService.ConsumerService("state", request);
        }
    }

    public static void End (){
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
    }


}
