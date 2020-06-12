package com.bbb.bbdev1.run;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.List;

class EntryListAdapter extends ArrayAdapter<ExerciseEntry> {
    List<ExerciseEntry> entryList;
    Context context;
    int resource;
    protected SharedPreferences mPreferences;
    
    public EntryListAdapter(Context context, int resource, @NonNull List<ExerciseEntry> entries) {
        super(context, resource, entries);
        this.context = context;
        this.entryList = entries;
        this.resource = resource;
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(resource, parent, false);
        }
        String unitPref = mPreferences.getString(SettingsActivity.UNIT_PREF_KEY, "kms");
        ExerciseEntry entry = entryList.get(position);
        TextView textHeader = listView.findViewById(R.id.text_header);
        TextView textSubheader = listView.findViewById(R.id.text_subheader);
        TextView textDatetime = listView.findViewById(R.id.text_datetime);
        textHeader.setText(entry.getEntryHeader());
        textSubheader.setText(entry.getStats(unitPref));
        textDatetime.setText(entry.getDateTime());
        return listView;
    }
}