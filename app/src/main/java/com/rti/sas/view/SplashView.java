package com.rti.sas.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.rti.sas.model.User;
import com.rti.sas.R;

public class SplashView extends AppCompatActivity {

    private static final String PREF = "preferences";
    //public static String Google_Service;
    private SharedPreferences preferences;
    private AlertDialog.Builder alertDialog;
    private User user;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view);
        user = new User();
        gson = new Gson();
        //Google_Service = getResources().getString(R.string.Google_Service);

        preferences = getApplicationContext().getSharedPreferences(PREF, MODE_PRIVATE);
        String jUser = preferences.getString("User", "user");
        if (!jUser.equals("user")) {
            user = gson.fromJson(jUser,User.class);
            if (user.getEmail() != null) {
                Intent intent = new Intent(getApplicationContext(), MapView.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(intent);
                finish();
            }
        } else {
            Loading();
        }

    }

    private void Loading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                String jUser = preferences.getString("User", "user");
                if (!jUser.equals("user")) {
                    user = gson.fromJson(jUser, User.class);
                    Intent intent = new Intent(getApplicationContext(), LoginView.class);
                    startActivity(intent);
                    finish();
                } else {
                    AlertMessage(getApplicationContext().getString(R.string.internetE));
                }
            }
        }, 7000);
    }

    private void AlertMessage(String message) {

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getApplicationContext().getString(R.string.app_name));
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getApplicationContext().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        alertDialog.create().show();

    }
}
