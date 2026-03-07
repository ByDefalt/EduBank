package com.example.clientAPI.entity;

import java.time.LocalDate;

public class OfferEntity {

    private int id;
    private String picturePath;
    private String title;
    private String description;
    private String state;
    private LocalDate startDate;
    private LocalDate endDate;

    public OfferEntity() {}

    public OfferEntity(int id, String picturePath, String title, String description,
                       String state, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.picturePath = picturePath;
        this.title = title;
        this.description = description;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPicturePath() { return picturePath; }
    public void setPicturePath(String picturePath) { this.picturePath = picturePath; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
