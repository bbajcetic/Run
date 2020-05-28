package com.bbb.bbdev1.run;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.widget.TextView;
import android.widget.Toast;


public class RunDialogFragment extends DialogFragment {

    public String response;

    OnDialogResponseListener listener;

    public interface OnDialogResponseListener {
        public void onDialogResponse(String response);
    }

    public static RunDialogFragment newInstance(String title) {
        RunDialogFragment frag = new RunDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_tab_start)
                .setTitle(title)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                listener.onDialogResponse("ok");
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                listener.onDialogResponse("cancel");
                            }
                        }).create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Make sure the parent Activity implements the listener interface
        try {
            listener = (OnDialogResponseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDialogResponseListener");
        }
    }
}