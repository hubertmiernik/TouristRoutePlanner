package com.example.touristrouteplanner;

import com.android.volley.toolbox.StringRequest;

import java.io.Serializable;

public class Route implements Serializable {

    private String name;
    private String description;
    private String region;
    private String latitude;
    private String longitude;
    private String picture;
    private String difficulty;
    private String endLatitude;
    private String endLongitude;
    private int length;

    public Route(String name, String description, String region, String latitude, String longitude, String picture, String difficulty, String endLatitude, String endLongitude, int length) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picture = picture;
        this.difficulty = difficulty;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.length = length;
    }


    public Route() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


}
