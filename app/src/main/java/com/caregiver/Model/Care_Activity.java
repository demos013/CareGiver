package com.caregiver.Model;

import java.io.Serializable;

/**
 * Created by Demos on 1/14/2018.
 */

public class Care_Activity implements Serializable{
    private String elder_uid;
    private String caregiver_uid;
    private String start_key;
    private String confirm_key;
    private String start_date;
    private String start_time;
    private Boolean start_check;
    private String finish_date;
    private String finish_time;
    private Boolean finish_check;
    private String activity_detail;

    public Care_Activity() {
        this.elder_uid = "null";
        this.caregiver_uid = "null";
        this.start_key = "null";
        this.confirm_key = "null";
        this.start_date = "null";
        this.start_time = "null";
        this.finish_date = "null";
        this.finish_time = "null";
        this.finish_check = false;
        this.activity_detail = "";
        start_check = false;
    }

    public Care_Activity(String elder_uid, String caregiver_uid, String start_key, String confirm_key, String start_date, String start_time, String finish_time, Boolean finish_check, String activity_detail) {
        this.elder_uid = elder_uid;
        this.caregiver_uid = caregiver_uid;
        this.start_key = start_key;
        this.confirm_key = confirm_key;
        this.start_date = start_date;
        this.start_time = start_time;
        this.finish_time = finish_time;
        this.finish_check = finish_check;
        this.activity_detail = activity_detail;

    }

    public String getElder_uid() {
        return elder_uid;
    }

    public void setElder_uid(String elder_uid) {
        this.elder_uid = elder_uid;
    }

    public String getCaregiver_uid() {
        return caregiver_uid;
    }

    public void setCaregiver_uid(String caregiver_uid) {
        this.caregiver_uid = caregiver_uid;
    }

    public String getStart_key() {
        return start_key;
    }

    public void setStart_key(String start_key) {
        this.start_key = start_key;
    }

    public String getConfirm_key() {
        return confirm_key;
    }

    public void setConfirm_key(String confirm_key) {
        this.confirm_key = confirm_key;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public Boolean getFinish_check() {
        return finish_check;
    }

    public void setFinish_check(Boolean finish_check) {
        this.finish_check = finish_check;
    }

    public String getActivity_detail() {
        return activity_detail;
    }

    public void setActivity_detail(String activity_detail) {
        this.activity_detail = activity_detail;
    }

    public Boolean getStart_check() {
        return start_check;
    }

    public Care_Activity setStart_check(Boolean start_check) {
        this.start_check = start_check;
        return this;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public Care_Activity setFinish_date(String finish_date) {
        this.finish_date = finish_date;
        return this;
    }
}