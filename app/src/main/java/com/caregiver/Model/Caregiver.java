package com.caregiver.Model;

import java.io.Serializable;

/**
 * Created by Demos on 1/14/2018.
 */

public class Caregiver  implements Serializable {
    private String mobile_number;
    private String uid;
    private String photo_path;
    private String name;
    private String lastname;
    private String citizen_id;
    private String date_of_birth;
    private String address;
    private String telephone;
    private String specialization;
    private String carecenter_id;

    public Caregiver() {
        this.mobile_number = "null";
        this.uid = "null";
        this.photo_path = "null";
        this.name = "null";
        this.lastname = "null";
        this.citizen_id = "null";
        this.date_of_birth = "null";
        this.address = "null";
        this.telephone = "null";
        this.specialization = "null";
        this.carecenter_id = "null";
    }

    public Caregiver(String mobile_number, String uid, String photo_path, String name, String lastname, String citizen_id, String date_of_birth, String address, String telephone, String specialization, String carecenter_id) {
        this.mobile_number = mobile_number;
        this.uid = uid;
        this.photo_path = photo_path;
        this.name = name;
        this.lastname = lastname;
        this.citizen_id = citizen_id;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.telephone = telephone;
        this.specialization = specialization;
        this.carecenter_id = carecenter_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCitizen_id() {
        return citizen_id;
    }

    public void setCitizen_id(String citizen_id) {
        this.citizen_id = citizen_id;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCarecenter_id() {
        return carecenter_id;
    }

    public void setCarecenter_id(String care_center_id) {
        this.carecenter_id = care_center_id;
    }
}
