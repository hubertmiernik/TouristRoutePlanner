package com.example.touristrouteplanner.model;

import com.android.volley.toolbox.StringRequest;

import java.io.Serializable;

public class Route implements Serializable {



    private String name;
    private String description;
    private String region;
    private double latitude;
    private double longitude;
    private String picture;
    private String difficulty;
    private double endLatitude;
    private double endLongitude;
    private String length;
    private String distance = null;
    private String duration =  null;
    private Double distanceFromPoint;

    public Route(String name, String description, String region, double latitude, double longitude, String picture, String difficulty, double endLatitude, double endLongitude, String length, String distance, String duration) {
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
        this.distance = distance;
        this.duration = duration;
    }

    public Route(String name, String description, String region, double latitude, double longitude, String picture, String difficulty, double endLatitude, double endLongitude, String length, Double distanceFromPoint) {
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
        this.distanceFromPoint = distanceFromPoint;
    }

    public Route(String name, String description, String region, double latitude, double longitude, String picture, String difficulty, double endLatitude, double endLongitude, String length) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getDistanceFromPoint() {
        return distanceFromPoint;
    }

    public void setDistanceFromPoint(Double distanceFromPoint) {
        this.distanceFromPoint = distanceFromPoint;
    }

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", distanceFromPoint=" + distanceFromPoint +
                '}';
    }


}
