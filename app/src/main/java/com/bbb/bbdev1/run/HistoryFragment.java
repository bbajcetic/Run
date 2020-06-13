package com.bbb.bbdev1.run;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<ExerciseEntry>> {
    private static final int EDIT_ENTRY_REQUEST = 1;
    private static final int ENTRIES_LOADER_ID = 1;
    protected ExerciseEntryDataSource dataSource;
    final String TAG = "RUN_TAG";
    private SharedPreferences mPreferences;
    String email;

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
        mPreferences = getActivity().getSharedPreferences(SignInActivity.PROFILES_FILE, MODE_PRIVATE);
        email = mPreferences.getString("SESSION_EMAIL", null);
        assert(email != null); //have to have a valid session email, if not, sign user out (assert for now)

        dataSource = new ExerciseEntryDataSource(getActivity());
        dataSource.open();
        //dataSource.reset();

        entryList = root.findViewById(R.id.entry_list);
        setupEntryListAdapter();
        setupEntryListOnClick();

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
                Intent editIntent = new Intent(getActivity(), ManualEntryActivity.class);
                editIntent.putExtra("ACTION", "edit");
                editIntent.putExtra("ENTRY", allEntries.get(position));
                startActivityForResult(editIntent, EDIT_ENTRY_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ENTRY_REQUEST && resultCode == RESULT_OK) {
            // reload the list
            /*LoaderManager loader = LoaderManager.getInstance(this);
            loader.initLoader(ENTRIES_LOADER_ID, null, this).forceLoad();*/
        }
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
        LoaderManager loader = LoaderManager.getInstance(this);
        loader.initLoader(ENTRIES_LOADER_ID, null, this).forceLoad();
        Log.d(TAG, String.format("%s onResume() called", "History Fragment"));
    }

    @NonNull
    @Override
    public Loader<List<ExerciseEntry>> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == ENTRIES_LOADER_ID) {
            return new EntryListLoader(getActivity(), email);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<ExerciseEntry>> loader, List<ExerciseEntry> data) {
        if (loader.getId() == ENTRIES_LOADER_ID) {
            if (data.size() > 0) {
                entryListAdapter.clear();
                entryListAdapter.addAll(data);
                entryListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<ExerciseEntry>> loader) {
    }
}
