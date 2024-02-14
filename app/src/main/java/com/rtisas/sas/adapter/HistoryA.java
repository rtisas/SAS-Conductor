package com.rtisas.sas.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rtisas.sas.model.Service;
import com.rtisas.sas.model.User;
import com.rtisas.sas.R;
import com.rtisas.sas.model.Request;
import com.rtisas.sas.service.ConsumeService;
import com.rtisas.sas.view.PqrsView;

import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class HistoryA extends BaseAdapter{

    private static final String PREF = "preferences";
    private SharedPreferences preferences;
    private ConsumeService consumeService;
    private Request request;
    private ArrayList<Service> items;
    private Context context;
    private User user;
    private Gson gson;

    public HistoryA(Context context,ArrayList<Service> items) {
        preferences = context.getSharedPreferences(PREF, MODE_PRIVATE);
        consumeService = new ConsumeService();
        user = new User();
        gson = new Gson();
        user = gson.fromJson(preferences.getString("User", "user"), User.class);

        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_history,null);

        final TextView cancel = (TextView)view.findViewById(R.id.cancel);
        TextView destination = (TextView)view.findViewById(R.id.destination);
        TextView idservice = (TextView)view.findViewById(R.id.idservice);
        final ImageView received = (ImageView)view.findViewById(R.id.received);
        TextView pending = (TextView)view.findViewById(R.id.pending);
        TextView source = (TextView)view.findViewById(R.id.source);
        TextView date = (TextView)view.findViewById(R.id.date);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView conductor = (TextView)view.findViewById(R.id.conductor);

        received.setColorFilter(context.getResources().getColor(R.color.colorPrimaryText));
        date.setText(items.get(position).getFechaInicial()+"      Valor: "+items.get(position).getValor());
        //name.setText("Vehiculo: "+items.get(position).getVehicle());
        name.setText("Tel: "+items.get(position).getTelefonoConductor()+" - Placa: "+items.get(position).getPlaca());
        destination.setText(items.get(position).getDescDestino());
        source.setText(items.get(position).getDescOrigen());
        idservice.setText(items.get(position).getId());
        conductor.setText("Conductor: "+items.get(position).getNombreConductor()+" "+items.get(position).getApellidoConductor());
        destination.setVisibility(View.VISIBLE);
        pending.setVisibility(View.VISIBLE);
        source.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        pending.setText("PQRS");

        if(items.get(position).getEstado().equals("TERMINADO")){
            if(items.get(position).getTipoServicio()==2){
                received.setImageResource(R.drawable.history);
                //name.setText("Tipo servicio: "+context.getResources().getString(R.string.type2));
            }else{
                received.setImageResource(R.drawable.reserva);
                //name.setText("Tipo servicio: "+context.getResources().getString(R.string.type1));
            }

        }else{
            received.setImageResource(R.drawable.reserva);
            cancel.setVisibility(View.VISIBLE);
            /*if(items.get(position).getNombreConductor()!=null){
                name.setText(items.get(position).getNombreConductor()+" "+items.get(position).getApellidoConductor());
            }else{
                name.setText(context.getResources().getString(R.string.driverE));
            }*/
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = new Request();
                request.setUser(user);
                request.setDescription("");
                request.setServicio(items.get(position).getId());
                request.setIdService(items.get(position).getId());
                request.setReason(context.getResources().getString(R.string.hightime));
                consumeService.ConsumerService("cancel1",request);
                cancel.setVisibility(View.GONE);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PqrsView.class).putExtra("idservice",items.get(position).getId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return view;

    }

}
