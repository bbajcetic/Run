package com.bbb.bbdev1.run;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingFragment extends Fragment {

    OnLoadingCompleteListener listener;
    AsyncTask waitTask;

    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        int signin_status = args.getInt("SIGNIN_STATUS");

        if (savedInstanceState != null) {
            listener.onLoadingComplete(signin_status);
            return;
        }

        //start async task
        waitTask = new WaitTask(signin_status).execute();
    }

    public interface OnLoadingCompleteListener {
        public void onLoadingComplete(int signin_status);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnLoadingCompleteListener) context;
        } catch (ClassCastException e) { throw new ClassCastException(context.toString() + " must implement OnLoadingCompleteListener"); }
    }

    public static LoadingFragment newInstance(int signin_status) {
        /*
        This method takes a parameter signin_status which can take on the following values:
            0: no signin was attempted, the activity was just created
            1: signin was attempted and was successful
            2: signin was attempted and failed
        */

        LoadingFragment fragment = new LoadingFragment();

        Bundle args = new Bundle();
        args.putInt("SIGNIN_STATUS", signin_status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class WaitTask extends AsyncTask<Void, Void, Boolean> {
        int signin_status;

        WaitTask(int signin_status) { this.signin_status = signin_status; };

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return !isCancelled();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                listener.onLoadingComplete(signin_status);
            }
        }
    }
}
