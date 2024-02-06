package com.rti.sas.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.gson.Gson;
import com.rti.sas.model.Service;
import com.rti.sas.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleView extends AppCompatActivity implements View.OnClickListener{

    private static final String PREF = "preferences";
    private AlertDialog.Builder alertDialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private TextView schedulebutton,source,destination;
    private EditText date,hour;
    //private Switch day;
    private Service service;
    private Gson gson;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_view);

        preferences = this.getSharedPreferences(PREF, MODE_PRIVATE);
        editor = preferences.edit();
        service = new Service();

        gson = new Gson();

        service = gson.fromJson(preferences.getString("Service","service"), Service.class);
        state = preferences.getString("state","start");

        schedulebutton = (TextView)findViewById(R.id.schedulebutton);
        destination = (TextView)findViewById(R.id.destination);
        source = (TextView)findViewById(R.id.source);
        date = (EditText)findViewById(R.id.date);
        hour = (EditText)findViewById(R.id.hour);
        //day = (Switch)findViewById(R.id.day);

        schedulebutton.setOnClickListener(this);
        date.setOnClickListener(this);
        hour.setOnClickListener(this);

        source.setText(service.getDescOrigen());
        destination.setText(service.getDescDestino());

    }

    @Override
    public void onClick(View view) {

        final DecimalFormat df = new DecimalFormat("00");
        Calendar c = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.date:
                date.setError(null);
                final int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                int mYear = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear<9 && dayOfMonth<10) {
                            date.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        }else if (monthOfYear>=9 && dayOfMonth<10) {
                            date.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }else if (monthOfYear<9 && dayOfMonth>=10) {
                            date.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        }else if (monthOfYear>=9 && dayOfMonth>=10) {
                            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.hour:
                hour.setError(null);
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hrs = hourOfDay;
                        int min = minute;
                        String time = String.valueOf(df.format(hrs) + ":" + df.format(min));
                        hour.setText(time);
                    }
                }, mHour, mMinute, false);

                timePickerDialog.show();
                break;
            case R.id.schedulebutton:
                if (Validate()) {
                    //service.setReservaDia(day.isChecked());
                    service.setFechaServicio(date.getText().toString()+" "+hour.getText().toString());
                    service.setFechaInicial(service.getFechaServicio());
                    Save();
                    startActivity(new Intent(getApplicationContext(),RequestServiceView.class));
                    finish();
                }
                break;
        }

    }

    private boolean Validate() {

        boolean result = false;
        boolean fechaok = date.getText().toString().trim().length()!=0;
        boolean horaok = hour.getText().toString().trim().length()!=0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Calendar calendario = new GregorianCalendar();
        Date datesystem = calendario.getTime();

        try {
            Date dateservice = dateFormat.parse(date.getText().toString()+" "+hour.getText().toString());
            calendario = Calendar.getInstance();
            calendario.setTime(dateservice);
            calendario.add(Calendar.HOUR, -0);
            dateservice = calendario.getTime();
            result=true;

            if(datesystem.after(dateservice)){
                AlertMessage(getResources().getString(R.string.advanceE));
                hour.setError(getResources().getString(R.string.advanceE));
                result=false;
            }

        } catch (ParseException e) {
            AlertMessage(getResources().getString(R.string.dateE));

            e.printStackTrace();
        }

        if(!(fechaok && horaok)){
            AlertMessage(getResources().getString(R.string.fieldsE));
        }

        return result;

    }

    private void AlertMessage (String message) {

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(this.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alertDialog.create().show();

    }

    private void Save() {

        gson = new Gson();
        String s = gson.toJson(service);
        editor.putString("Service",s);
        editor.putString("state",state);
        editor.commit();

    }
}
