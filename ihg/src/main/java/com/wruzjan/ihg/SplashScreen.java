package com.wruzjan.ihg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashScreen extends Activity {

    private static String TAG = SplashScreen.class.getName();
    // keep splash screen for some time
    private static long SLEEP_TIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.splash_screen);

        try {
            File file = new File(Environment.getExternalStorageDirectory()+"/IHG/LOG/", "log_"+new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())+".txt");
            Runtime.getRuntime().exec("logcat -d -v time -f " + file.getAbsolutePath());
            Log.d("IHG_DEBUG", "data logged at path: " + file.getAbsolutePath());
        }
        catch (IOException e){
            Log.d("IHG_DEBUG", "unable to log: " + e.getMessage());
        }

        // Start timer and launch main activity
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            // Start main activity
            Intent intent = new Intent(SplashScreen.this, BrowseAddressesActivity.class);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
    }

}
