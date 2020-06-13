package com.bbb.bbdev1.run;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExerciseEntries.db";

    public static final String TABLE_ENTRIES = "exercise_entries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_INPUT_TYPE = "INPUT_TYPE";
    public static final String COLUMN_ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final String COLUMN_DATE_TIME = "DATE_TIME";
    public static final String COLUMN_DURATION = "DURATION";
    public static final String COLUMN_DISTANCE = "DISTANCE";
    public static final String COLUMN_AVG_PACE = "AVG_PACE";
    public static final String COLUMN_AVG_SPEED = "AVG_SPEED";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_CLIMB = "CLIMB";
    public static final String COLUMN_HEARTRATE = "HEARTRATE";
    public static final String COLUMN_COMMENT = "COMMENT";
    public static final String COLUMN_PRIVACY = "PRIVACY";
    public static final String COLUMN_GPS_DATA = "GPS_DATA";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_ENTRIES + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_EMAIL + " text not null, "
                    + COLUMN_INPUT_TYPE + " integer not null, "
                    + COLUMN_ACTIVITY_TYPE + " text not null, "
                    + COLUMN_DATE_TIME + " datetime not null, "
                    + COLUMN_DURATION + " float not null, "
                    + COLUMN_DISTANCE + " float, "
                    + COLUMN_AVG_PACE + " float, "
                    + COLUMN_AVG_SPEED + " float, "
                    + COLUMN_CALORIES + " integer, "
                    + COLUMN_CLIMB + " float, "
                    + COLUMN_HEARTRATE + " integer, "
                    + COLUMN_COMMENT + " text, "
                    + COLUMN_PRIVACY + " integer, "
                    + COLUMN_GPS_DATA + " text"
                    + ");";
    public static final String DATABASE_DROP =
            "DROP TABLE IF EXISTS " + TABLE_ENTRIES;

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // destroying all old data to upgrade database (not ideal for future)
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }
}
