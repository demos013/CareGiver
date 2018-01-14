package com.caregiver.Model;

/**
 * Created by Demos on 1/14/2018.
 */

public class Care_Center {
    private String care_center_id;
    private String name;
    private String address;
    private String detail;
    private String photo_path;
    private String telephone;

    public Care_Center() {
    }

    public Care_Center(String care_center_id, String name, String address, String detail, String photo_path, String telephone) {
        this.care_center_id = care_center_id;
        this.name = name;
        this.address = address;
        this.detail = detail;
        this.photo_path = photo_path;
        this.telephone = telephone;
    }

    public String getCare_center_id() {
        return care_center_id;
    }

    public void setCare_center_id(String care_center_id) {
        this.care_center_id = care_center_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
