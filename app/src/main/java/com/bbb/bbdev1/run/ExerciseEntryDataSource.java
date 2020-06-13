package com.bbb.bbdev1.run;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.bbb.bbdev1.run.MySQLiteOpenHelper.*;

public class ExerciseEntryDataSource {
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;

    public static String[] columns = {
            COLUMN_ID,
            COLUMN_EMAIL,
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
    public void addExercise(ExerciseEntry entry, String email) {
        new insertAsyncTask(database, entry, email).execute();
    }
    public void deleteExercise(long entryId, String email) {
        new deleteAsyncTask(database, entryId, email).execute();
    }
    // if I use this, change this to run asynchronously
    public void deleteAllExercises(String email) {
        database.delete(MySQLiteOpenHelper.TABLE_ENTRIES, MySQLiteOpenHelper.COLUMN_EMAIL + " = ?", new String[]{email});
    }
    public List<ExerciseEntry> getAllExercises(String email) {
        List<ExerciseEntry> exercises = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_ENTRIES,
                ExerciseEntryDataSource.columns, COLUMN_EMAIL + " = ?", new String[]{email},
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
        // make an enum for this in the future instead of hardcoded numbers
        ExerciseEntry entry = new ExerciseEntry(
                cursor.getLong(0),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getDouble(7),
                cursor.getDouble(8),
                cursor.getInt(9),
                cursor.getDouble(10),
                cursor.getInt(11),
                cursor.getString(12),
                cursor.getInt(13),
                cursor.getString(14)
        );
        return entry;
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase database;
        private long entryId;
        private String email;

        deleteAsyncTask(SQLiteDatabase database, long entryId, String email) {
            this.database = database;
            this.entryId = entryId;
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int rows = database.delete(MySQLiteOpenHelper.TABLE_ENTRIES,
                    COLUMN_ID + " = " + entryId + " AND " + COLUMN_EMAIL + " = ?", new String[]{email});
            Log.d("RUN_TAG", entryId + " " + email + " " + Integer.toString(rows));
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase database;
        private ExerciseEntry entry;
        private String email;

        insertAsyncTask(SQLiteDatabase database, ExerciseEntry entry, String email) {
            this.database = database;
            this.entry = entry;
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, email);
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
