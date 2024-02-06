package com.rti.sas.global;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class GlobalApp extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
