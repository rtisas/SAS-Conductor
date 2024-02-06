package com.rti.sas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rti.sas.model.Chats;
import com.rti.sas.R;

public class ChatA extends BaseAdapter {

    private Context context;
    private Chats chats;

    public ChatA(Context context,Chats chats){
        this.context = context;
        this.chats = chats;
    }

    @Override
    public int getCount() {
        return chats.getChats().size();
    }

    @Override
    public Object getItem(int position) {
        return chats.getChats().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_chat,null);

        ImageView received = (ImageView)view.findViewById(R.id.received);
        ImageView send = (ImageView)view.findViewById(R.id.sent);
        TextView text = (TextView)view.findViewById(R.id.text);
        TextView date = (TextView)view.findViewById(R.id.date);

        if(chats.getChats().get(position).isPropio()){
            received.setVisibility(View.GONE);
            send.setVisibility(View.VISIBLE);
            text.setText(chats.getChats().get(position).getMensaje());
            date.setText(chats.getChats().get(position).getFecha());
        }else{
            received.setVisibility(View.VISIBLE);
            send.setVisibility(View.GONE);
            text.setText(chats.getChats().get(position).getMensaje());
            date.setText(chats.getChats().get(position).getFecha());
        }

        return view;
    }
}
