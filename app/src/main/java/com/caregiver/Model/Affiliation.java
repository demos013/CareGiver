package com.caregiver.Model;

/**
 * Created by Demos on 1/14/2018.
 */

public class Affiliation {
    private String affilation_id;
    private String name;
    private String address;
    private String detail;
    private String photo_path;
    private String telephone;

    public Affiliation(String affilation_id, String name, String address, String detail, String photo_path, String telephone) {
        this.affilation_id = affilation_id;
        this.name = name;
        this.address = address;
        this.detail = detail;
        this.photo_path = photo_path;
        this.telephone = telephone;
    }

    public Affiliation() {
    }

    public String getAffilation_id() {
        return affilation_id;
    }

    public void setAffilation_id(String affilation_id) {
        this.affilation_id = affilation_id;
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
