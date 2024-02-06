package com.rti.sas.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rti.sas.model.Service;
import com.rti.sas.model.Vehicle;
import com.rti.sas.R;
import com.rti.sas.model.Request;
import com.rti.sas.service.ConsumeService;
import com.rti.sas.service.EndPoint;
import com.rti.sas.service.GeneratorService;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VehiclesA extends RecyclerView.Adapter<VehiclesA.VehiclesHolder> {

    private static final String PREF = "preferences";
    public static int select_item = 100;
    private EndPoint ep;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private ConsumeService consumeService;
    private Request request;
    private ArrayList<Vehicle> vehicles;
    private GeneratorService generator;
    private Context context;
    private Service service;
    private Gson gson;

    public VehiclesA (Context context){
        this.context = context;
        this.vehicles = new ArrayList<>();

        preferences = context.getSharedPreferences(PREF, context.MODE_PRIVATE);
        consumeService = new ConsumeService();
        editor = preferences.edit();
        service = new Service();
        gson = new Gson();
        service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
    }

    @Override
    public VehiclesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_vehicle, parent, false);
        return new VehiclesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VehiclesHolder holder, int position) {
        final Vehicle vehicle = vehicles.get(position);
        holder.setCar(vehicle.getUrl_img_out());
        holder.setType(vehicle.getType());
        holder.setId(vehicle.getId());
        holder.setVehicle(vehicle);
        holder.setPosition(position);
        holder.Select();

    }

    @Override
    public int getItemCount() {
        if (vehicles==null){
            return 1;
        }else{
            return vehicles.size();
        }

    }

    public void addAll(@NonNull ArrayList<Vehicle> vehicles) {

        this.vehicles.addAll(vehicles);
        notifyDataSetChanged();

    }


    public class VehiclesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int position;
        private Vehicle vehicle;
        private ImageView car;
        private TextView type;
        private String id;

        public VehiclesHolder(View itemView) {
            super(itemView);
            car = (ImageView) itemView.findViewById(R.id.car);
            type = (TextView) itemView.findViewById(R.id.type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            select_item = position;
            if (service.getidTipoServicio()!=1) {
                service.setFechaServicio(Date());
            }
            service.setVehicle(id);
            service.setidTipoVehiculo(service.getVehicle());
            request = new Request();
            request.setService(service);
            consumeService.ConsumerService("rate",request);
            car.setAlpha(1.0f);
            type.setTextColor(context.getResources().getColor((R.color.colorPrimary)));
        }

        private void setPosition (int position){
            this.position = position;
        }

        private void setId(String id) {
            this.id = id;
        }

        private void setType(String modelo) {
            type.setText(modelo);
        }

        private void setCar(String url) {
            String img = "";
            if(url!=null){
                img = url.replace(" ", "%20");
            }
            String server = generator.URL_SERVER;
            String s = server+"/"+ep.Ws+"" + img;
//            Picasso.with(context).load(s).into(car);
            Picasso.get().load(s).into(car);

        }

        private void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        private void Select() {
            if(select_item==position){
                request = new Request();
                vehicle.setIdTipoServicio(service.getTipoServicio());
                request.setTipoVehiculo(vehicle);
                consumeService.ConsumerService("models",request);
                car.setAlpha(1.0f);
                type.setTextColor(context.getResources().getColor((R.color.colorPrimary)));
            }else{
                car.setAlpha(0.5f);
                type.setTextColor(context.getResources().getColor((R.color.colorPrimaryText)));
            }
/*
            select_item = position;
            service.setFechaServicio(Date());
            service.setVehicle(id);
            service.setidTipoVehiculo(service.getVehicle());
            request = new Request();
            request.setService(service);
            consumeService.ConsumerService("rate",request);
            car.setAlpha(1.0f);
            type.setTextColor(context.getResources().getColor((R.color.colorPrimary)));
  */
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

    }

}
