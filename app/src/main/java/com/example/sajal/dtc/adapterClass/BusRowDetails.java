package com.example.sajal.dtc.adapterClass;

public class BusRowDetails {
    private String busNumber;
    private String startingTime;
    private String endingTime;
    private String availability;
    private String busName;
    private String rootCode;
    private String busFares;

    private BusRowDetails() {

    }


    private BusRowDetails(String busNumber, String startingTime, String endingTime, String availability, String busName, String rootCode, String busFares) {
        this.busNumber = busNumber;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.availability = availability;
        this.busName = busName;
        this.rootCode = rootCode;
        this.busFares = busFares;


    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusFares() {
        return busFares;
    }

    public void setBusFares(String busFares) {
        this.busFares = busFares;
    }

    public String getRootCode() {
        return rootCode;
    }

    public void setRootCode(String rootCode) {
        this.rootCode = rootCode;
    }
}
