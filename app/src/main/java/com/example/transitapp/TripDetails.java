package com.example.transitapp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripDetails implements Serializable {
    int bus_number;
    String driver_name;
    int racks_loaded;
    int racks_unloaded;

    public TripDetails() {
        this.racks_loaded = 0;
        this.racks_unloaded = 0;
        this.students_arrived = 0;
        this.students_departed = 0;
    }

    String stop;
    int students_arrived;
    int students_departed;
    String trip_end_time;
    Date trip_start_time;
    String schedule;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }


    public int getBus_number() {
        return bus_number;
    }

    public void setBus_number(int bus_number) {
        this.bus_number = bus_number;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public int getRacks_loaded() {
        return racks_loaded;
    }

    public void setRacks_loaded(int racks_loaded) {
        this.racks_loaded = racks_loaded;
    }

    public int getRacks_unloaded() {
        return racks_unloaded;
    }

    public void setRacks_unloaded(int racks_unloaded) {
        this.racks_unloaded = racks_unloaded;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public int getStudents_arrived() {
        return students_arrived;
    }

    public void setStudents_arrived(int students_arrived) {
        this.students_arrived = students_arrived;
    }

    public int getStudents_departed() {
        return students_departed;
    }

    public void setStudents_departed(int students_departed) {
        this.students_departed = students_departed;
    }

    public String getTrip_end_time() {
        return trip_end_time;
    }

    public void setTrip_end_time(String trip_end_time) {
        this.trip_end_time = trip_end_time;
    }

    public Date getTrip_start_time() {
        return trip_start_time;
    }

    public void setTrip_start_time(String trip_start_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        try {
            this.trip_start_time = dateFormat.parse(trip_start_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
