package com.bbb.bbdev1.run;

import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

public class ExerciseEntry implements Parcelable {
    private Long id;

    private int inputType;         // Manual, GPS or Automatic
    private String activityType;      // Running, cycling, etc.
    private String dateTime;     // When did this entry happen
    private int duration;          // Exercise duration in seconds
    private double distance;       // Distance traveled. Either in meters or feet
    private double avgPace;        // Average pace
    private double avgSpeed;       // Average speed
    private int calorie;           // Calories burnt
    private double climb;          // Climb. Either in meters or feet.
    private int heartRate;         // Heart rate
    private String comment;        // Comments
    private int privacy;
    private ArrayList<LatLng> locationList;    // Location list

    public ExerciseEntry(int inputType, String activityType, String dateTime, int duration, double distance,
                         int calorie, int heartRate, String comment, int privacy) {
        this.id = -1L;
        this.inputType = inputType;
        this.activityType = activityType;
        this.dateTime = dateTime;
        this.duration = duration;
        this.distance = distance;
        this.avgPace = -1;
        this.avgSpeed = -1;
        this.calorie = calorie;
        this.climb = -1;
        this.heartRate = heartRate;
        this.comment = comment;
        this.privacy = privacy;
        this.locationList = null;
    }
    public ExerciseEntry(Long id, int inputType, String activityType, String dateTime, int duration, double distance,
                         double avgPace, double avgSpeed, int calorie, double climb, int heartRate, String comment,
                         int privacy, String gpsData) {
        this(inputType, activityType, dateTime, duration, distance, calorie, heartRate, comment, privacy);
        this.id = id;
        this.avgPace = avgPace;
        this.avgSpeed = avgSpeed;
        this.climb = climb;
        putGPSData(gpsData);
    }

    protected ExerciseEntry(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        inputType = in.readInt();
        activityType = in.readString();
        dateTime = in.readString();
        duration = in.readInt();
        distance = in.readDouble();
        avgPace = in.readDouble();
        avgSpeed = in.readDouble();
        calorie = in.readInt();
        climb = in.readDouble();
        heartRate = in.readInt();
        comment = in.readString();
        privacy = in.readInt();
        locationList = in.createTypedArrayList(LatLng.CREATOR);
    }

    public static final Creator<ExerciseEntry> CREATOR = new Creator<ExerciseEntry>() {
        @Override
        public ExerciseEntry createFromParcel(Parcel in) {
            return new ExerciseEntry(in);
        }

        @Override
        public ExerciseEntry[] newArray(int size) {
            return new ExerciseEntry[size];
        }
    };

    public Long getId() { return id; }
    public int getInputType() { return inputType; }
    public String getActivityType() { return activityType; }
    public String getDateTime() { return dateTime; }
    public int getDuration() { return duration; }
    public double getDistance() { return distance; }
    public double getAvgPace() { return avgPace; }
    public double getAvgSpeed() { return avgSpeed; }
    public int getCalorie() { return calorie; }
    public double getClimb() { return climb; }
    public int getHeartRate() { return heartRate; }
    public String getComment() { return comment; }
    public int getPrivacy() { return privacy; }
    public ArrayList<LatLng> getLocationList() { return locationList; }
    public String getGPSData() { return ""; }

    public void setId(Long id) { this.id = id; }
    public void setInputType(int inputType) { this.inputType = inputType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setAvgPace(double avgPace) { this.avgPace = avgPace; }
    public void setAvgSpeed(double avgSpeed) { this.avgSpeed = avgSpeed; }
    public void setCalorie(int calorie) { this.calorie = calorie; }
    public void setClimb(double climb) { this.climb = climb; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
    public void setComment(String comment) { this.comment = comment; }
    public void setPrivacy(int privacy) { this.privacy = privacy; }
    public void setLocationList(ArrayList<LatLng> locationList) { this.locationList = locationList; }
    public void putGPSData(String gpsData) { locationList = null; }

    @Override
    public String toString() {
        return "Exercise #" + id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(inputType);
        dest.writeString(activityType);
        dest.writeString(dateTime);
        dest.writeInt(duration);
        dest.writeDouble(distance);
        dest.writeDouble(avgPace);
        dest.writeDouble(avgSpeed);
        dest.writeInt(calorie);
        dest.writeDouble(climb);
        dest.writeInt(heartRate);
        dest.writeString(comment);
        dest.writeInt(privacy);
        dest.writeTypedList(locationList);
    }

    enum InputType {
        Manual(0),
        GPS(1),
        Automatic(2);

        private int index;
        InputType(int index) {
            this.index = index;
        }
        public int getIndex() { return index; }
    }
    public String getEntryHeader() {
        InputType type = InputType.values()[inputType];
        return type.name() + ": " + activityType;
    }
    public String getStats() {
        return distance + " kms, " + duration + " mins";
    }

}
