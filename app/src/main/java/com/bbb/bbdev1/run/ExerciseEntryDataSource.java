package com.bbb.bbdev1.run;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.bbb.bbdev1.run.MySQLiteOpenHelper.*;

public class ExerciseEntryDataSource {
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;

    String[] columns = {
            COLUMN_ID,
            COLUMN_INPUT_TYPE,
            COLUMN_ACTIVITY_TYPE,
            COLUMN_DATE_TIME,
            COLUMN_DURATION,
            COLUMN_DISTANCE,
            COLUMN_AVG_PACE,
            COLUMN_AVG_SPEED,
            COLUMN_CALORIES,
            COLUMN_CLIMB,
            COLUMN_HEARTRATE,
            COLUMN_COMMENT,
            COLUMN_PRIVACY,
            COLUMN_GPS_DATA
    };

    public ExerciseEntryDataSource(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Database operations
    public ExerciseEntry addExercise(ExerciseEntry entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_INPUT_TYPE, entry.getInputType());
        long entryId = database.insert(MySQLiteOpenHelper.TABLE_ENTRIES, null, values);
        entry.setId(entryId);

        // Check that the entry was inserted
        // fill out this section
        return entry;
    }
    public void deleteExercise(ExerciseEntry entry) {
        long entryId = entry.getId();
        database.delete(MySQLiteOpenHelper.TABLE_ENTRIES, MySQLiteOpenHelper.COLUMN_ID + " = " + entryId, null);
    }
    public void deleteAllExercises() {
        database.delete(MySQLiteOpenHelper.TABLE_ENTRIES, null, null);
    }
    public List<ExerciseEntry> getAllExercises() {
        List<ExerciseEntry> exercises = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_ENTRIES,
                columns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            ExerciseEntry entry = getExerciseFromCursor(cursor);
            exercises.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return exercises;
    }
    private ExerciseEntry getExerciseFromCursor(Cursor cursor) {
        ExerciseEntry entry = new ExerciseEntry();
        entry.setId(cursor.getLong(0));
        entry.setInputType(cursor.getInt(1));
        return entry;
    }
}