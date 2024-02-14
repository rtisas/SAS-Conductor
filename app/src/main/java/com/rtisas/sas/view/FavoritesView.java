package com.rtisas.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.rtisas.sas.adapter.FavoritesA;
import com.rtisas.sas.model.EventService;
import com.rtisas.sas.model.Localization;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FavoritesView extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener {

    public static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ArrayList<Localization> localizationArray;
    private ConsumeService consumeService;
    private FavoritesA favoritesA;
    private TextView searchfav;
    private ListView listfav;
    private Place place;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_view);

        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);

        searchfav = (TextView)findViewById(R.id.searchfav);
        listfav = (ListView)findViewById(R.id.listfav);

        searchfav.setOnClickListener(this);
        listfav.setOnItemClickListener(this);

        email = getIntent().getStringExtra("email");

        Progress();
        consumeService.ConsumerService("getfavorite",email);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.searchfav:
                    GooglePlace();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = getIntent();
        i.putExtra("RESULTADO",localizationArray.get(position).getNombre());
        i.putExtra("Address",localizationArray.get(position).getDireccion());
        i.putExtra("lat",localizationArray.get(position).getLatitud());
        i.putExtra("lng",localizationArray.get(position).getLongitud());
        setResult(RESULT_OK, i);
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {

                place = PlaceAutocomplete.getPlace(this, data);
                Intent i = getIntent();

                i.putExtra("RESULTADO",place.getName());
                i.putExtra("Address",place.getAddress());
                i.putExtra("lat", String.valueOf(place.getLatLng().latitude));
                i.putExtra("lng", String.valueOf(place.getLatLng().longitude));
                setResult(RESULT_OK, i);

                finish();

           } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event (final EventService event) {

        if (event.getAction().equals("getfavorite")){
            String code = event.getResponse().getFunctionalMessage().getCode();
            if(code.equals("F-0000")){
                localizationArray = event.getResponse().getLocalizations();
                favoritesA = new FavoritesA(getApplicationContext(),localizationArray);
                listfav.setAdapter(favoritesA);
                if(progress.isShowing()){
                    progress.dismiss();
                }
            }else{
                AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else if (event.getAction().equals("deletefavorite")){
            AlertMessage (event.getResponse().getFunctionalMessage().getMessage(),false);
        }else if (event.getAction().equals("error")){
            AlertMessage (event.getResult(),true);
        }

    }

    public void GooglePlace(){

        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("CO").build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter).build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();

        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    private void Progress (){

        if(progress.isShowing()){
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.app_name));
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
                    startActivity(new Intent(getApplicationContext(),SplashView.class));
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

}
