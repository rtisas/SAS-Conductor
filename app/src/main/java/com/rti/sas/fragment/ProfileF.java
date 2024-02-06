package com.rti.sas.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rti.sas.model.EventService;
import com.rti.sas.model.Request;
import com.rti.sas.model.User;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.R;
import com.rti.sas.view.HistoryView;
import com.rti.sas.view.LoginView;
import com.rti.sas.view.PayView;
import com.rti.sas.view.PqrsView;
import com.rti.sas.view.SplashView;
import com.rti.sas.view.UserView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import static android.content.Context.MODE_PRIVATE;

public class ProfileF extends Fragment implements View.OnClickListener{

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private ConsumeService consumeService;
    private Request request;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private TextView person,pay,travel,promo,pqrs,singoff;
    private LinearLayout menu;
    private User user;
    private Gson gson;
    private String state;

    public ProfileF() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getActivity().getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        alertDialog = new AlertDialog.Builder(getActivity());
        progress = new ProgressDialog(getActivity());
        consumeService = new ConsumeService();
        editor = preferences.edit();
        gson = new Gson();
        state = preferences.getString("state","start");
        user = gson.fromJson(preferences.getString("User", "user"), User.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        singoff = (TextView)view.findViewById(R.id.singoff);
        travel = (TextView)view.findViewById(R.id.travel);
        person = (TextView)view.findViewById(R.id.person);
        menu = (LinearLayout)view.findViewById(R.id.menu);
        promo = (TextView)view.findViewById(R.id.promo);
        pqrs = (TextView)view.findViewById(R.id.pqrs);
        pay = (TextView)view.findViewById(R.id.pay);

        singoff.setOnClickListener(this);
        person.setOnClickListener(this);
        travel.setOnClickListener(this);
        promo.setOnClickListener(this);
        pqrs.setOnClickListener(this);
        pay.setOnClickListener(this);

        menu.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.person:
                intent = new Intent(getActivity().getApplication(),UserView.class);
                startActivity(intent);
                break;
            case R.id.pay:
                intent = new Intent(getActivity().getApplication(),PayView.class);
                startActivity(intent);
                break;
            case R.id.travel:
                intent = new Intent(getActivity().getApplication(),HistoryView.class);
                intent.putExtra("email",user.getEmail());
                intent.putExtra("view","schedule");
                startActivity(intent);
                break;
            case R.id.promo:
                Promo();
                break;
            case R.id.pqrs:
                intent = new Intent(getActivity().getApplication(),PqrsView.class);
                startActivity(intent);
                break;
            case R.id.singoff:
                if(!state.equals("arrived")&&!state.equals("initiate")&&!state.equals("finish")){
                    state = "disconnected";
                    Restart();
                    startActivity(new Intent(getActivity().getApplicationContext(), LoginView.class));
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventService event) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(event.getAction().equals("promo")){
            AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),false);
        }
    }

    private void Progress() {

        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.app_name));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    private void AlertMessage (String message,final boolean finish) {

        if(alertDialog.create().isShowing()){
            alertDialog.create().dismiss();
        }
        if(progress.isShowing()){
            progress.dismiss();
        }
        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(this.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if(finish){
                    startActivity(new Intent(getActivity().getApplicationContext(),SplashView.class));
                    getActivity().finish();
                }
            }
        });
        alertDialog.create().show();

    }

    private void Promo() {

        final EditText input = new EditText(getActivity().getApplicationContext());
        input.setSingleLine(true);
        input.setTextColor(Color.parseColor("#000000"));
        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(input);
        alertDialog.setTitle(getResources().getString(R.string.promoadd));
        alertDialog.setPositiveButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = "";
                value = input.getText().toString();
                request = new Request();
                request.setUser(user);
                request.setCodigo(value);
                request.setRegistro(false);
                Progress();
                consumeService.ConsumerService("promo",request);
            }
        });
        alertDialog.setNegativeButton(this.getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }

    private void Restart() {

        editor.clear();
        gson = new Gson();
        User user1 = new User();
        user1.setRegistration_id(user.getRegistration_id());
        String u = gson.toJson(user1);
        editor.putString("User",u);
        editor.putString("state","start");
        editor.commit();

    }

    private void Save() {

        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User",u);
        editor.commit();

    }

}