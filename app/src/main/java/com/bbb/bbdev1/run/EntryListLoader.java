package com.bbb.bbdev1.run;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EntryListLoader extends AsyncTaskLoader<List<ExerciseEntry>> {
    private final ExerciseEntryDataSource dataSource;
    String email;

    public EntryListLoader(@NonNull Context context, String email) {
        super(context);
        this.email = email;
        dataSource = new ExerciseEntryDataSource(context);
        dataSource.open();
    }

    @Nullable
    @Override
    public List<ExerciseEntry> loadInBackground() {
        return dataSource.getAllExercises(email);
    }
}
