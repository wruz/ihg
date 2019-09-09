package com.wruzjan.ihg.utils.threading;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseAsyncTask<P, R> extends AsyncTask<P, Void, R> {

    @Nullable
    private UiListener<R> uiListener;

    @Override
    protected void onPostExecute(R protocol) {
        super.onPostExecute(protocol);
        if (uiListener != null) {
            uiListener.onPostExecute(protocol);
        }
    }

    public void setUiListener(@Nullable UiListener<R> uiListener) {
        this.uiListener = uiListener;
    }

    public interface UiListener<R> {

        void onPostExecute(@NonNull R protocol);
    }
}
