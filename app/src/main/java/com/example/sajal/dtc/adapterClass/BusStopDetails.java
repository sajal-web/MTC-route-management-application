package com.example.sajal.dtc.adapterClass;

public class BusStopDetails {
    private String bus_time;
    private String stopage_name;
    private String km_details;

    private BusStopDetails(){}

    private BusStopDetails(String bus_time, String stopage_name, String  km_details){
        this.bus_time = bus_time;
        this.stopage_name = stopage_name;
        this.km_details = km_details;

    }

    public String getBus_time() {
        return bus_time;
    }

    public void setBus_time(String bus_time) {
        this.bus_time = bus_time;
    }

    public String getStopage_name() {
        return stopage_name;
    }

    public void setStopage_name(String stopage_name) {
        this.stopage_name = stopage_name;
    }

    public String getKm_details() {
        return km_details;
    }

    public void setKm_details(String km_details) {
        this.km_details = km_details;
    }
}
