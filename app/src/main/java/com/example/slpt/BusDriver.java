package com.example.slpt;

public class BusDriver {
    private String startdestination;
    private String stopdestination;

    public BusDriver(String startdestination, String stopdestination) {
        this.startdestination = startdestination;
        this.stopdestination = stopdestination;
    }

    public String getStartdestination() {
        return startdestination;
    }

    public void setStartdestination(String startdestination) {
        this.startdestination = startdestination;
    }

    public String getStopdestination() {
        return stopdestination;
    }

    public void setStopdestination(String stopdestination) {
        this.stopdestination = stopdestination;
    }
}

