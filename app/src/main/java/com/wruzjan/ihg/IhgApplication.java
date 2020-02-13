package com.wruzjan.ihg;

import com.wruzjan.ihg.utils.reporting.CrashReporter;

import androidx.multidex.MultiDexApplication;

public class IhgApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReporter.getInstance().initialize();
    }
}
