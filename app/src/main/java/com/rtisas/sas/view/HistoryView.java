package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rtisas.sas.adapter.CardA;
import com.rtisas.sas.adapter.HistoryA;
import com.rtisas.sas.model.EventService;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HistoryView extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private HistoryA historya;
    private CardA cardA;
    private TextView title,mencard;
    private ImageView addcard;
    private ListView list;
    private String email,view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_view);

        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);

        addcard = (ImageView)findViewById(R.id.addcard);
        mencard = (TextView)findViewById(R.id.mencard);
        title = (TextView)findViewById(R.id.title);
        list = (ListView)findViewById(R.id.list);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        view = intent.getStringExtra("view");
        mencard.setVisibility(View.GONE);
        addcard.setVisibility(View.GONE);
        addcard.setOnClickListener(this);

        Progress();
        consumeService.ConsumerService(view,email);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.addcard:
                startActivity(new Intent(this.getApplicationContext(), AddCardView.class));
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
        if(event.getAction().equals("getCards")) {
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                title.setText(getResources().getString(R.string.pay));
                cardA = new CardA(this.getApplicationContext(),event.getResponse().getCards());
                list.setAdapter(cardA);
                addcard.setVisibility(View.VISIBLE);
                mencard.setVisibility(View.VISIBLE);
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),true);
            }
        }else if(event.getAction().equals("schedule")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                title.setText(getResources().getString(R.string.reservations));
                if(event.getResponse().getServicios().size()==0){
                    AlertMessage(getResources().getString(R.string.reservasE),true);
                }
                historya = new HistoryA(this.getApplicationContext(),event.getResponse().getServicios());
                list.setAdapter(historya);
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }else if(event.getAction().equals("cancel1")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }else if(event.getAction().equals("error")) {
            AlertMessage(event.getResult(), true);
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
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

}
