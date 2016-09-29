package com.wruzjan.ihg;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Locale locale = new Locale("pl");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    /**
     * add new form button
     */
    public void addNewForm(View view) {
        Intent intent = new Intent(this, ChooseWorkerActivity.class);
        startActivity(intent);
    }

    /**
     * browse addresses button
     */
    public void browseAdresses(View view) {
        Intent intent = new Intent(this, BrowseAddressesActivity.class);
        startActivity(intent);
    }

}
