package com.caregiver.Model;

/**
 * Created by Demos on 1/14/2018.
 */

public class Care_Center_Location {
    private String care_center_id;
    private double latitude;

    public Care_Center_Location() {
    }

    public Care_Center_Location(String care_center_id, double latitude) {
        this.care_center_id = care_center_id;
        this.latitude = latitude;
    }

    public String getCare_center_id() {
        return care_center_id;
    }

    public void setCare_center_id(String care_center_id) {
        this.care_center_id = care_center_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
