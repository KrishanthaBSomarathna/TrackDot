package com.example.slpt.SA22403810;

public class BusDriver {

    private boolean isSaved;
    private String status,seatcount,LocationName, bustype, roadnumber,vehicleNum,r1start,r1stop,r2start,r2stop,startdestination,stopdestination;

    private Double  Latitude,Longitude;
    public BusDriver() {
    }

    public BusDriver(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String getSeatcount() {
        return seatcount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getLocationName() {
        return LocationName;
    }

    public String getBustype() {
        return bustype;
    }

    public String getRoadnumber() {
        return roadnumber;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public String getR1start() {
        return r1start;
    }

    public String getR1stop() {
        return r1stop;
    }

    public String getR2start() {
        return r2start;
    }

    public String getR2stop() {
        return r2stop;
    }

    public String getStartdestination() {
        return startdestination;
    }

    public String getStopdestination() {
        return stopdestination;
    }

    public String getStatus() {
        return status;
    }
}
