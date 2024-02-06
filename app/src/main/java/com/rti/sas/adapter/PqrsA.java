package com.rti.sas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rti.sas.model.Pqrs;
import com.rti.sas.R;
import com.rti.sas.service.ConsumeService;

import java.util.ArrayList;

public class PqrsA extends BaseAdapter {

    private ConsumeService consumeService;
    private ArrayList<Pqrs> items;
    private Context context;

    public PqrsA (Context context,ArrayList<Pqrs> items){

        consumeService = new ConsumeService();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_pqrs, null);

        TextView dateini = (TextView)view.findViewById(R.id.dateini);
        TextView datefin = (TextView)view.findViewById(R.id.datefin);
        TextView idpqrs = (TextView)view.findViewById(R.id.idpqrs);
        TextView result = (TextView)view.findViewById(R.id.result);
        TextView name = (TextView)view.findViewById(R.id.name);

        result.setText(items.get(position).getRespuestaSolicitud());
        dateini.setText(items.get(position).getFechaSolicitud());
        datefin.setText(items.get(position).getFechaRespuesta());
        name.setText(items.get(position).getUsuarioRespuesta());
        idpqrs.setText(items.get(position).getId());

        return view;
    }

}
