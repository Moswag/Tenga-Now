package com.cytex.moswag.util;

import android.app.Application;
import android.content.Context;

public class ContextClass extends Application {
    private static Application instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }




}
