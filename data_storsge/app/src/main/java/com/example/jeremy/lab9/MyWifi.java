package com.example.jeremy.lab9;

public class MyWifi {
    private String name;
    private Integer level;
    private String timestamp;
    private Double lat;
    private Double lon;

    public MyWifi(String timestamp, String name, Integer level, Double lat, Double lon) {
        this.timestamp = timestamp;
        this.name = name;
        this.level = level;
        this.lat = lat;
        this.lon = lon;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer genre) {
        this.level = genre;
    }
}