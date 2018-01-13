package com.caregiver.Model;

/**
 * Created by Demos on 1/14/2018.
 */

public class Location {
    private String uid;
    private Double latitude;
    private Double longitude;

    public Location() {
    }

    public Location(String uid, Double latitude, Double longitude) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
