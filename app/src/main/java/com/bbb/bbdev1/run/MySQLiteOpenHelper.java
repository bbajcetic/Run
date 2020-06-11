package com.bbb.bbdev1.run;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExerciseEntries.db";

    public static final String TABLE_ENTRIES = "exercise_entries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INPUT_TYPE = "input_type";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    public static final String COLUMN_DATE_TIME = "data_time";
    public static final String COLUMN_DURATION = "data_time";
    public static final String COLUMN_DISTANCE = "data_time";
    public static final String COLUMN_AVG_PACE = "data_time";
    public static final String COLUMN_AVG_SPEED = "data_time";
    public static final String COLUMN_CALORIES = "data_time";
    public static final String COLUMN_CLIMB = "data_time";
    public static final String COLUMN_HEARTRATE = "data_time";
    public static final String COLUMN_COMMENT = "data_time";
    public static final String COLUMN_PRIVACY = "data_time";
    public static final String COLUMN_GPS_DATA = "data_time";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_ENTRIES + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_INPUT_TYPE + "integer not null,"
                    + COLUMN_ACTIVITY_TYPE + "integer not null,"
                    + COLUMN_DATE_TIME + "datetime not null,"
                    + COLUMN_DURATION + "integer not null,"
                    + COLUMN_DISTANCE + "double not null,"
                    + COLUMN_AVG_PACE + "double not null,"
                    + COLUMN_AVG_SPEED + "double not null,"
                    + COLUMN_CALORIES + "integer not null,"
                    + COLUMN_CLIMB + "double not null,"
                    + COLUMN_HEARTRATE + "int not null,"
                    + COLUMN_COMMENT + "text not null,"
                    + COLUMN_PRIVACY + "integer not null,"
                    + COLUMN_GPS_DATA + "text not null"
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
