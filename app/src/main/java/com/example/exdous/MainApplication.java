package com.example.exdous;
import android.app.Application;
import android.content.Context;

import com.example.exdous.Helper.LocaleHelper;

public class MainApplication extends Application {
    // press ctrl+o

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}