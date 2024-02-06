package com.rti.sas.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.rti.sas.model.Chat;
import com.rti.sas.model.Chats;
import com.rti.sas.model.Service;
import com.rti.sas.model.Ubication;
import com.rti.sas.model.User;
import com.rti.sas.R;
import com.rti.sas.view.MapView;
import com.rti.sas.view.SearchView;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

public class CloudMessaging extends FirebaseMessagingService {

    private static final String TAG = "CloudMessagingService";

    public static final String PREF = "preferences";
    private NotificationManager notificationManager;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String type,title,content;
    private Service service;
    private User user;
    private Gson gson;
    private Runnable runnable;
    private Handler handler;
    private String mensaje,state,billetera,creditoEmp;



    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        preferences = getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        editor = preferences.edit();
        service = new Service();
        user = new User();
        gson = new Gson();
        if(!preferences.getString("Service", "service").equals("service")){
            service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
        }
        user = gson.fromJson(preferences.getString("User", "user"), User.class);
        state = preferences.getString("state","start");

        Map data = message.getData();
        //Log.e("notificacion", message.toString());
        //Log.e("notificacion",data.get("notification_type").toString());
        //Log.e("notificacion",data.get("title").toString());
        //Log.e("notificacion",data.get("message").toString());

        type = data.get("notification_type").toString();
        content = data.get("message").toString();
        title = data.get("title").toString();

        if(!type.equals("0013")){
            ValidateMessage();
        }

        if(ValidateState(Integer.parseInt(type))){
            switch (type){
                case "0003"://se asigna conductor
                    state = "arrived";
                    Save();
                    Notification(true);
                    SearchView.End();
                    break;
                case "0006"://servicio cancelado desde conductor
                    Notification(true);
                    Restart();
                    break;
                case "0007"://calificar
                    if(ValidateData()){
                        state = "qualification";
                        Save();
                    }
                    Notification(true);
                    break;
                case "0008"://su conductor ha llegado
                    if(ValidateData()){
                        state = "initiate";
                        editor.putString("state",state);
                        editor.commit();
                    }
                    Notification(true);
                    break;
                case "0009"://respuesta de las acciones, confirmación agendamiento web
                    Notification(true);
                    break;
                case "0011"://chat, asignacion de conductores desde la web
                    ChatMessager();
                    Notification(true);
                    break;
                case "0014"://pago ejecutado con exito
                    if(!mensaje.equals("Pago rechazado")) {
                        if(ValidateData()){
                            state = "finish";
                        }
                        user.setEnterpriseCredits(Integer.parseInt(creditoEmp));
                        user.setCredits(Integer.parseInt(billetera));
                        String u = gson.toJson(user);
                        editor.putString("User",u);
                        editor.putString("state", state);
                        editor.commit();
                        Notification(true);
                    }else{
                        Notification(true);
                    }
                    break;
                case "0016"://inicio del servicio
                    state = "arrived";
                    Save();
                    Notification(true);
                    break;
                case "0018"://locaclizacion del conductor
                    UbicationDriver();
                    break;
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
      //  Log.d(TAG, "Refreshed token: " + token);
        preferences = getApplicationContext().getSharedPreferences(PREF,MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        user = new User();
        user.setRegistration_id(token);
        String usuario = gson.toJson(user);
        editor.putString("User", usuario);
        editor.putString("firebaseToken", token);
        editor.commit();
    }

    private void Notification(boolean jump) {

        Intent intent = new Intent(getApplicationContext(), MapView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title",title);
        intent.putExtra("type",type);
        intent.putExtra("opt",mensaje);
        if(jump){
            startActivity(intent);
        }
        PendingIntent intentN = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), MapView.class), PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String Channel_ID = "my_channel_01"; //new
        String Channel_Name = "Channel Name"; //new
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(Channel_ID, Channel_Name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, Channel_ID)
                .setSmallIcon(R.mipmap.notify)
                .setContentText(title)
                .setAutoCancel(true)
                .setPriority(0)
                .setSound(sound)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setContentIntent(intentN);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        notification.setContentIntent(resultPendingIntent);

        notificationManager.notify(Integer.parseInt("0"), notification.build());

    }

    private void ValidateMessage() {

        JSONObject object;
        String opt;
        mensaje = content;
        try {
            object = new JSONObject(content);
            opt = object.optString("servicio");
            long te = service.getTiempoEstimado();
            int hr = service.getHoraRecogida();
            service = gson.fromJson(opt,Service.class);
            mensaje = opt;
            if(service == null){
                service = gson.fromJson(preferences.getString("Service", "service"), Service.class);
                try {
                    object = new JSONObject(content);
                    mensaje = object.optString("mensaje");
                    billetera = object.optString("billetera");
                    creditoEmp = object.optString("creditoEmp");
                } catch (JSONException i) {
                    i.printStackTrace();
                }
            }else{
                service.setTiempoEstimado(te);
                service.setHoraRecogida(hr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                object = new JSONObject(content);
                mensaje = object.optString("mensaje");
            } catch (JSONException i) {
                i.printStackTrace();
            }
        }
    }

    private void UbicationDriver() {
        JSONObject object;
        String opt;
        try {
            object = new JSONObject(content);
            opt = object.optString("ubicacion");
            Ubication ubication = gson.fromJson(opt,Ubication.class);
            String u = gson.toJson(ubication);
            editor.putString("Ubication",u);
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ChatMessager() {

        String c = preferences.getString("Chat","chat");
        Chats chats = new Chats();
        Chat chat = new Chat();
        if(!c.equals("chat")){
            chats = gson.fromJson(c,Chats.class);
        }
        chat.setPropio(false);
        chat.setMensaje(content);
        chat.setFecha(Date());
        chats.addChat(chat);
        c = gson.toJson(chats);
        editor.putString("Chat",c);
        editor.commit();

    }

    private void Save() {

        gson = new Gson();
        String s = gson.toJson(service);
        Log.e("recibe",s);
        editor.putString("Service",s);
        editor.putString("state",state);
        editor.commit();

    }

    private void Restart() {

        editor.clear();
        gson = new Gson();
        String u = gson.toJson(user);
        editor.putString("User",u);
        editor.putString("state","start");
        editor.commit();

    }

    public String Date() {
        Calendar calendar = Calendar.getInstance();
        /*int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);*/
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        DecimalFormat mFormat = new DecimalFormat("00");
        String fechaActual = String.valueOf(mFormat.format(Double.valueOf(hora)) + ":" + mFormat.format(Double.valueOf(min)));
        return fechaActual;
    }

    private Boolean ValidateState(int stateN){
        boolean res = true;
        switch (state){
            case "qualification":
                res = false;
                break;
            case "finish":
                if(stateN==8||stateN==3||stateN==16){
                    res = false;
                }
                break;
            case "initiate":
                if(stateN==3||stateN==16){
                    res = false;
                }
                break;
        }
        return res;
    }

    private boolean ValidateData(){
        boolean res = true;
        if(service.getId()==null){
            res = false;
        }
        return res;
    }

}

    /*private void Logout() {

        if (!state.equals("arrived") && !state.equals("initiate") && !state.equals("finish")) {
            state = "disconnected";
            Restart();
            startActivity(new Intent(getApplicationContext(), LoginView.class));
        }
    };*/