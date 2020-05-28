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

import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
            case 3: //Duration
                break;
            case 4: //Distance
                break;
            case 5: //Calorie
                break;
            case 6: //Heartbeat
                break;
            case 7: //Comment
                break;
        }
        /*return new AlertDialog.Builder(getActivity())
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
                        }).create();*/
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