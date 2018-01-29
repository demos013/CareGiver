package com.caregiver.Model;

import android.location.*;

import java.io.Serializable;

/**
 * Created by Demos on 1/13/2018.
 */

public class Elder implements Serializable{
    private String mobile_number;
    private String uid;
    private String photo_path;
    private String name;
    private String lastname;
    private String citizen_id;
    private String date_of_birth;
    private String job;
    private String address;
    private String telephone;
    private String drug_allergy;
    private String blood_group;
    private String relative_name;
    private String relative_lastname;
    private String relation_elder;
    private String relative_mobile_number;
    private Location location ;
    private String sex;



    public Elder(String mobile_number, String uid, String photo_path, String name, String lastname, String citizen_id, String date_of_birth, String job, String address, String telephone, String drug_allergy, String blood_group, String relative_name, String relative_lastname, String relation_elder, String relative_mobile_number) {
        this.mobile_number = mobile_number;
        this.uid = uid;
        this.photo_path = photo_path;
        this.name = name;
        this.lastname = lastname;
        this.citizen_id = citizen_id;
        this.date_of_birth = date_of_birth;
        this.job = job;
        this.address = address;
        this.telephone = telephone;
        this.drug_allergy = drug_allergy;
        this.blood_group = blood_group;
        this.relative_name = relative_name;
        this.relative_lastname = relative_lastname;
        this.relation_elder = relation_elder;
        this.relative_mobile_number = relative_mobile_number;
        this.location = new Location();

    }


    public Elder() {
        this.mobile_number = "null";
        this.uid = "null";
        this.name = "null";
        this.lastname = "null";
        this.citizen_id = "null";
        this.date_of_birth = "null";
        this.job = "null";
        this.address = "null";
        this.telephone = "null";
        this.drug_allergy = "null";
        this.blood_group = "null";
        this.relative_name = "null";
        this.relative_lastname = "null";
        this.relation_elder = "null";
        this.relative_mobile_number = "null";
        this.photo_path = "null";
        this.relation_elder = "null";
        this.location = new Location();
        this.sex = "null";
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getDrug_allergy() {
        return drug_allergy;
    }

    public void setDrug_allergy(String drug_allergy) {
        this.drug_allergy = drug_allergy;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getRelative_name() {
        return relative_name;
    }

    public void setRelative_name(String relative_name) {
        this.relative_name = relative_name;
    }

    public String getRelative_lastname() {
        return relative_lastname;
    }

    public void setRelative_lastname(String relative_lastname) {
        this.relative_lastname = relative_lastname;
    }

    public String getRelation_elder() {
        return relation_elder;
    }

    public void setRelation_elder(String relation_elder) {
        this.relation_elder = relation_elder;
    }

    public String getRelative_mobile_number() {
        return relative_mobile_number;
    }

    public void setRelative_mobile_number(String relative_mobile_number) {
        this.relative_mobile_number = relative_mobile_number;
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

    public Elder setSex(String sex) {
        this.sex = sex;
        return this;
    }
}
