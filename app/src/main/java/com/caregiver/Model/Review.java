package com.caregiver.Model;

/**
 * Created by Demos on 1/14/2018.
 */

public class Review {
    private String elder_uid;
    private String caregiver_uid;
    private float score;
    private String review_detail;
    private String date_of_view;

    public Review() {
        this.elder_uid = "null";
        this.caregiver_uid = "null";
        this.score = 0;
        this.review_detail = "";
        this.date_of_view = "null";
    }

    public Review(String elder_uid, String caregiver_uid, float score, String review_detail, String date_of_view) {
        this.elder_uid = elder_uid;
        this.caregiver_uid = caregiver_uid;
        this.score = score;
        this.review_detail = review_detail;
        this.date_of_view = date_of_view;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getReview_detail() {
        return review_detail;
    }

    public void setReview_detail(String review_detail) {
        this.review_detail = review_detail;
    }

    public String getDate_of_view() {
        return date_of_view;
    }

    public void setDate_of_view(String date_of_view) {
        this.date_of_view = date_of_view;
    }


}
