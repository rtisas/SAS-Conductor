package com.rti.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rti.sas.model.EventService;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RecoverView extends AppCompatActivity implements View.OnClickListener{

    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private TextView getcode;
    private EditText email;
    private String setEmail;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_view);

        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);

        getcode = (TextView)findViewById(R.id.getcode);
        email = (EditText)findViewById(R.id.email);

        getcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.getcode:
                if(Validate()){
                    Getcode();
                }
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
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),true);
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else{
            AlertMessage (event.getResult(),false);
        }
    }

    private void Getcode () {

        Progress();
        consumeService.ConsumerService("getcode",setEmail);

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

    private boolean Validate() {

        boolean emailV = Email(email.getText().toString());
        return emailV;

    }

    private boolean Email(String text) {

        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            email.setError(getString(R.string.emailE));
            return false;
        } else {
            setEmail = text;
            email.setError(null);
            return true;
        }
    }
}
