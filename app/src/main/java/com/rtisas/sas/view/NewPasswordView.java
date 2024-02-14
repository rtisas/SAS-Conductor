package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rtisas.sas.model.EventService;
import com.rtisas.sas.model.Request;
import com.rtisas.sas.model.User;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewPasswordView extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private EditText code,password,newpassword;
    private TextView confirm;
    private Request request;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password_view);

        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        user = new User();

        newpassword = (EditText)findViewById(R.id.newpassword);
        password = (EditText)findViewById(R.id.password);
        confirm = (TextView)findViewById(R.id.confirm);
        code = (EditText)findViewById(R.id.code);

        confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (Validate()) {
                    NewPassword();
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

        if (event.getAction().equals("newpassword")){
            String code = event.getResponse().getFunctionalMessage().getCode();
            if(code.equals("F-0000")){
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),true);
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else{
            AlertMessage (event.getResult(),false);
        }

    }

    private void NewPassword () {

        Progress();
        user.setRestorationCode(code.getText().toString());
        user.setId(getIntent().getIntExtra("id",0));
        request = new Request();
        request.setUser(user);
        consumeService.ConsumerService("newpassword",request);

    }

    private void Progress (){

        if(progress.isShowing()){
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.newpassword));
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
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

    private boolean Validate() {

        boolean equalityV = Equality();
        boolean codeV = Code(code.getText().toString());
        boolean passwordV = Password(password.getText().toString());

        return equalityV && passwordV && codeV;

    }

    private boolean Equality () {

        if(password.getText().toString().equals(newpassword.getText().toString())){
            return true;
        }else{
            newpassword.setError(getString(R.string.passwordE));
            return false;
        }

    }

    private boolean Code (String text) {
        if(text.length()>=1){
            return true;
        }else {
            code.setError(getString(R.string.codeE));
            return false;
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
