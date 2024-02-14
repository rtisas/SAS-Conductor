package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rtisas.sas.model.EventService;
import com.rtisas.sas.model.User;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PayView extends AppCompatActivity implements View.OnClickListener {

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private TextView credit,business,digital,cash;
    private User user;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_view);

        preferences = this.getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        gson = new Gson();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        business = (TextView)findViewById(R.id.business);
        digital = (TextView)findViewById(R.id.digital);
        credit = (TextView)findViewById(R.id.credit);
        cash = (TextView)findViewById(R.id.cash);

        business.setOnClickListener(this);
        digital.setOnClickListener(this);
        credit.setOnClickListener(this);
        cash.setOnClickListener(this);

        Progress();
        consumeService.ConsumerService("updatepay",user.getEmail());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.business:
                AlertMessage(getResources().getString(R.string.balance)+" $ "+user.getEnterpriseCredits(),false);
                break;
            case R.id.digital:
                AlertMessage(getResources().getString(R.string.balance)+" $ "+user.getCredits(),false);
                break;
            case R.id.credit:
                intent = new Intent(this.getApplication(),HistoryView.class);
                intent.putExtra("email",user.getEmail());
                intent.putExtra("view","getCards");
                startActivity(intent);
                break;
            case R.id.cash:
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event (final EventService event) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(event.getAction().equals("updatepay")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                user = event.getResponse().getUser();
                Save();
            } else {
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
                    startActivity(new Intent(getApplicationContext(),SplashView.class));
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
        editor.commit();

    }

}
