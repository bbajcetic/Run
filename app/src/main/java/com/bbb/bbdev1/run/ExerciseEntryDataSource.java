package com.bbb.bbdev1.run;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.bbb.bbdev1.run.MySQLiteOpenHelper.*;

public class ExerciseEntryDataSource {
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;

    public static String[] columns = {
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

    public void reset() {
        dbHelper.onUpgrade(database, DATABASE_VERSION, DATABASE_VERSION);
    }
    public void close() {
        dbHelper.close();
    }

    // Database operations
    public void addExercise(ExerciseEntry entry) {
        new insertAsyncTask(database, entry).execute();
    }
    public void deleteExercise(long entryId) {
        new deleteAsyncTask(database, entryId).execute();
    }
    public void deleteAllExercises() {
        database.delete(MySQLiteOpenHelper.TABLE_ENTRIES, null, null);
    }
    public List<ExerciseEntry> getAllExercises() {
        List<ExerciseEntry> exercises = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_ENTRIES,
                ExerciseEntryDataSource.columns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            ExerciseEntry entry = ExerciseEntryDataSource.getExerciseFromCursor(cursor);
            exercises.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return exercises;
    }

    public static ExerciseEntry getExerciseFromCursor(Cursor cursor) {
        ExerciseEntry entry = new ExerciseEntry(
                cursor.getLong(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getDouble(7),
                cursor.getInt(8),
                cursor.getDouble(9),
                cursor.getInt(10),
                cursor.getString(11),
                cursor.getInt(12),
                cursor.getString(13)
        );
        return entry;
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase database;
        private long entryId;

        deleteAsyncTask(SQLiteDatabase database, long entryId) {
            this.database = database;
            this.entryId = entryId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database.delete(MySQLiteOpenHelper.TABLE_ENTRIES, MySQLiteOpenHelper.COLUMN_ID + " = " + entryId, null);
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase database;
        private ExerciseEntry entry;

        insertAsyncTask(SQLiteDatabase database, ExerciseEntry entry) {
            this.database = database;
            this.entry = entry;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_INPUT_TYPE, entry.getInputType());
            values.put(COLUMN_ACTIVITY_TYPE, entry.getActivityType());
            values.put(COLUMN_DATE_TIME, entry.getDateTime());
            values.put(COLUMN_DURATION, entry.getDuration());
            values.put(COLUMN_DISTANCE, entry.getDistance());
            values.put(COLUMN_AVG_PACE, entry.getAvgPace());
            values.put(COLUMN_AVG_SPEED, entry.getAvgSpeed());
            values.put(COLUMN_CALORIES, entry.getCalorie());
            values.put(COLUMN_CLIMB, entry.getClimb());
            values.put(COLUMN_HEARTRATE, entry.getHeartRate());
            values.put(COLUMN_COMMENT, entry.getComment());
            values.put(COLUMN_PRIVACY, entry.getPrivacy());
            values.put(COLUMN_GPS_DATA, entry.getGPSData());
            long entryId = database.insert(MySQLiteOpenHelper.TABLE_ENTRIES, null, values);
            entry.setId(entryId);

            return null;
        }
    }

}
