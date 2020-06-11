package com.bbb.bbdev1.run;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

public class ExerciseEntry {
    private Long id;

    private int inputType;         // Manual, GPS or Automatic
    private int activityType;      // Running, cycling, etc.
    private Calendar dateTime;     // When did this entry happen
    private int duration;          // Exercise duration in seconds
    private double distance;       // Distance traveled. Either in meters or feet
    private double avgPace;        // Average pace
    private double avgSpeed;       // Average speed
    private int calorie;           // Calories burnt
    private double climb;          // Climb. Either in meters or feet.
    private int heartRate;         // Heart rate
    private String comment;        // Comments
    private ArrayList<LatLng> locationList;    // Location list


    public Long getId() { return id; }
    public int getInputType() { return inputType; }
    public int getActivityType() { return activityType; }
    public Calendar getDateTime() { return dateTime; }
    public int getDuration() { return duration; }
    public double getDistance() { return distance; }
    public double getAvgPace() { return avgPace; }
    public double getAvgSpeed() { return avgSpeed; }
    public int getCalorie() { return calorie; }
    public double getClimb() { return climb; }
    public int getHeartRate() { return heartRate; }
    public String getComment() { return comment; }
    public ArrayList<LatLng> getLocationList() { return locationList; }

    public void setId(Long id) { this.id = id; }
    public void setInputType(int inputType) { this.inputType = inputType; }
    public void setActivityType(int activityType) { this.activityType = activityType; }
    public void setDateTime(Calendar dateTime) { this.dateTime = dateTime; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setAvgPace(double avgPace) { this.avgPace = avgPace; }
    public void setAvgSpeed(double avgSpeed) { this.avgSpeed = avgSpeed; }
    public void setCalorie(int calorie) { this.calorie = calorie; }
    public void setClimb(double climb) { this.climb = climb; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
    public void setComment(String comment) { this.comment = comment; }
    public void setLocationList(ArrayList<LatLng> locationList) { this.locationList = locationList; }

    @Override
    public String toString() {
        return "Exercise #" + id;
    }

}
