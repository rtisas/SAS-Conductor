package com.rti.sas.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.rti.sas.adapter.HistoryA;
import com.rti.sas.broadcast.ConnectivityReceiver;
import com.rti.sas.fragment.ChatF;
import com.rti.sas.fragment.ProfileF;
import com.rti.sas.model.EventService;
import com.rti.sas.model.Localization;
import com.rti.sas.model.Request;
import com.rti.sas.model.ResponseServer;
import com.rti.sas.model.Service;
import com.rti.sas.model.Ubication;
import com.rti.sas.model.User;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapView extends AppCompatActivity implements View.OnClickListener, AHBottomNavigation.OnTabSelectedListener, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_PHONE = 88;
    private static final String PREF = "preferences";
    private static final int SelectF = 2;
    private static final int SelectD = 1;
    private static final int SelectS = 0;
    private static final int mMap = 10;
    private static final int zoom = 14;
    private NotificationManager notificationManager;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private ConnectivityReceiver connectivityReceiver;
    private Request request;
    private AHBottomNavigationItem item1, item2, item3, item4, item5;
    private AHBottomNavigation navigation;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private LocationRequest locationRequest;
    private GoogleApiClient googleClient;
    private Localization localization;
    private LatLng myLatLng;
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private ChatF chatF;
    private TextView mins, row0, row1, row2, row3, row4, source, title, destination, triger, select;
    private RatingBar qualification;
    private LinearLayout content, content1, infouser, infotravel, fragment;
    private CircleImageView photo;
    private ListView history;
    private ImageView waze, info;
    private HistoryA historya;
    private Service service;
    private User user;
    private Gson gson;
    private String state, citySource, cityDestination, stateActivity;
    private boolean ns, nn, background, updatemin, stateQuery, query, airportSource, airportDestination, direcctionfavoritos, cityfavoritos;
    private DecimalFormat formateador;
    private double lator, latde, lonor, londe;
    //private int acum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityReceiverListener(this);
        preferences = this.getSharedPreferences(PREF, MODE_PRIVATE);
        alertDialog = new AlertDialog.Builder(this);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        editor = preferences.edit();
        localization = new Localization();
        service = new Service();
        user = new User();
        gson = new Gson();
        direcctionfavoritos = true;
        query = true;
        stateActivity = "start";
        state = preferences.getString("state", "start");
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        navigation = (AHBottomNavigation) findViewById(R.id.navigation);
        infotravel = (LinearLayout) findViewById(R.id.infotravel);
        infouser = (LinearLayout) findViewById(R.id.infouser);
        fragment = (LinearLayout) findViewById(R.id.fragment);
        content1 = (LinearLayout) findViewById(R.id.content1);
        content = (LinearLayout) findViewById(R.id.content);
        photo = (CircleImageView) findViewById(R.id.photo);
        history = (ListView) findViewById(R.id.history);
        qualification = (RatingBar) findViewById(R.id.qualification);
        destination = (TextView) findViewById(R.id.destination);
        source = (TextView) findViewById(R.id.source);
        triger = (TextView) findViewById(R.id.triger);
        select = (TextView) findViewById(R.id.select);
        title = (TextView) findViewById(R.id.title);
        waze = (ImageView) findViewById(R.id.waze);
        info = (ImageView) findViewById(R.id.info);
        row0 = (TextView) findViewById(R.id.row0);
        row1 = (TextView) findViewById(R.id.row1);
        row2 = (TextView) findViewById(R.id.row2);
        row3 = (TextView) findViewById(R.id.row3);
        row4 = (TextView) findViewById(R.id.row4);
        mins = (TextView) findViewById(R.id.mins);

        fragment.setVisibility(View.GONE);
        content1.setVisibility(View.GONE);
        select.setVisibility(View.GONE);

        formateador = new DecimalFormat("###,###.##");
        navigation.setOnTabSelectedListener(this);
        destination.setOnClickListener(this);
        source.setOnClickListener(this);
        select.setOnClickListener(this);
        waze.setOnClickListener(this);
        info.setOnClickListener(this);

        LoadState();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        checkGPS();
        LoadView(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        checkGPS();
        stateActivity = "start";
        navigation.setVisibility(View.VISIBLE);
        navigation.removeAllItems();
        LoadState();
        LoadView(intent);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.destination:
                startActivityForResult(new Intent(getApplicationContext(), FavoritesView.class).putExtra("email", user.getEmail()), SelectD);
                select.setText("Seleccionar Destino");
                select.setVisibility(View.VISIBLE);
                stateActivity = "destination";
                airportDestination = false;
                break;
            case R.id.source:
                startActivityForResult(new Intent(getApplicationContext(), FavoritesView.class).putExtra("email", user.getEmail()), SelectS);
                select.setText("Seleccionar Origen");
                select.setVisibility(View.VISIBLE);
                stateActivity = "source";
                airportSource = false;
                break;
            case R.id.select:
                direcctionfavoritos = true;
                selectSites();
                break;
            case R.id.waze:
                Waze();
                break;
            case R.id.info:
                Info();
                break;

        }
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if (nn) {
            switch (position) {
                case 0:
                    if (ns) {
                        Travel(1);
                    } else {
                        ToCall();
                    }
                    break;
                case 1:
                    if (ns) {
                        History("history");
                    } else {
                        title.setText(getResources().getString(R.string.chat));
                        Chat();
                    }
                    break;
                case 2:
                    if (ns) {
                        Travel(2);
                    } else {
                        Cancel(true);
                    }
                    break;
                case 3:
                    if (ns) {
                        startActivityForResult(new Intent(getApplicationContext(), FavoritesView.class).putExtra("email", user.getEmail()), SelectF);
                        //Favorites();
                    } else {
                        Share();
                    }
                    break;
                case 4:
                    notificationManager.cancelAll();
                    title.setText(getResources().getString(R.string.profile));
                    Profile();
                    break;
            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (googleClient == null) {
                    BuildGoogleClient();
                }
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            if (googleClient == null) {
                BuildGoogleClient();
            }
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //Log.e("onLocationChanged", "paso por aqui" + location.getLatitude() + " " + location.getLongitude());
        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.setPadding(mMap, (mMap * 4), mMap, mMap);
        boolean travel = state.equals("arrived") || state.equals("initiate") || state.equals("finish");
        if (source.getText().toString().equals("Origen") && myLatLng.latitude != 0 && !travel) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, zoom));
            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if (state.equals("start")) {
                        if (stateActivity.equals("start")) {
                            String latlng = cameraPosition.target.latitude + "," + cameraPosition.target.longitude;
                            service.setLatitudInicial(Double.toString(cameraPosition.target.latitude));
                            service.setLongitudInicial(Double.toString(cameraPosition.target.longitude));
                            localization.setLatitud(Double.toString(cameraPosition.target.latitude));
                            localization.setLongitud(Double.toString(cameraPosition.target.longitude));
                            Marker(cameraPosition.target.latitude, cameraPosition.target.longitude, 0, 0);
                            Progress();
                            airportSource = airportDestination = false;
                            consumeService.ConsumerGoogle("geocode", latlng, "es");
                        } else if (stateActivity.equals("destination")) {
                            String latlon = cameraPosition.target.latitude + "," + cameraPosition.target.longitude;
                            service.setLatitudFinal(Double.toString(cameraPosition.target.latitude));
                            service.setLongitudFinal(Double.toString(cameraPosition.target.longitude));
                            double latd = Double.parseDouble(service.getLatitudFinal());
                            double lngd = Double.parseDouble(service.getLongitudFinal());
                            Marker(0, 0, latd, lngd);
                            select.setVisibility(View.VISIBLE);
                        } else if (stateActivity.equals("source")) {
                            String latlon = cameraPosition.target.latitude + "," + cameraPosition.target.longitude;
                            service.setLatitudInicial(Double.toString(cameraPosition.target.latitude));
                            service.setLongitudInicial(Double.toString(cameraPosition.target.longitude));
                            localization.setLatitud(Double.toString(cameraPosition.target.latitude));
                            localization.setLongitud(Double.toString(cameraPosition.target.longitude));
                            double lats = Double.parseDouble(service.getLatitudInicial());
                            double lngs = Double.parseDouble(service.getLongitudInicial());
                            Marker(lats, lngs, 0, 0);
                            select.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
        if (travel && !background && updatemin) {
            Marker(Double.parseDouble(service.getLatitudInicial()), Double.parseDouble(service.getLongitudInicial()), Double.parseDouble(service.getLatitudFinal()), Double.parseDouble(service.getLongitudFinal()));
            String source = myLatLng.latitude + " , " + myLatLng.longitude;
            String destination = service.getLatitudFinal() + " , " + service.getLongitudFinal();
            consumeService.ConsumerGoogle("distance", source, destination);
        }
        if (updatemin == false) {
            updatemin = true;
        } else {
            updatemin = false;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleClient == null) {
                            BuildGoogleClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                    startActivity(new Intent(getApplicationContext(), SplashView.class));
                    finish();
                } else {
                    checkLocationPermission();
                    Toast.makeText(this, getResources().getString(R.string.permitGPS), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_PHONE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    checkLocationPermission();
                    Toast.makeText(this, getResources().getString(R.string.permitCALL), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onNetworkConnection(boolean isConnected) {
        if (!isConnected) {
            triger.setVisibility(View.VISIBLE);
        } else {
            triger.setVisibility(View.GONE);
            Query();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);
        background = false;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityReceiverListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        background = true;
        if (state.equals("start")) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void finish() {
        if (googleClient != null) {
            if (googleClient.isConnected()) {
                googleClient.disconnect();
            }
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (fragment.getVisibility() == View.VISIBLE || content1.getVisibility() == View.VISIBLE) {
            UnLoadFragment();
        } else if (state.equals("arrived") || state.equals("initiate") || state.equals("finish")) {
            AlertMessage(getResources().getString(R.string.returnE), false);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (resultCode == RESULT_CANCELED) {
        } else {
            localization.setLongitud(data.getStringExtra("lng"));
            localization.setLatitud(data.getStringExtra("lat"));
            localization.setDireccion(data.getStringExtra("Address"));
            localization.setNombre(data.getStringExtra("RESULTADO"));
            direcctionfavoritos = false;
            cityfavoritos = false;
            switch (requestCode) {
                case SelectD:
                    stateActivity = "destination";
                    service.setLongitudFinal(data.getStringExtra("lng"));
                    service.setLatitudFinal(data.getStringExtra("lat"));
                    service.setDescDestino(data.getStringExtra("Address"));
                    destination.setText(data.getStringExtra("RESULTADO"));
                    double latd = Double.parseDouble(service.getLatitudFinal());
                    double lngd = Double.parseDouble(service.getLongitudFinal());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latd, lngd)));
                    Marker(0, 0, latd, lngd);
                    selectSites();
                    Save();
                    break;
                case SelectS:
                    stateActivity = "source";
                    service.setLongitudInicial(data.getStringExtra("lng"));
                    service.setLatitudInicial(data.getStringExtra("lat"));
                    service.setDescOrigen(data.getStringExtra("Address"));
                    source.setText(data.getStringExtra("RESULTADO"));
                    double lats = Double.parseDouble(service.getLatitudInicial());
                    double lngs = Double.parseDouble(service.getLongitudInicial());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lats, lngs)));
                    Marker(lats, lngs, 0, 0);
                    selectSites();
                    Save();
                    break;
                case SelectF:
                    cityfavoritos = true;
                    String latlng = "";
                    latlng = localization.getLatitud() + "," + localization.getLongitud();
                    Log.e("LATITUDLONGITUDFAVORITO", latlng);
                    consumeService.ConsumerGoogle("geocode", latlng, "es");
                    Favorites();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventService event) {

        if (progress.isShowing()) {
            progress.dismiss();
        }
        triger.setVisibility(View.GONE);
        if (event.getAction().equals("distance")) {
            Distance(event.getResponse());
        } else if (event.getAction().equals("geocode")) {
            if (cityfavoritos == true) {
               GeoCodeCityFav(event.getResponse());
            } else { GeoCode(event.getResponse());}

        } else if (event.getAction().equals("addfavorite")) {
            AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
        } else if (event.getAction().equals("cancel")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                navigation.removeAllItems();
                Restart();
                startActivity(new Intent(getApplicationContext(), SplashView.class));
                finish();
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        } else if (event.getAction().equals("cancel1")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        } else if (event.getAction().equals("history")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                title.setText(getResources().getString(R.string.history));
                if (event.getResponse().getServicios().size() == 0) {
                    AlertMessage(getResources().getString(R.string.historialE), false);
                }
                historya = new HistoryA(getApplicationContext(), event.getResponse().getServicios());
                history.setAdapter(historya);
            } else {
                title.setText(getResources().getString(R.string.app_name));
                navigation.setVisibility(View.VISIBLE);
                fragment.setVisibility(View.GONE);
                content1.setVisibility(View.GONE);
                navigation.removeAllItems();
                LoadState();
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        } else if (event.getAction().equals("state")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                State(event.getResponse().getServicio());
            }
        } else if (event.getAction().equals("sendmessage")) {
            chatF.save();
        } else if (event.getAction().equals("error")) {
            AlertMessage(event.getResult(), false);
            triger.setVisibility(View.VISIBLE);
        }
    }

    private void LoadState() {

        if (fragment.getVisibility() == View.VISIBLE) {
            fragment.setVisibility(View.GONE);
        }
        state = preferences.getString("state", "start");
        service = gson.fromJson(preferences.getString("Service", ""), Service.class);
        if (service == null) {
            service = new Service();
        }
        boolean travel = state.equals("arrived") || state.equals("initiate") || state.equals("finish") || state.equals("qualification") || state.equals("search");
        if (state.equals("start") || !travel) {
            if (fragment.getVisibility() == View.GONE) {
                content.setVisibility(View.VISIBLE);
            }
            infotravel.setVisibility(View.GONE);
            infouser.setVisibility(View.GONE);
            content1.setVisibility(View.GONE);
            waze.setVisibility(View.GONE);
            info.setVisibility(View.GONE);
            Navigation(true);
        } else if (state.equals("qualification")) {
            startActivity(new Intent(getApplicationContext(), QualifyView.class));
            state = "start";
            finish();
        } else if (state.equals("search")) {
            int timer = TimerSearch();
            if (TimerSearch() < 8) {
                startActivity(new Intent(getApplicationContext(), SearchView.class).putExtra("timer", timer));
                finish();
            } else {
                Restart();
            }
        } else if (travel) {
            infotravel.setVisibility(View.VISIBLE);
            infouser.setVisibility(View.VISIBLE);
            waze.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            String foto = service.getFotoConductor();
            if (foto != null) {
                //Picasso.with(getApplicationContext()).load(foto).into(photo);
                Picasso.get().load(foto).into(photo);
            }
            qualification.setRating(Float.parseFloat(service.getCalificacionConductor()));
            row0.setText(service.getNombreConductor() + " " + service.getApellidoConductor());
            row1.setText(service.getPlaca().substring(0, 3) + "-" + service.getPlaca().substring(3, 6));
            row2.setText("# Servicio: " + service.getId());
            DecimalFormat formateador = new DecimalFormat("###,###.##");
            row3.setText("$ " + formateador.format(service.getValor()));
            mins.setText(String.valueOf(service.getTiempoEstimado()) + " m");
            Navigation(false);
            if (state.equals("arrived")) {
                title.setText(getResources().getString(R.string.arrivedt));
            }
            if (state.equals("initiate")) {
                title.setText(getResources().getString(R.string.initiatet));
            }
            if (state.equals("finish")) {
                title.setText(getResources().getString(R.string.finisht));
            }
        }

    }

    private void LoadView(Intent intent) {

        if (intent.getStringExtra("type") != null) {
            String title = intent.getStringExtra("title");
            String type = intent.getStringExtra("type");
            String opt = intent.getStringExtra("opt");

            switch (type) {
                case "0003":
                    AlertMessage(title + " #Servicio: " + service.getId(), false);
                    row4.setText("'Llegando en " + service.getTiempoRecogida() + " min'");
                    break;
                case "0006":
                    AlertMessage(opt, true);
                    break;
                case "0007":
                    notificationManager.cancelAll();
                    /*if(user.getTipopago().equals("final")){
                        AlertMessage(getResources().getString(R.string.pago),true);
                    }else{
                        startActivity(new Intent(getApplicationContext(),QualifyView.class));
                        finish();
                    }*/
                    break;
                case "0008":
                    AlertMessage(opt, false);
                    row4.setText(opt);
                    break;
                case "0009":
                    AlertMessage(opt, false);
                    break;
                case "0011":
                    AlertMessage(opt, false);
                    break;
                case "0014":
                    if (user.getTipopago().equals("inicio")) {
                        AlertMessage(opt, false);
                        row4.setText(opt);
                    } else {
                        AlertMessage(getResources().getString(R.string.pagofin), false);
                        row4.setText(getResources().getString(R.string.pagofin));
                    }
                    break;
                case "0016":
                    AlertMessage(title, false);
                    row4.setText("'Llegando en " + service.getTiempoRecogida() + " min'");
                    break;
                case "0018":

                    break;
            }
        }
    }

    private void GeoCode(ResponseServer response) {

        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (query) {
            Query();
            query = false;
        }

        service.setFueraCiudad(false);
        service.setAeropuerto(false);
        service.setFechaInicial("");
        service.setFechaFinal("");

        for (int i = 0; i < response.getResults().size(); i++) {
            for (int n = 0; n < response.getResults().get(i).getAddressComponents().size(); n++) {
                if (response.getResults().get(i).getAddressComponents().get(n).getTypes().get(0).equals("locality")) {
                    if (stateActivity.equals("start") || stateActivity.equals("source")) {
                        if(direcctionfavoritos){
                            source.setText(response.getResults().get(i).getFormattedAddress());
                            service.setDescOrigen(response.getResults().get(i).getFormattedAddress());
                        }
                        citySource = response.getResults().get(i).getAddressComponents().get(n).getLongName();
                        service.setCiudad(citySource);
                        //Log.e("CIUDADOR", citySource);
                        if (response.getResults().get(i).getAddressComponents().get(n).getLongName().length() <= 3) {
                            citySource = response.getResults().get(i+1).getAddressComponents().get(n).getLongName();
                            service.setCiudad(citySource);
                          //  Log.e("CIUDADOR", citySource);
                        }
                        localization.setNombre(response.getResults().get(i).getFormattedAddress());
                        localization.setDireccion(response.getResults().get(i).getFormattedAddress());
                        if (stateActivity.equals("start")) {
                            stateActivity = "source";
                        } else {
                            /*stateActivity = "select";
                            if (!destination.getText().equals("Destino")) {
                                Marker(Double.parseDouble(service.getLatitudInicial()),
                                        Double.parseDouble(service.getLongitudInicial()),
                                        Double.parseDouble(service.getLatitudFinal()),
                                        Double.parseDouble(service.getLongitudFinal()));
                            }*/
                        }
                        select.setVisibility(View.GONE);

                    } else if (stateActivity.equals("destination")) {
                        if(direcctionfavoritos){
                            service.setDescDestino(response.getResults().get(i).getFormattedAddress());
                            destination.setText(response.getResults().get(i).getFormattedAddress());
                        }
                        cityDestination = response.getResults().get(i).getAddressComponents().get(n).getLongName();
                        Log.e("CIUDADDE", cityDestination);
                        if (response.getResults().get(i).getAddressComponents().get(n).getLongName().length() <= 3) {
                            cityDestination = response.getResults().get(i+1).getAddressComponents().get(n).getLongName();
                            Log.e("CIUDADDE", cityDestination);
                        }


                        /*stateActivity = "select";
                        Marker(Double.parseDouble(service.getLatitudInicial()),
                                Double.parseDouble(service.getLongitudInicial()),
                                Double.parseDouble(service.getLatitudFinal()),
                                Double.parseDouble(service.getLongitudFinal()));*/
                        select.setVisibility(View.GONE);
                    }

                    if (citySource != null && cityDestination != null) {

                        service.setCiudadOrigen(citySource);
                        service.setCiudadDestino(cityDestination);

                        if (service.getCiudad().equals(cityDestination)) {
                            service.setFueraCiudad(false);
                        } else {
                            service.setFueraCiudad(true);
                        }
                    }

                    Log.e("city", "origen: " + citySource + " destino: " + cityDestination);
                    i = response.getResults().size();
                    break;
                    //Save();
                }
                if(response.getResults().get(i).getAddressComponents().get(n).getTypes().get(0).equals("airport")){
                    if(stateActivity.equals("start") || stateActivity.equals("source")){
                        airportSource = true;
                    }else if (stateActivity.equals("destination")){
                        airportDestination = true;
                    }
                }
            }
        }
        for (int i = 0; i < response.getResults().size(); i++) {
            for (int n = 0; n < response.getResults().get(i).getAddressComponents().size(); n++) {
                if(response.getResults().get(i).getAddressComponents().get(n).getTypes().get(0).equals("airport")){
                    if(stateActivity.equals("start") || stateActivity.equals("source")){
                        airportSource = true;
                    }else if (stateActivity.equals("destination")){
                        airportDestination = true;
                    }
                    break;
                }
            }
        }


        // solución problema recargo aeropuerto: el JSON entregado por Google Places en
        // ocasiones no activa la bandera en el aeropuerto Eldorado, por lo tanto se incluyó un polígono de la zona

        lator = Double.parseDouble(service.getLatitudInicial());
        lonor = Double.parseDouble(service.getLongitudInicial());
        latde = Double.parseDouble(service.getLatitudFinal());
        londe = Double.parseDouble(service.getLongitudFinal());



        if ((lator < 4.700148 && lator > 4.687748 && lonor < -74.131970 && lonor > -74.149136) ||
            (lator < 4.704465 && lator > 4.693265 && lonor < -74.137736 && lonor > -74.149136) ||
            (lator < 4.696156 && lator > 4.690556 && lonor < -74.126639 && lonor > -74.145639)) {

            airportSource = true;
        }

        if ((latde < 4.700148 && latde > 4.687748 && londe < -74.131970 && londe > -74.149136) ||
            (latde < 4.704465 && latde > 4.693265 && londe < -74.137736 && londe > -74.149136) ||
            (latde < 4.696156 && latde > 4.690556 && londe < -74.126639 && londe > -74.145639)) {

            airportDestination = true;
        }


        if(airportSource) {
            service.setAeropuertoOrigen(true);
        }
        else {
            service.setAeropuertoOrigen(false);
        }

        if(airportDestination) {
            service.setAeropuertoDestino(true);
        }
        else {
            service.setAeropuertoDestino(false);
        }

        service.setIdaPozo(false);

        if(airportSource || airportDestination){
            service.setAeropuerto(true);
        }
        Save();
    }

    private void GeoCodeCityFav(ResponseServer response) {

        for (int i = 0; i < response.getResults().size(); i++) {
            for (int n = 0; n < response.getResults().get(i).getAddressComponents().size(); n++) {
                if (response.getResults().get(i).getAddressComponents().get(n).getTypes().get(0).equals("locality")) {

                    localization.setCiudad(response.getResults().get(i).getAddressComponents().get(n).getLongName());
                    Log.e("CIUDADFAV", localization.getCiudad());
                    if (response.getResults().get(i).getAddressComponents().get(n).getLongName().length() <= 3) {
                        localization.setCiudad(response.getResults().get(i + 1).getAddressComponents().get(n).getLongName());
                        Log.e("CIUDADFAV", localization.getCiudad());
                    }
                }
            }
        }
    }



    private void Distance(ResponseServer response) {

        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (response.getRows().get(0).getElements().get(0).getStatus().equals("OK")) {
            if (!state.equals("start")) {
                mins.setText("" + response.getRows().get(0).getElements().get(0).getDuration().getValue() / 60 + " m");
            } else {
                service.setDistancia(response.getRows().get(0).getElements().get(0).getDistance().getValue() / 1000.0);
                service.setTiempoEstimado(response.getRows().get(0).getElements().get(0).getDuration().getValue() / 60);
                Save();
                if (stateActivity.equals("travel")) {
                    startActivity(new Intent(getApplicationContext(), RequestServiceView.class));
                } else if (stateActivity.equals("schedule")) {
                    startActivity(new Intent(getApplicationContext(), ScheduleView.class));
                }
            }
        } else {
            AlertMessage(this.getString(R.string.retryE), false);
        }

    }

    private void Marker(double latA, double lngA, double latB, double lngB) {

        googleMap.clear();
        LatLng suroeste, noreste, a, b, driver;
        a = new LatLng(latA, lngA);
        b = new LatLng(latB, lngB);

        if (latB == 0 || lngB == 0) {
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.sourcep)).position(a));
        } else if (latA == 0 || lngA == 0) {
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.destinationp)).position(b));
        } else {
            if (latA < latB) {
                if (lngA < lngB) {
                    suroeste = new LatLng(latA, lngA);
                    noreste = new LatLng(latB, lngB);
                } else {
                    suroeste = new LatLng(latA, lngB);
                    noreste = new LatLng(latB, lngA);
                }
            } else {
                if (lngA < lngB) {
                    suroeste = new LatLng(latB, lngA);
                    noreste = new LatLng(latA, lngB);
                } else {
                    suroeste = new LatLng(latB, lngB);
                    noreste = new LatLng(latA, lngA);
                }
            }
            LatLngBounds LocationPasCon = new LatLngBounds(suroeste, noreste);

            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.sourcep)).position(a));
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.destinationp)).position(b));
            if (!preferences.getString("Ubication", "ubication").equals("ubication")) {
                Ubication ubication = gson.fromJson(preferences.getString("Ubication", "ubication"), Ubication.class);
                driver = new LatLng(Double.parseDouble(ubication.getLatitud()), Double.parseDouble(ubication.getLongitud()));
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(driver));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LocationPasCon, 0));
        }

    }

    private void selectSites() {
        Progress();
        select.setVisibility(View.GONE);
        String latlng = "";
        if (stateActivity.equals("source")) {
            latlng = service.getLatitudInicial() + "," + service.getLongitudInicial();
            airportSource = false;
        } else if (stateActivity.equals("destination")) {
            latlng = service.getLatitudFinal() + "," + service.getLongitudFinal();
            airportDestination = false;
        }
        consumeService.ConsumerGoogle("geocode", latlng, "es");
    }

    private void Navigation(boolean state) {

        nn = false;
        ns = state;
        if (state) {
            item1 = new AHBottomNavigationItem(R.string.schedule, R.drawable.agendar, R.color.colorPrimary);
            item2 = new AHBottomNavigationItem(R.string.history, R.drawable.historial, R.color.colorPrimary);
            item3 = new AHBottomNavigationItem(R.string.travel, R.drawable.viajar, R.color.colorPrimary);
            item4 = new AHBottomNavigationItem(R.string.favorites, R.drawable.favoritos, R.color.colorPrimary);
            item5 = new AHBottomNavigationItem(R.string.profile, R.drawable.perfil, R.color.colorPrimary);
        } else {
            item1 = new AHBottomNavigationItem(R.string.call, R.drawable.llamar, R.color.colorPrimary);
            item2 = new AHBottomNavigationItem(R.string.message, R.drawable.chat, R.color.colorPrimary);
            item3 = new AHBottomNavigationItem(R.string.cancelar, R.drawable.cancelar, R.color.colorPrimary);
            item4 = new AHBottomNavigationItem(R.string.share, R.drawable.compartir, R.color.colorPrimary);
            item5 = new AHBottomNavigationItem(R.string.profile, R.drawable.perfil, R.color.colorPrimary);
        }

        navigation.addItem(item1);
        navigation.addItem(item2);
        navigation.addItem(item3);
        navigation.addItem(item4);
        navigation.addItem(item5);
        navigation.setCurrentItem(2);
        navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        navigation.setColored(true);
        navigation.setAccentColor(getResources().getColor(R.color.colorAccent));
        navigation.setInactiveColor(getResources().getColor(R.color.colorSecundaryText));
        nn = true;

    }

    private void Progress() {

        if (progress.isShowing()) {
            progress.dismiss();
        }
        progress.setCancelable(false);
        progress.setMessage(this.getString(R.string.app_name));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    private void AlertMessage(String message, final boolean finish) {

        if (progress.isShowing()) {
            progress.dismiss();
        }
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(this.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (finish) {
                    if (user.getTipopago().equals("final")) {
                        startActivity(new Intent(getApplicationContext(), QualifyView.class));
                        editor.putString("state", "qualification");
                        editor.commit();
                        finish();
                    }
                    startActivity(new Intent(getApplicationContext(), SplashView.class));
                    finish();
                }
                if (content1.getVisibility() == View.VISIBLE) {
                    UnLoadFragment();
                }

            }
        });
        alertDialog.create().show();

    }

    private boolean Validator() {

        /*if (destination.getText().toString().equals("Destino")) {
            AlertMessage(this.getString(R.string.destinationE), false);
            return false;
        } else {
            return true;
        }*/

//        if (service.getCiudad() == "" || service.getCiudad() == null || service.getCiudadOrigen() == "" || service.getCiudadOrigen() == null) {

        if (service.getDescOrigen() == "" || service.getDescOrigen() == null || service.getDescOrigen() == "Origen") {
            AlertMessage(this.getString(R.string.originM), false);
            return false;
        } else if (citySource == "" || citySource == null) {
            AlertMessage(this.getString(R.string.originP), false);
            return false;
        } else if (service.getDescDestino() == "" || service.getDescDestino() == null || service.getDescDestino() == "Destino") {
            AlertMessage(this.getString(R.string.destinationM), false);
            return false;
        } else if (cityDestination == "" || cityDestination == null) {
            AlertMessage(this.getString(R.string.destinationP), false);
            return false;
        } else {
            return true;
        }

    }

    private void Save() {

        gson = new Gson();
        String s = gson.toJson(service);
        editor.putString("Service", s);
        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User", u);
        editor.putString("state", state);
        editor.commit();

    }

    private void Restart() {

        editor.clear();
        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User", u);
        editor.putString("state", "start");
        editor.commit();
        navigation.removeAllItems();
        LoadState();
    }

    private void ToCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPhonePermission()) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service.getTelefonoConductor()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service.getTelefonoConductor()));
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            startActivity(intent);
        }
    }

    private void Chat() {

        if (chatF == null) {
            chatF = new ChatF();
        }
        LoadFragment(chatF);

    }

    private void History(String view) {
        Progress();
        consumeService.ConsumerService(view, user.getEmail());
        content1.setVisibility(View.VISIBLE);
        infotravel.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
        fragment.setVisibility(View.GONE);
        infouser.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    private void Travel(int type) {

        if (Validator()) {
            if (type == 1) {
                stateActivity = "schedule";
            } else if (type == 2) {
                stateActivity = "travel";
            }
            service.setPersonaEsperada(user.getFirstName() + " " + user.getLastName());
            service.setEmailPasajero(user.getEmail());
            service.setRegistrationIdPasajero(user.getRegistration_id());
            service.setTipoServicio(type);
            service.setidTipoServicio(type); // newid
            String source = service.getLatitudInicial() + " , " + service.getLongitudInicial();
            String destination = service.getLatitudFinal() + " , " + service.getLongitudFinal();
            Progress();
            consumeService.ConsumerGoogle("distance", source, destination);
        }

    }

    private void Cancel(boolean motivo) {

        if (motivo) {
            if (!state.equals("finish")) {
                final String[] cancel = getResources().getStringArray(R.array.cancel);
                alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.cancelS));
                alertDialog.setItems(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Progress();
                        request = new Request();
                        request.setUser(user);
                        request.setDescription(cancel[which]);
                        request.setServicio(service.getId());
                        request.setIdService(service.getId());
                        request.setReason(getResources().getString(R.string.hightime));
                        consumeService.ConsumerService("cancel", request);
                    }
                });
                alertDialog.setNeutralButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.create().show();
            } else {
                AlertMessage(getResources().getString(R.string.cancelE), false);
            }
        } else {
            request = new Request();
            request.setUser(user);
            request.setDescription(getResources().getString(R.string.finSearch));
            request.setReason(getResources().getString(R.string.hightime));
            consumeService.ConsumerService("cancel", request);
        }

    }

    private void Favorites() {

        request = new Request();
        request.setUser(user);
        final EditText input = new EditText(getApplicationContext());
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.ta));
        input.setFilters(fArray);
        input.setSingleLine(true);
        input.setTextColor(Color.parseColor("#000000"));
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(input);
        alertDialog.setTitle(this.getString(R.string.addfav));
        alertDialog.setPositiveButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = "";
                value = input.getText().toString();
                if (value.equals("")) {
                    request.setLocalization(localization);
                } else {
                    localization.setNombre(value);
                    request.setLocalization(localization);
                }
                Progress();
                consumeService.ConsumerService("addfavorite", request);
            }
        });
        alertDialog.setNegativeButton(this.getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();

    }

    private void Share() {

        String uri = "http://maps.google.com/maps?&z=10&q=" + myLatLng.latitude + "," + myLatLng.longitude + "&ll=" + myLatLng.latitude + "," + myLatLng.longitude;

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        share.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(share, getResources().getString(R.string.share)));

    }

    private void Profile() {

        ProfileF profileF = new ProfileF();
        LoadFragment(profileF);

    }

    private void Info() {
        String detailS = "#SERVICIO: " + service.getId() + "\n" +
                "Origen: " + service.getDescOrigen() + "\n" +
                "Destino: " + service.getDescDestino() + "\n" +
                "Distancia: " + service.getDistancia() + "\n" +
                "Vehiculo: " + service.getVehicle() + "\n" +
                "Valor: $" + formateador.format(service.getValor()) + "\n";
        if (service.getTipoServicio() == 7) {
            detailS = detailS + "Servicio por horas: " + service.getHoraRecogida() + "h";
        }
        AlertMessage(detailS, false);
    }

    private void Waze() {

        try {
            String url = "waze://?ll=" + service.getLatitudFinal() + "," + service.getLongitudFinal() + "&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }

    }

    private void State(Service serv) {

        service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
        switch (serv.getEstado()) {
            case "CANCELADO":
                if (service.getId() != null) {
                    Restart();
                    startActivity(new Intent(getApplicationContext(), SplashView.class));
                    finish();
                }
                break;
            case "ACEPTADO":
                service = serv;
                state = "arrived";
                Save();
                break;
            case "EN_TRAYECTO":
                service = serv;
                state = "finish";
                Save();
                break;
            case "TERMINADO":
                if (service.getId() != null) {
                    service = serv;
                    state = "start";
                    Save();
                }
                break;
            case "EN_ESPERA":
            case "EN_PROGRESO":
                int timer = TimerSearch();
                if (serv.getTipoServicio() == 2 || serv.getTipoServicio() == 7) {
                    if (TimerSearch() < 8) {
                        state = "search";
                        startActivity(new Intent(getApplicationContext(), SearchView.class).putExtra("timer", timer));
                        finish();
                    } else {
                        service = serv;
                        state = "start";
                        Cancel(false);
                    }
                }
                break;
        }

        if (!UnLoadFragment()) {
            navigation.removeAllItems();
            LoadState();
        }
    }

    private void Query() {
        editor.putBoolean("query", true);
        editor.commit();
        stateQuery = preferences.getBoolean("query", true);
        if (stateQuery) {
            editor.putBoolean("query", false);
            editor.commit();
            request = new Request();
            if (service.getId() == null) {
                request.setUser(user);
            } else {
                request.setService(service);
            }
            consumeService.ConsumerService("state", request);
        }
    }

    private void LoadFragment(Fragment frag) {

        fragment.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.GONE);
        infotravel.setVisibility(View.GONE);
        infouser.setVisibility(View.GONE);
        content1.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, frag);
        fragmentTransaction.commit();

    }

    private boolean UnLoadFragment() {
        if (fragment.getVisibility() == View.VISIBLE || content1.getVisibility() == View.VISIBLE) {
            title.setText(getResources().getString(R.string.app_name));
            navigation.setVisibility(View.VISIBLE);
            fragment.setVisibility(View.GONE);
            content1.setVisibility(View.GONE);
            navigation.removeAllItems();
            LoadState();
            return true;
        } else {
            return false;
        }
    }

    private int TimerSearch() {

        int res;
        Calendar calendario = new GregorianCalendar();
        long seg = calendario.getTimeInMillis() - preferences.getLong("timer", 0);
        if (seg < 240000) {
            res = (int) (seg / 30000);
            res = res + 1;
        } else {
            res = 8;
        }

        return res;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void checkGPS() {
        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(getResources().getString(R.string.gpsE));
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton(this.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.create().show();
        }
    }

    private boolean checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    protected synchronized void BuildGoogleClient() {
        googleClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleClient.connect();
    }

}
