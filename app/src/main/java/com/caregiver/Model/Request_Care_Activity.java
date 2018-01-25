package com.caregiver.Model;

import java.io.Serializable;

/**
 * Created by Demos on 1/14/2018.
 */

public class Request_Care_Activity implements Serializable {
    private String elder_uid;
    private String caregiver_id;
    private int confirm_key;
    private String start_date;
    private String start_time;

    public Request_Care_Activity() {
    }

    public Request_Care_Activity(String elder_uid, String caregiverid, int confirm_key) {
        this.elder_uid = elder_uid;
        caregiver_id = caregiverid;
        this.confirm_key = confirm_key;
    }

    public String getElder_uid() {
        return elder_uid;
    }

    public void setElder_uid(String elder_uid) {
        this.elder_uid = elder_uid;
    }

    public String getCaregiver_id() {
        return caregiver_id;
    }

    public void setCaregiver_id(String caregiver_id) {
        this.caregiver_id = caregiver_id;
    }

    public int getConfirm_key() {
        return confirm_key;
    }

    public void setConfirm_key(int confirm_key) {
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
}
