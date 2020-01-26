package com.wruzjan.ihg.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

public class NetworkStateChecker {

    @NonNull
    private final Application application;

    public NetworkStateChecker(@NonNull Application application) {
        this.application = application;
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }
}
