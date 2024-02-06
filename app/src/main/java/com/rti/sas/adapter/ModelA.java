package com.rti.sas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.rti.sas.R;
import java.util.ArrayList;

public class ModelA extends BaseAdapter {

    private ArrayList<String> item;
    private Context context;

    public ModelA (Context context,ArrayList<String> item){
        this.context = context;
        this.item = item;
    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_model, null);

        TextView model = (TextView)view.findViewById(R.id.model);

        model.setText(item.get(position));

        return view;
    }

}
