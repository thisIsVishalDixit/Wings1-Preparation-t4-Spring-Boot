package com.wings.WingsSecurity.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_request")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String category;

    @Column(name = "no_of_days")
    private int noOfDays;

    @Column(name = "applied_on")
    private Date appiedOn;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Leave() {
    }

    public Leave(int id, String category, int noOfDays, Date appiedOn, String description, Status status, User user) {
        this.id = id;
        this.category = category;
        this.noOfDays = noOfDays;
        this.appiedOn = appiedOn;
        this.description = description;
        this.status = status;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getAppiedOn() {
        return appiedOn;
    }

    public void setAppiedOn(Date appiedOn) {
        this.appiedOn = appiedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Leave [id=" + id + ", category=" + category + ", noOfDays=" + noOfDays + ", appiedOn=" + appiedOn
                + ", description=" + description + ", status=" + status + ", user=" + user + "]";
    }

}
