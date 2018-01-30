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
    private String sex;
    private String date_of_birth;
    private String address;
    private String telephone;
    private String specialization;
    private String carecenter_id;
    private Location location;
    private String experience;
    private String job;
    private String cost;
    private String city;
    private String district;
    private String state;



    public Caregiver() {
        this.mobile_number = "null";
        this.uid = "null";
        this.photo_path = "null";
        this.name = "null";
        this.lastname = "null";
        this.sex = "null";
        this.citizen_id = "null";
        this.date_of_birth = "null";
        this.address = "null";
        this.telephone = "null";
        this.specialization = "null";
        this.carecenter_id = "null";
        this.location = new Location();
        this.experience = "null";
        this.job = "null";
        this.cost = "null";
        this.city = "null";
        this.district = "null";
        this.state = "null";
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
