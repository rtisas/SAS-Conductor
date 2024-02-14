package com.rtisas.sas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.rtisas.sas.R;
import com.rtisas.sas.model.Localization;
import com.rtisas.sas.service.ConsumeService;

import java.util.ArrayList;

public class FavoritesA extends BaseAdapter {

    private ConsumeService consumeService;
    private ArrayList<Localization> items;
    private Context context;

    public FavoritesA (Context context, ArrayList<Localization> items){
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

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_favorite,null);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        final ImageView delete = (ImageView) view.findViewById(R.id.delete);

        final Localization localization = items.get(position);

        title.setText(localization.getNombre());
        content.setText(localization.getDireccion());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumeService.ConsumerService("deletefavorite",localization.getId());
                delete.setVisibility(View.GONE);
            }
        });
        return view;

    }

}
