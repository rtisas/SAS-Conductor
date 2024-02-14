package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rtisas.sas.model.Card;
import com.rtisas.sas.model.EventService;
import com.rtisas.sas.model.Request;
import com.rtisas.sas.model.User;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Calendar;
import java.util.regex.Pattern;
import morxander.editcard.EditCard;

public class AddCardView extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private AlertDialog.Builder alertDialog;
    private SharedPreferences preferences;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private Request request;
    private EditText date,code,name;
    private TextView requestbutton;
    private EditCard number;
    private User user;
    private Card card;
    private Gson gson;
    private String nameCard;
    private boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_view);

        preferences = this.getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        user = new User();
        card = new Card();
        gson = new Gson();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        requestbutton = (TextView)findViewById(R.id.requestbutton);
        number = (EditCard)findViewById(R.id.number);
        date = (EditText)findViewById(R.id.date);
        code = (EditText)findViewById(R.id.code);
        name = (EditText)findViewById(R.id.name);

        requestbutton.setOnClickListener(this);
        number.addTextChangedListener(numberWatcher);
        date.addTextChangedListener(dateWatcher);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.requestbutton:
                if(Validate()){
                    Progress();
                    AddCard();
                }
                Log.e("addcard","no paso la validacion");
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
    public void Event(final EventService event) {

        if(event.getAction().equals("addcard")){
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),true);
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }

    }

    private final TextWatcher numberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void afterTextChanged(Editable s) {
          //  Log.d("DEBUG", "cardnumber : " + number.getCardNumber());
          //  Log.d("DEBUG", "cardTipe : " + number.getCardType());
          //  Log.d("DEBUG", "cardvalid : " + number.isValid());

            nameCard=number.getCardType();
            if (nameCard.equals("American_Express")){
                code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            }else{
                code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            }
        }
    };

    private final TextWatcher dateWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int before, int i2) {
            String working = charSequence.toString();
            isValid = true;
            Pattern pattern1 = Pattern.compile("^[0-9]+$");
            if (working.length() == 4 && before == 0) {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (!pattern1.matcher(working).matches()){
                    isValid = false;
                }else{
                    if (Integer.parseInt(working) < currentYear) {
                        isValid = false;
                    }else {
                        working += "/";
                        date.setText(working);
                        date.setSelection(working.length());
                        isValid = true;
                    }
                }
            } else if (working.length() == 7 && before == 0) {
                String enteredmonth = working.substring(5);
                Pattern pattern2 = Pattern.compile("^[0-9]+$");
                if (pattern1.matcher(working).matches()){
                    isValid = false;
                }else {
                    if (Integer.parseInt(enteredmonth) < 1 || Integer.parseInt(enteredmonth) > 12) {
                        isValid = false;
                    }else{
                        isValid = true;
                    }
                }
            } else if (working.length() != 7) {
                isValid = false;
            }
            if (!isValid) {
                date.setError(getResources().getString(R.string.dateEF));
            } else {
                date.setError(null);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

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
                    startActivity(new Intent(getApplicationContext(),MapView.class));
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

    private boolean Validate() {

        boolean dateV = Date(date.getText().toString());
        boolean codeV = Code(code.getText().toString());
        boolean nameV = Name(name.getText().toString());
        return dateV && codeV && nameV && isValid;

    }

    private boolean Name(String text) {

        Pattern pattern = Pattern.compile("^[a-zA-ZáéíóúAÉÍÓÚÑñ[:space:]]+$");
        if (!pattern.matcher(text).matches() || text.length() > 30) {
            name.setError(this.getString(R.string.nameE));
            return false;
        } else {
            name.setError(null);
            return true;
        }

    }

    private boolean Code(String text) {

        Pattern pattern = Pattern.compile("^[0-9]+$");
        if (!pattern.matcher(text).matches() || text.length() > 4 ||text.length() <3) {
            code.setError(this.getString(R.string.codeE));
            return false;
        } else {
            code.setError(null);
            return true;
        }

    }

    private boolean Date(String text) {

        if (text.length() < 7||text.length()>7) {
            code.setError(getResources().getString(R.string.dateEF));
            return false;
        } else {
            code.setError(null);
            return true;
        }

    }

    private void AddCard() {

        int tipoCard = 0;
        if ((nameCard.equals("American_Express")&&code.length()==4)||(!nameCard.equals("American_Express")&&code.length()==3)) {
            if (number.isValid()) {
                switch (nameCard) {
                    case "Visa":
                        tipoCard = 1;
                        break;
                    case "MasterCard":
                        tipoCard = 2;
                        break;
                    case "Diners_Club":
                        tipoCard = 4;
                        break;
                    case "American_Express":
                        tipoCard = 5;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid card: " + nameCard);
                }
            }
        }
        request = new Request();
        card.setNumero(number.getCardNumber());
        card.setTipo(tipoCard);
        card.setVigencia(date.getText().toString());
        card.setCodigoSeguridad(code.getText().toString());
        card.setNombreTarjeta(name.getText().toString());

        request.setCard(card);
        request.setUser(user);

        consumeService.ConsumerService("addcard",request);

    }

}
