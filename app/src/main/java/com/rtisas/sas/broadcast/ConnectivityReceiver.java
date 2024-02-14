package com.rtisas.sas.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    public ConnectivityManager cm;
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connectivityReceiverListener.onNetworkConnection(isConnected);
        }

    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnection(boolean isConnected);
    }

    public void setConnectivityReceiverListener (ConnectivityReceiverListener crl){
        connectivityReceiverListener = crl;
    }
}
