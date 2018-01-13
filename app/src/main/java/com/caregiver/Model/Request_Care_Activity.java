package com.caregiver.Model;

/**
 * Created by Demos on 1/14/2018.
 */

public class Request_Care_Activity {
    private String elder_uid;
    private String caregiver_id;
    private String confirm_key;

    public Request_Care_Activity() {
    }

    public Request_Care_Activity(String elder_uid, String caregiverid, String confirm_key) {
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

    public String getConfirm_key() {
        return confirm_key;
    }

    public void setConfirm_key(String confirm_key) {
        this.confirm_key = confirm_key;
    }
}
