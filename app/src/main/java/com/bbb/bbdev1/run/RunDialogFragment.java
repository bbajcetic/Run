package com.bbb.bbdev1.run;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class RunDialogFragment extends DialogFragment {

    public String response;

    OnDialogResponseListener listener;

    public interface OnDialogResponseListener {
        public void onDialogResponse(String response);
    }

    public static RunDialogFragment newInstance(int dialogId) {
        RunDialogFragment frag = new RunDialogFragment();
        Bundle args = new Bundle();
        args.putInt("dialogId", dialogId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dialogId = getArguments().getInt("dialogId");
        switch (dialogId) {
            case 1: // Date
                final Calendar cDate = Calendar.getInstance();
                int year = cDate.get(Calendar.YEAR);
                int month = cDate.get(Calendar.MONTH);
                int day = cDate.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Calendar months are 0 indexed, days and years are not
                        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        listener.onDialogResponse(date);
                    }
                }, year, month, day);

            case 2: //Time
                final Calendar cTime = Calendar.getInstance();
                int hour = cTime.get(Calendar.HOUR_OF_DAY);
                int minute = cTime.get(Calendar.MINUTE);
                return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        listener.onDialogResponse(time);
                    }
                }, hour, minute, DateFormat.is24HourFormat(getActivity()));

            case 3: // Duration
            case 4: // Distance
            case 5: // Calorie
            case 6: // Heartbeat
            case 7: // Comment
                View inflatedInputView = LayoutInflater.from(getContext()).inflate(R.layout.run_dialog_input, (ViewGroup) getView(), false);
                View inflatedTitleView = LayoutInflater.from(getContext()).inflate(R.layout.run_dialog_title, (ViewGroup) getView(), false);
                final EditText input = (EditText)inflatedInputView.findViewById(R.id.input);
                if (dialogId == 3 || dialogId == 4) {
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                } else if (dialogId == 5 || dialogId == 6) {
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (dialogId == 7) {
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                final TextView title = (TextView)inflatedTitleView.findViewById(R.id.title);
                if (dialogId == 3) {
                    title.setText("Duration");
                } else if (dialogId == 4) {
                    title.setText("Distance");
                } else if (dialogId == 5) {
                    title.setText("Calorie");
                } else if (dialogId == 6) {
                    title.setText("Heartbeat");
                } else if (dialogId == 7) {
                    title.setText("Comment");
                }
                return new AlertDialog.Builder(getActivity())
                        .setCustomTitle(inflatedTitleView)
                        // .setTitle("Duration")
                        .setView(inflatedInputView)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String duration = input.getText().toString();
                                        listener.onDialogResponse(duration);
                                    }
                                }).create();
        }
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_tab_start)
                .setTitle("mytitle")
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