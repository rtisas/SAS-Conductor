package com.rti.sas.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.rti.sas.adapter.CardA;
import com.rti.sas.adapter.HourA;
import com.rti.sas.adapter.ModelA;
import com.rti.sas.adapter.VehiclesA;
import com.rti.sas.model.Card;
import com.rti.sas.model.DetalleTarifa;
import com.rti.sas.model.EventService;
import com.rti.sas.model.Recargos;
import com.rti.sas.model.Request;
import com.rti.sas.model.Service;
import com.rti.sas.model.User;
import com.rti.sas.model.Vehicle;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.BuildConfig;
import com.rti.sas.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RequestServiceView extends AppCompatActivity implements View.OnClickListener {

    private static final String PREF = "preferences";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progress;
    private ConsumeService consumeService;
    private Request request;
    private TextView title,source,destination,creditT,businessT,digitalT,cashT,rate,detail,requestbutton;
    private LinearLayout creditbutton,businessbutton,digitalbutton,cashbutton,hourcontent,card,viewT,optional;
    private ImageView vehicleleft,vehicleright,creditI,businessI,digitalI,cashI;
    private RecyclerView vehiclelist;
    private RatingBar qualification;
    private Spinner model,hours;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Card> cards;
    private ListView history;
    private DetalleTarifa detailT;
    private Recargos surcharge;
    private VehiclesA adapter;
    private Vehicle vehicle;
    private Service service;
    private ModelA modelA;
    private HourA hourA;
    private CardA cardA;
    private User user;
    private Gson gson;
    private String state,selectM,selectH;
    private ArrayList<String> year;
    private int positionV=1,typepay=0;
    private DecimalFormat formateador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_service_view);

        preferences = this.getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        progress = new ProgressDialog(this);
        adapter = new VehiclesA(this);
        detailT = new DetalleTarifa();
        editor = preferences.edit();
        surcharge = new Recargos();
        vehicle = new Vehicle();
        service = new Service();
        user = new User();
        gson = new Gson();

        service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
        user = gson.fromJson(preferences.getString("User", "user"), User.class);
        state = preferences.getString("state", "start");

        businessbutton = (LinearLayout) findViewById(R.id.businessbutton);
        digitalbutton = (LinearLayout) findViewById(R.id.digitalbutton);
        creditbutton = (LinearLayout) findViewById(R.id.creditbutton);
        hourcontent = (LinearLayout) findViewById(R.id.hourcontent);
        cashbutton = (LinearLayout) findViewById(R.id.cashbutton);
        optional = (LinearLayout) findViewById(R.id.optional);
        viewT = (LinearLayout) findViewById(R.id.view);
        card = (LinearLayout) findViewById(R.id.card);
        vehicleright = (ImageView) findViewById(R.id.vehicleright);
        vehicleleft = (ImageView) findViewById(R.id.vehicleleft);
        businessI = (ImageView) findViewById(R.id.businessI);
        digitalI = (ImageView) findViewById(R.id.digitalI);
        creditI = (ImageView) findViewById(R.id.creditI);
        cashI = (ImageView) findViewById(R.id.cashI);
        requestbutton = (TextView) findViewById(R.id.requestbutton);
        destination = (TextView) findViewById(R.id.destination);
        businessT = (TextView) findViewById(R.id.businessT);
        digitalT = (TextView) findViewById(R.id.digitalT);
        creditT = (TextView) findViewById(R.id.creditT);
        source = (TextView) findViewById(R.id.source);
        detail = (TextView) findViewById(R.id.detail);
        title = (TextView) findViewById(R.id.title);
        cashT = (TextView) findViewById(R.id.cashT);
        hours = (Spinner) findViewById(R.id.hours);
        model = (Spinner) findViewById(R.id.model);
        rate = (TextView) findViewById(R.id.rate);
        qualification = (RatingBar) findViewById(R.id.qualification);
        vehiclelist = (RecyclerView) findViewById(R.id.vehiclelist);
        history = (ListView) findViewById(R.id.history);
        formateador = new DecimalFormat("###,###.##");

        businessbutton.setOnClickListener(this);
        digitalbutton.setOnClickListener(this);
        requestbutton.setOnClickListener(this);
        creditbutton.setOnClickListener(this);
        vehicleright.setOnClickListener(this);
        vehicleleft.setOnClickListener(this);
        cashbutton.setOnClickListener(this);
        detail.setOnClickListener(this);

        hourA = new HourA(this, 24);
        hours.setAdapter(hourA);
        hourA = new HourA(this, 2010);
        //model.setAdapter(hourA);
        hourA = new HourA(this, 2010);

        if (user.getTipouser().equals("especial")) {
            optional.setVisibility(View.GONE);
        }

        card.setVisibility(View.GONE);
        detail.setVisibility(View.GONE);
        viewT.setVisibility(View.VISIBLE);
        source.setText(service.getDescOrigen());
        destination.setText(service.getDescDestino());
        // por defecto se deja tipo de calificacion 1 y tipo de pago 4 (En efectivo)
        qualification.setRating(1);
        Paying(4);
        if (service.getTipoServicio() == 1) {
            requestbutton.setText(getResources().getString(R.string.scheduleT));
            hourcontent.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.scheduleT));
        } else if (service.getTipoServicio() == 2) {
            requestbutton.setText(getResources().getString(R.string.travelT));
            hourcontent.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.travelT));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vehiclelist.setLayoutManager(layoutManager);
        vehiclelist.setAdapter(adapter);
        getVehicles();

        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card card1 = cards.get(position);
                service.setTarjeta(String.valueOf(card1.getId()));
                viewT.setVisibility(View.VISIBLE);
                card.setVisibility(View.GONE);
                creditI.setColorFilter(getApplication().getResources().getColor(R.color.colorPrimary));
                creditT.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                service.setTipoPago(String.valueOf(3));
            }
        });

        qualification.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (vehicle.getQualification() != null) {
                    if (rating > Float.valueOf(vehicle.getQualification())) {
                        AlertMessage(getResources().getString(R.string.qualificationE), false);
                        qualification.setRating(Float.valueOf(vehicle.getQualification()));
                    }
                }
            }
        });

        model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectM = (String) parent.getItemAtPosition(position);
                service.setModeloVehiculo(selectM);
                if (year != null) {
                    consumeService = new ConsumeService();
                    ArrayList<String> modelos = new ArrayList<>();
                    modelos.add(0, selectM);
                    vehicle.setModelos(modelos);
                    //vehicle.setId(String.valueOf(detailT.getIdTipoVehiculo()));
                    vehicle.setId(String.valueOf(service.getVehicle()));
                    vehicle.setIdTipoServicio(service.getTipoServicio());
                    request = new Request();
                    request.setTipoVehiculo(vehicle);
                    //Progress();
                    //consumeService.ConsumerService("qualification", request);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectH = (String) parent.getItemAtPosition(position);
                if (detail.getVisibility() == View.VISIBLE) {
                    if (selectH.equals("0")) {
                        service.setTipoServicio(2);
                        service.setidTipoServicio(2); // newid
                    } else {
                        service.setTipoServicio(7);
                        service.setidTipoServicio(7); // newid
                    }
                    service.setHoraRecogida(Integer.parseInt(selectH));
                    request = new Request();
                    request.setService(service);
                    Progress();
                    consumeService.ConsumerService("rateh", request);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.requestbutton:
                if(Validate()){
                    Query();
                }else{

                }
                break;
            case R.id.vehicleright:
                VehiclesMove(true);
                break;
            case R.id.vehicleleft:
                VehiclesMove(false);
                break;
            case R.id.creditbutton:
                Paying(3);
                break;
            case R.id.businessbutton:
                Paying(2);
                break;
            case R.id.digitalbutton:
                Paying(1);
                break;
            case R.id.cashbutton:
                Paying(4);
                break;
            case R.id.detail:
                DetailT();
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

    @Override
    public void onBackPressed() {
        if(card.getVisibility()==View.VISIBLE){
            card.setVisibility(View.GONE);
            viewT.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventService event) {

        if(progress.isShowing()){
            progress.dismiss();
        }
        if(event.getAction().equals("vehicle")){
            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                vehicles = event.getResponse().getVehicles();
                adapter.addAll(event.getResponse().getVehicles());
                adapter.select_item = 100;
                vehiclelist.getAdapter().notifyDataSetChanged();
                consumeService = new ConsumeService();
//                consumeService.ConsumerService("afes",service.getEmailPasajero());
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),false);
            }
        }else if(event.getAction().equals("rate")||event.getAction().equals("rateh")){

            if(event.getResponse().getFunctionalMessage().getCode().equals("F-0000")){
                //surcharge = event.getResponse().getDetalleTarifa().getRecargos();
                //detailT = event.getResponse().getDetalleTarifa();
                detail.setVisibility(View.VISIBLE);
                //service.setDetalleTarifa(detailT);
                service.setVehicle(String.valueOf(service.getidTipoVehiculo())); //detailT.getIdTipoVehiculo()));
                //service.setidTipoVehiculo(String.valueOf(detailT.getIdTipoVehiculo())); // problemavehiculo
                service.setidTipoVehiculo(service.getVehicle());
                service.setValor((int)event.getResponse().getValor());
                if(service.getTipoServicio()==2||service.getTipoServicio()==7){
                    service.setFechaServicio(Date());
                }

                // Cuenta Empresa por defecto
                if(service.getValor()<user.getEnterpriseCredits()){
                    businessI.setColorFilter(this.getResources().getColor(R.color.colorPrimary));
                    businessT.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                    service.setTipoPago(String.valueOf(2));
                    digitalI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
                    digitalT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
                    creditI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
                    creditT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
                    cashI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
                    cashT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
                    typepay=2;
                }
                //
                rate.setText("$ "+formateador.format(service.getValor()));
                if(event.getAction().equals("rate")){
                    vehiclelist.getAdapter().notifyDataSetChanged();
                    hours.setSelection(0);
                }else{
                    if(progress.isShowing()){
                        progress.dismiss();
                    }
                }
            }else{
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(),false);
                hours.setSelection(0);
                //rate.setText("");
            }
        }else if(event.getAction().equals("models")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                if (event.getResponse().getTypeVehicle().getModelos().get(0).equals("0")) {
                    AlertMessage(getResources().getString(R.string.requestE), false);
                    hourA = new HourA(this, 2010);
                    model.setAdapter(hourA);
                    year = null;
                } else {
                    year = event.getResponse().getTypeVehicle().getModelos();
                    modelA = new ModelA(RequestServiceView.this, event.getResponse().getTypeVehicle().getModelos());
                    model.setAdapter(modelA);
                }
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }else if(event.getAction().equals("qualification")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                vehicle.setQualification(event.getResponse().getTypeVehicle().getQualification());
                qualification.setRating(1);
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }else if(event.getAction().equals("requestservice")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                if(service.getTipoServicio()==2||service.getTipoServicio()==7){
                    startActivity(new Intent(getApplicationContext(),SearchView.class));
                    editor.putString("state","search").commit();
                }
                finish();
            } else {
                AlertMessage(event.getResponse().getFunctionalMessage().getMessage(), false);
            }
        }else if(event.getAction().equals("getCards")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                cards = event.getResponse().getCards();
                cardA = new CardA(getApplicationContext(), cards);
                history.setAdapter(cardA);
                viewT.setVisibility(View.GONE);
                card.setVisibility(View.VISIBLE);

            }
        }else if(event.getAction().equals("state")) {
            if (event.getResponse().getFunctionalMessage().getCode().equals("F-0000")) {
                Service ser = event.getResponse().getServicio();
                if(ser.getTipoServicio()==2||ser.getTipoServicio()==7){
                    if(ser.getEstado().equals("CANCELADO")||ser.getEstado().equals("TERMINADO")){

                        RequestService();
                    }
                }else{
                    if(!ser.getEstado().equals("EN_TRAYECTO")||!ser.getEstado().equals("ACEPTADO")){
                        RequestService();
                    }
                }
            }else{
                RequestService();
            }
        }else if(event.getAction().equals("error")) {
            AlertMessage(event.getResult(),false);
        }

    }

    /* SOLUCION CERRAR SESION AL RECIBIR NOTIFICACION SIN ESTAR CONECTADO
    private void Restart() {

        editor.clear();
        gson = new Gson();
        User user1 = new User();
        user1.setRegistration_id(user.getRegistration_id());
        String u = gson.toJson(user1);
        editor.putString("User", u);
        editor.putString("state", "start");
        editor.commit();
    }

    Restart();
    startActivity(new Intent(getActivity().getApplicationContext(), LoginView.class));
    getActivity().finish();
    */

    private void getVehicles () {

        Progress();


       /* if (service.getCiudad() == "" || service.getCiudad() == null || service.getCiudadOrigen() == "" || service.getCiudadOrigen() == null) {
            AlertMessage(this.getString(R.string.originM), false);
        } else if (service.getCiudadDestino() == "" || service.getCiudadDestino() == null) {
            AlertMessage(this.getString(R.string.destinationM), false);
        } else {*/
            request = new Request();
            request.setService(service);
            consumeService.ConsumerService("vehicle", request);
        //}

    }

    private void RequestService() {

        request = new Request();
        request.setUser(user);
        final EditText input = new EditText(getApplicationContext());
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.taa));
        input.setFilters(fArray);
        input.setSingleLine(false);
        input.setTextColor(Color.parseColor("#000000"));

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(input);
        alertDialog.setTitle(this.getString(R.string.addinfo));
        alertDialog.setMessage(this.getString(R.string.addinfo2));
        alertDialog.setPositiveButton(this.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = "";
                if(service.getId() != null){
                    service.setId(null);
                }
                value = input.getText().toString();
                service.setObservaciones(value);
                service.setCalificacionConductor(""+qualification.getRating());
                service.setAppVersionPassanger(BuildConfig.VERSION_NAME);
                request = new Request();
                request.setService(service);
                if(service.getTipoServicio()==1){
                    state = "start";
                }else{
                    state = "request";
                }
                Save();
                Progress();
                service.setVehicle(String.valueOf(1));
                //service.setVehicle(service.getidTipoVehiculo());
                consumeService.ConsumerService("requestservice",request);
            }
        });
        alertDialog.create().show();

    }

    private void Query (){

        request = new Request();
        if(service.getId()==null){
            request.setUser(user);
        }else{
            request.setService(service);
        }
        consumeService.ConsumerService("state", request);

    }

    private void VehiclesMove(boolean direction) {

        if(vehicles!=null){
            if(direction&&positionV<vehicles.size()-1){
                positionV=positionV+1;
                vehiclelist.scrollToPosition(positionV);
            }else if(!direction&&positionV>1){
                positionV=positionV-1;
                vehiclelist.scrollToPosition(positionV);
            }
        }
    }

    private void Paying(int mode) {

        service.setTipoPago(String.valueOf(mode));
        businessI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
        businessT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
        digitalI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
        digitalT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
        creditI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
        creditT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
        cashI.setColorFilter(this.getResources().getColor(R.color.colorPrimaryText));
        cashT.setTextColor(this.getResources().getColor(R.color.colorPrimaryText));
        typepay=mode;
        Log.e("fondos","cre "+user.getCredits()+"empre"+user.getEnterpriseCredits());
        switch (mode){
            case 1:
                if(service.getValor()<user.getCredits()){
                    digitalI.setColorFilter(this.getResources().getColor(R.color.colorPrimary));
                    digitalT.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                    service.setTipoPago(String.valueOf(mode));
                    break;
                }else{
                    Toast.makeText(this,getResources().getString(R.string.insufficient),Toast.LENGTH_LONG).show();
                    break;
                }
            case 2:
                if(service.getValor()<user.getEnterpriseCredits()){
                    businessI.setColorFilter(this.getResources().getColor(R.color.colorPrimary));
                    businessT.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                    service.setTipoPago(String.valueOf(mode));
                    break;
                }else{
                    Toast.makeText(this,getResources().getString(R.string.insufficient),Toast.LENGTH_LONG).show();
                    break;
                }
            case 3:
                Progress();
                consumeService.ConsumerService("getCards",user.getEmail());
                break;
            case 4:
                cashI.setColorFilter(this.getResources().getColor(R.color.colorPrimary));
                cashT.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                service.setTipoPago(String.valueOf(mode));
                break;

        }
    }

    private void DetailT() {
        if(service.getValor()!=0){
        String base="";
        String Arranque="";
        String Kms="";
        String Distancia="";
        String Dia="";
        String Hora="";
        String Minuto="";
        String Recargo="";
        String Total="";
        String Tiempo="";
        if(detailT.getValorBase()!=0){base = "Base: $"+formateador.format(detailT.getValorBase())+"\n";}
        if(detailT.getValorArranque()!=0){Arranque = "Arranque: $"+formateador.format((detailT.getValorArranque()))+"\n";}
        if(detailT.getValorKilometro()!=0){Kms = "Kms: $"+formateador.format((detailT.getValorKilometro()))+"\n";}
        //if(detailT.getTiempoEstimado()!=0){Distancia = "Distancia: "+(detailT.getDistancia())+"km\n";
        if(service.getTiempoEstimado()!=0){Distancia = "Distancia: "+(service.getDistancia())+"km\n";}
        if(detailT.getValorDia()!=0){Dia = "dia: $"+formateador.format((detailT.getValorDia()))+"\n";}
        if(detailT.getValorHora()!=0){Hora = "Hora: $"+formateador.format((detailT.getValorHora()))+"\n";}
        if(detailT.getValorMinuto()!=0){Minuto = "Minuto: $"+formateador.format((detailT.getValorMinuto()))+"\n";}
        if(surcharge.getTotalRecargo()!=0){Recargo = "Recargo: $"+formateador.format((surcharge.getTotalRecargo()))+"\n";}
        if(service.getValor()!=0){Total = "Total: $"+formateador.format((service.getValor()))+"\n";}
        //if(detailT.getTiempoEstimado()!=0){Tiempo = "Tiempo estimado de viaje: "+(detailT.getTiempoEstimado())+" minutos\n";}
        if(service.getTiempoEstimado()!=0){Tiempo = "Tiempo estimado de viaje: "+(service.getTiempoEstimado())+" minutos\n";}
        String detailS = base+Arranque+Kms+Distancia+Dia+Hora+Minuto+Recargo+Total+Tiempo;
        AlertMessage(detailS,false);
        // Corrección crash "ver detalle"
        } else {AlertMessage("Total: $0",false);}

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
                    startActivity(new Intent(getApplicationContext(),MapView.class));
                    finish();
                }
            }
        });
        alertDialog.create().show();

    }

    private boolean Validate() {

        if(typepay==0||rate.getText().toString().length()<=1){
            if(typepay==0){
                AlertMessage(getResources().getString(R.string.formaDePago), false);
            }else{
                AlertMessage(getResources().getString(R.string.fieldsE),false);
            }
            return false;
        }else if(!user.getTipouser().equals("especial")){
            if(service.getModeloVehiculo().equals("----")){
                AlertMessage(getResources().getString(R.string.requestE), false);
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }

    }

    public String Date() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        DecimalFormat mFormat = new DecimalFormat("00");
        String fechaActual = String.valueOf(mFormat.format(Double.valueOf(mDay)) + "-" + mFormat.format(Double.valueOf(mMonth + 1)) + "-" + mYear + " " + mFormat.format(Double.valueOf(hora)) + ":" + mFormat.format(Double.valueOf(min)));
        return fechaActual;
    }

    private void Save() {

        gson = new Gson();
        String s = gson.toJson(service);
        editor.putString("Service",s);
        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User",u);
        editor.putString("state",state);
        editor.commit();

    }

}
