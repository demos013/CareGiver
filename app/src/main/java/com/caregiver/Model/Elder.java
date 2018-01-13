package com.caregiver.Model;

/**
 * Created by Demos on 1/13/2018.
 */

public class Elder {
    private String mobile_number;
    private String uid;
    private String photo_path;
    private String name;
    private String lastname;
    private String citizend_id;
    private String date_of_birth;
    private String address;
    private String telephone;
    private String drug_allergy;
    private String blood_group;
    private String relative_name;
    private String relative_lastname;
    private String relation_elder;
    private String relative_mobile_number;

    public Elder(String mobile_number, String uid,  String name, String lastname, String citizend_id, String date_of_birth, String address, String telephone, String drug_allergy, String blood_group, String relative_name, String relative_lastname, String relation_elder, String relative_mobile_number) {
        this.mobile_number = mobile_number;
        this.uid = uid;
        this.name = name;
        this.lastname = lastname;
        this.citizend_id = citizend_id;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.telephone = telephone;
        this.drug_allergy = drug_allergy;
        this.blood_group = blood_group;
        this.relative_name = relative_name;
        this.relative_lastname = relative_lastname;
        this.relation_elder = relation_elder;
        this.relative_mobile_number = relative_mobile_number;
    }

    public Elder() {
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

    public String getCitizend_id() {
        return citizend_id;
    }

    public void setCitizend_id(String citizend_id) {
        this.citizend_id = citizend_id;
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
}