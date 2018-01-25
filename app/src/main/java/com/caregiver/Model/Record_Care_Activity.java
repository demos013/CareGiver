package com.caregiver.Model;

/**
 * Created by Demos on 1/25/2018.
 */

public class Record_Care_Activity {
    private String elder_uid;
    private String caregiver_uid;
    private String start_date;
    private String start_time;
    private String finish_date;
    private String finish_time;
    private String activity_detail;

    public Record_Care_Activity() {
        this.elder_uid = "null";
        this.caregiver_uid = "null";
        this.start_date = "null";
        this.start_time = "null";
        this.finish_date = "null";
        this.finish_time = "null";
        this.activity_detail = "";

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

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getActivity_detail() {
        return activity_detail;
    }

    public void setActivity_detail(String activity_detail) {
        this.activity_detail = activity_detail;
    }
}
