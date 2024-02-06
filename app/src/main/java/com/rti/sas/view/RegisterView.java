package com.rti.sas.view;

import android.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.gson.Gson;
import com.rti.sas.fragment.Facebook;
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
import java.util.regex.Pattern;

import retrofit2.Response;

public class RegisterView extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, Facebook.EventsFragments {

    private static final String PREF = "preferences";
    public static final int RC_SIGN_IN = 9001;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private GoogleSignInOptions googleOptions;
    private GoogleSignInAccount googleAccount;
    private GoogleApiClient googleClient;
    private EditText name,lastname,email,mobile,password,validator;
    private TextView toregister,terms,imvalid;
    private Fragment facebook;
    private SignInButton google;
    private Request request;
    private User user;
    private Gson gson;
    private int valeat;
    private String valeatorio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        preferences = this.getSharedPreferences(PREF,MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        facebook = new Facebook();
        gson = new Gson();

        try {
            user = gson.fromJson(preferences.getString("User","user"),User.class);
        }catch (Exception e){
            Log.e("user_failed",e.getMessage());
        }


        lastname = (EditText)findViewById(R.id.lastname);
        password = (EditText)findViewById(R.id.password);
        mobile = (EditText)findViewById(R.id.mobile);
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        toregister = (TextView)findViewById(R.id.toregister);
        terms = (TextView)findViewById(R.id.terms);
        google = (SignInButton)findViewById(R.id.google);
        validator = (EditText)findViewById(R.id.validator);
        valeat = (int) (Math.random()*(999999-100000+1)+100000);
        imvalid = (TextView) findViewById(R.id.imvalid);
        imvalid.setText(String.valueOf(valeat));

        toregister.setOnClickListener(this);
        google.setOnClickListener(this);
        terms.setOnClickListener(this);

        google.setSize(SignInButton.SIZE_ICON_ONLY);

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.add(R.id.facebook,facebook);
        fTransaction.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.toregister:
                if (Validate()) {
                        Register();
                }
                break;
            case R.id.google:
                SignOut();
                break;
        }

    }

    @Override
    protected void onStart() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("googleclient","googleclient");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            ResultGoogle(result);
        }
    }

    @Override
    public void FragmentInteraction(String email, String name, String apellido) {
        this.email.setText(email);
        this.name.setText(name);
        this.lastname.setText(apellido);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event (final EventService event) {
        Log.d("CZ- llama al método", "evet");

        if (event.getAction().equals("register")){
            String code = event.getResponse().getFunctionalMessage().getCode();
            if(code.equals("F-0000")){
                AlertMessage(this.getString(R.string.successreg),true);
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else{
            AlertMessage (event.getResult(),false);
        }
    }

    private void SignOut() {
        if(googleClient != null) {
            googleClient.disconnect();
        }
        googleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("707998206566-uj4revi9ng2io8k47ci2vqbo5kc3834r.apps.googleusercontent.com").requestScopes(new Scope(Scopes.PLUS_LOGIN)).requestEmail().build();
        googleClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(Auth.GOOGLE_SIGN_IN_API, googleOptions).build();
        startActivityForResult(new Intent(Auth.GoogleSignInApi.getSignInIntent(googleClient)), RC_SIGN_IN);
    }

    private void ResultGoogle(GoogleSignInResult result){
        if (result.isSuccess()) {

            googleAccount = result.getSignInAccount();

            if (googleAccount != null) {
                email.setText(googleAccount.getEmail());
                name.setText(googleAccount.getGivenName());
                lastname.setText(googleAccount.getFamilyName());
            }
        }
    }

    private void Register () {

        Progress();
        request = new Request();
        request.setUser(user);
        consumeService.ConsumerService("register",request);

    }

    private void Progress (){

        if(progress.isShowing()){
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.register));
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
                    finish();
                }
            }
        });
        alertDialog.create().show();
    }

    private boolean Validate() {

        boolean tokenV = Token();
        boolean nameV = Name(name.getText().toString());
        boolean lastnameV = LastName(lastname.getText().toString());
        boolean emailV = Email(email.getText().toString());
        boolean mobileV = Mobile(mobile.getText().toString());
        boolean passwordV = Password(password.getText().toString());
        boolean validatorV = Valid(validator.getText().toString());
        return nameV && lastnameV && emailV && mobileV && passwordV && tokenV && validatorV;

    }

        private boolean Token () {

        if(user.getRegistration_id() != null){
         //   Log.e("token","si hay token");
            return true;
        }else{
         //   Log.e("token","error al generar token");
            return false;
        }

    }

    private boolean Name(String text) {

        Pattern pattern = Pattern.compile("^[a-zA-ZáéíóúAÉÍÓÚÑñ[:space:]]+$");
        if (!pattern.matcher(text).matches() || text.length() > 30) {
            name.setError(this.getString(R.string.nameE));
            return false;
        } else {
            user.setFirstName(text.trim());
            name.setError(null);
            return true;
        }

    }

    private boolean LastName(String text) {

        Pattern pattern = Pattern.compile("^[a-zA-ZáéíóúAÉÍÓÚÑñ[:space:]]+$");
        if (!pattern.matcher(text).matches() || text.length() > 30) {
            lastname.setError(this.getString(R.string.lastnameE));
            return false;
        } else {
            user.setLastName(text.trim());
            lastname.setError(null);
            return true;
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

    private boolean Mobile(String text) {

        if (!Patterns.PHONE.matcher(text).matches() || text.length() != 10) {
            mobile.setError(this.getString(R.string.mobileE));
            return false;
        } else {
            user.setPhone(text);
            mobile.setError(null);
            return true;
        }

    }

    private boolean Valid(String text) {

        if (validator.getText().toString().isEmpty()) {
            validator.setError(this.getString(R.string.valierror));
            return false;
        } else {

            valeatorio2 = validator.getText().toString();
            int valid = Integer.parseInt(valeatorio2);
            if (valeat != valid) {
                validator.setError(this.getString(R.string.valierror));
                return false;
            } else {
                validator.setError(null);
                return true;
            }
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
