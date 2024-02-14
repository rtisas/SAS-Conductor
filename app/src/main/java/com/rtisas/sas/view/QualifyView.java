package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rtisas.sas.model.EventService;
import com.rtisas.sas.model.Request;
import com.rtisas.sas.model.Service;
import com.rtisas.sas.model.User;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;
import com.squareup.picasso.Picasso;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

public class QualifyView extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private Request request;
    private TextView name,source,destination,qualifybutton;
    private RatingBar qualification;
    private CircleImageView photo;
    private EditText comment;
    private Service service;
    private User user;
    private Gson gson;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qualify_view);

        preferences = this.getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        user = new User();
        gson = new Gson();
        state = "start";
        service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        qualification = (RatingBar)findViewById(R.id.qualification);
        qualifybutton = (TextView)findViewById(R.id.qualifybutton);
        destination = (TextView)findViewById(R.id.destination);
        photo = (CircleImageView)findViewById(R.id.photo);
        comment = (EditText)findViewById(R.id.comment);
        source = (TextView)findViewById(R.id.source);
        name = (TextView)findViewById(R.id.name);

        String foto = service.getFotoConductor();
        if (foto != null) {
            //Picasso.with(getApplicationContext()).load(foto).into(photo);}
            Picasso.get().load(foto).into(photo);}
        name.setText(service.getNombreConductor()+" "+service.getApellidoConductor());
        destination.setText(service.getDescDestino());
        source.setText(service.getDescOrigen());
        qualifybutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qualifybutton:
                if(Validator()){
                    Progress();
                    request = new Request();
                    request.setCalificacion((int)qualification.getRating());
                    request.setServicio(service.getId());
                    request.setComentario(comment.getText().toString());
                    consumeService.ConsumerService("qualify",request);
                }else{
                    AlertMessage(getResources().getString(R.string.qualifyE),false);
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
    public void finish() {
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventService event) {

        if(event.getAction().equals("qualify")){
            Restart();
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                if(progress.isShowing()){
                    progress.dismiss();
                }
                startActivity(new Intent(getApplicationContext(),SplashView.class));
                finish();
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),true);
            }
        }else{
            AlertMessage(event.getResult(),false);
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

    private boolean Validator() {
        // Si la calificación es menor o igual a 3 y no hay comentario (la longitud del comentario es cero)
        // retorna falso
//        if(qualification.getRating() <= 3 && comment.getText().toString().trim().length()==0){
        if(qualification.getRating() <= getResources().getInteger(R.integer.minimumCalification) && comment.getText().toString().trim().length()==0){
            return false;
        }else {
            return true;
        }

    }

    private void Restart() {

        editor.clear();
        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User",u);
        editor.putString("state","start");
        editor.commit();

        state = preferences.getString("state", "start");
    }
}
