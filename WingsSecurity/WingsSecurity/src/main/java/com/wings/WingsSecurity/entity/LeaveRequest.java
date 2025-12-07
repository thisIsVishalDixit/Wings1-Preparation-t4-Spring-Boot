package com.wings.WingsSecurity.entity;

import java.util.Date;

public class LeaveRequest {

    private String category;

    private int noOfDays;

    private Date appliedOn;

    private String description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public Date getAppliedOn() {
        return appliedOn;
    }

    public void setAppliedOn(Date appliedOn) {
        this.appliedOn = appliedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
