package com.bbb.bbdev1.run;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    protected ExerciseEntryDataSource dataSource;
    final String TAG = "RUN_TAG";

    private ListView entryList;
    private List<ExerciseEntry> allEntries = new ArrayList<>();
    private ArrayAdapter<ExerciseEntry> entryListAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        dataSource = new ExerciseEntryDataSource(getActivity());
        dataSource.open();
        //dataSource.reset();
        allEntries = dataSource.getAllExercises();

        entryList = root.findViewById(R.id.entry_list);
        setupEntryListAdapter();

        return root;
    }

    private void setupEntryListAdapter() {
        entryListAdapter = new EntryListAdapter(getActivity(), R.layout.item_entry_list, allEntries);
        entryList.setAdapter(entryListAdapter);
    }

    private void setupEntryListOnClick() {
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create intent to view details about exercise entry (query for exercise entry or pass in id)
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        dataSource.close();
        Log.d(TAG, String.format("%s onPause() called", "History Fragment"));
    }
    @Override
    public void onResume() {
        super.onResume();
        dataSource.open();
        allEntries.clear();
        allEntries.addAll(dataSource.getAllExercises());
        entryListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "" + allEntries.size(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, String.format("%s onResume() called", "History Fragment"));
    }

}
