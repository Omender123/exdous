package com.example.exdous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.exdous.Helper.LocaleHelper;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        // Init Paper First
        Paper.init(this);

        //Default language is English

        String language=Paper.book().read("language");
        if (language==null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this,language);

        Resources resources =context.getResources();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }
*/
    }
}