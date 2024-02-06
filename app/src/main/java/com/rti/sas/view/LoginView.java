package com.rti.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rti.sas.model.EventService;
import com.rti.sas.model.Request;
import com.rti.sas.model.User;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginView extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private TextView sign_in,recover,register;
    private EditText email,password;
    private Intent intent;
    private Request request;
    private User user;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        preferences = this.getSharedPreferences(PREF,MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        gson = new Gson();

        try {
            user = gson.fromJson(preferences.getString("User","user"),User.class);
        }catch (Exception e){
            Log.e("user_failed",e.getMessage());
            user = new User();
        }

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        register = (TextView)findViewById(R.id.register);
        sign_in = (TextView)findViewById(R.id.sign_in);
        recover = (TextView)findViewById(R.id.recover);

        sign_in.setOnClickListener(this);
        recover.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in:
                if(Validate()){
                    Login();
                }
                break;
            case R.id.recover:
                intent = new Intent(this, RecoverView.class);
                startActivity(intent);
                break;
            case R.id.register:
                intent = new Intent(this, RegisterView.class);
                startActivity(intent);
                /*Progress();
                consumeService.ConsumerService("companyData",request);*/
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
        if (event.getAction().equals("login")){
            String code = event.getResponse().getFunctionalMessage().getCode();
            if(code.equals("F-0000")||code.equals("F-0005")){
                if(progress.isShowing()){
                    progress.dismiss();
                }
                gson = new Gson();
                String usuario = gson.toJson(event.getResponse().getUser());
                editor.putString("User",usuario);
                editor.putString("state","start");
                editor.commit();
                if(event.getResponse().getFunctionalMessage().getCode().equals("F-0005")){
                    AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),true);
                }else{
                    startActivity(new Intent(getApplicationContext(),MapView.class));
                    finish();
                }
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else if (event.getAction().equals("companyData")){
            String code = event.getResponse().getFunctionalMessage().getCode();
            if(code.equals("F-0000")){
                String respuesta[] = event.getResponse().getParameter().getValor().split(",");
                String info="";
                for(int i=0;i<respuesta.length;i++){
                    info=""+info+respuesta[i]+"\n";
                }
                AlertMessage (info,false);
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else{
            AlertMessage (event.getResult(),false);
        }

    }

    private void Login () {
        Progress();
        request = new Request();
        request.setUser(user);
        consumeService.ConsumerService("login",request);
    }

    private void Progress (){

        if(progress.isShowing()){
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.sign_in));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    private void AlertMessage (String message,final boolean finish) {

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
                    startActivity(new Intent(getApplicationContext(),MapView.class));
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

    private boolean Validate() {

        boolean tokenV = Token();
        boolean emailV = Email(email.getText().toString());
        boolean passwordV = Password(password.getText().toString());

        return emailV && passwordV && tokenV;

    }

    private boolean Token () {

        if(user.getRegistration_id()!=null){
            Log.e("token","si hay token");
            return true;
        }else{
            Log.e("token","error al generar token");
            return false;
        }

    }

    private boolean Email(String text) {

        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            email.setError(getString(R.string.emailE));
            return false;
        } else {
            user.setEmail(text);
            email.setError(null);
            return true;
        }

    }

    private boolean Password(String text) {

        if (text.length()>=5){
            password.setError(null);
            try {
                user.setPassword(EncryptSHA(text));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            password.setError(getString(R.string.passwordE));
            return false;
        }
        return true;

    }

    private static String EncryptSHA(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }

}
