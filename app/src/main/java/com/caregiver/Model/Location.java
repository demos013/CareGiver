package com.caregiver.Model;

import java.io.Serializable;

/**
 * Created by Demos on 1/14/2018.
 */

public class Location implements Serializable {

    private Double latitude;
    private Double longitude;

    public Location() {
        latitude = 0.0;
        longitude = 0.0;

    }

    public Location(Double latitude, Double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
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
