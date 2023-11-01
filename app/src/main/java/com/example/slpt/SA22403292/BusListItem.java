package com.example.slpt.SA22403292;

public class BusListItem {

    private String startStation;
    private String endStation;
    private String routeNumber;
    private String routeDesc;
    private boolean isStartToEnd;
    private float pricePerSeat;

    public BusListItem() {
    }

    public BusListItem(boolean isStartToEnd, float pricePerSeat) {
        this.isStartToEnd = isStartToEnd;
        this.pricePerSeat = pricePerSeat;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getRouteDesc() {
        return routeDesc;
    }

    public void setRouteDesc(String routeDesc) {
        this.routeDesc = routeDesc;
    }

    public boolean isStartToEnd() {
        return isStartToEnd;
    }

    public void setStartToEnd(boolean startToEnd) {
        isStartToEnd = startToEnd;
    }

    public float getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(float pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

}
