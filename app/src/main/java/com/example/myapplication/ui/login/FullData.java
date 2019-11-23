package com.example.myapplication.ui.login;

public class FullData {

    static private FullData fullData;
    private int id;
    private double latitude;
    private double longitude;
    private String pass;


    static {
        fullData = new FullData();
    }

    private FullData(){
        latitude = 0;
        latitude = 0;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public static FullData getFullData() {
        return fullData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString(){
        return getId() + " " + getLatitude() + " " + getLongitude();
    }

    public String getUrlSufix(){
        return "?id=" + fullData.getId() + "&lat=" + fullData.getLatitude() + "&lon=" + fullData.getLongitude();
    }
}
