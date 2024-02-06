package com.rti.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rti.sas.model.EventService;
import com.rti.sas.model.User;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UserView extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private TextView name,email,mobile,editpassword;
    private User user;
    private Gson gson;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        preferences = this.getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        gson = new Gson();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        editpassword = (TextView)findViewById(R.id.editpassword);
        mobile = (TextView)findViewById(R.id.mobile);
        email = (TextView)findViewById(R.id.email);
        name = (TextView)findViewById(R.id.name);

        editpassword.setOnClickListener(this);
        name.setText(user.getFirstName()+" "+user.getLastName());
        email.setText(user.getEmail());
        mobile.setText(user.getPhone());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editpassword:
                Progress ();
                consumeService.ConsumerService("getcode",user.getEmail());
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

        if (event.getAction().equals("getcode")){
            String code = event.getResponse().getFunctionalMessage().getCode();
            if(code.equals("F-0000")){
                id=event.getResponse().getId();
                AlertMessage(getResources().getString(R.string.codepassw)+" "+user.getEmail(),true);
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else{
            AlertMessage (event.getResult(),false);
        }
    }

    private void Progress (){

        if(progress.isShowing()){
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.recover));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    private void AlertMessage (String message, final boolean finish) {

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
                    Intent intent = new Intent(getApplicationContext(),NewPasswordView.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

}
