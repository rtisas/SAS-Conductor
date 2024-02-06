package com.rti.sas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rti.sas.model.Card;
import com.rti.sas.R;
import com.rti.sas.service.ConsumeService;

import java.util.ArrayList;

public class CardA extends BaseAdapter {

    private ConsumeService consumeService;
    private ArrayList<Card> items;
    private Context context;

    public CardA(Context context,ArrayList<Card> items) {
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
        View view = inflater.inflate(R.layout.item_card, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView number = (TextView) view.findViewById(R.id.number);
        final ImageView delete = (ImageView) view.findViewById(R.id.delete);

        final Card card = items.get(position);

        name.setText(card.getDescTipo());
        number.setText("*******"+card.getNumero());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumeService.ConsumerService("deletecard",String.valueOf(card.getId()));
                delete.setVisibility(View.GONE);
            }
        });

        return view;

    }
}
