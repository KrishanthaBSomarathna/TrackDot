package com.example.slpt;

public class Route {
    private String routeNumber;
    private String routeDetails;

    public Route(String routeNumber, String routeDetails) {
        this.routeNumber = routeNumber;
        this.routeDetails = routeDetails;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public String getRouteDetails() {
        return routeDetails;
    }
}
