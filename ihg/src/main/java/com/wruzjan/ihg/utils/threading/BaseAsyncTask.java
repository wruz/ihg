package com.wruzjan.ihg.utils.threading;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseAsyncTask<P, R> extends AsyncTask<P, Void, R> {

    @Nullable
    private PreExecuteUiListener preExecuteUiListener;

    @Nullable
    private PostExecuteUiListener<R> postExecuteUiListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (preExecuteUiListener != null) {
            preExecuteUiListener.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(R protocol) {
        super.onPostExecute(protocol);
        if (postExecuteUiListener != null) {
            postExecuteUiListener.onPostExecute(protocol);
        }
    }

    public void setPreExecuteUiListener(@Nullable PreExecuteUiListener preExecuteUiListener) {
        this.preExecuteUiListener = preExecuteUiListener;
    }

    public void setPostExecuteUiListener(@Nullable PostExecuteUiListener<R> postExecuteUiListener) {
        this.postExecuteUiListener = postExecuteUiListener;
    }

    public interface PreExecuteUiListener {

        void onPreExecute();
    }

    public interface PostExecuteUiListener<R> {

        void onPostExecute(@NonNull R protocol);
    }
}
