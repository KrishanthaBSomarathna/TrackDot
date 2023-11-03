package com.example.slpt.SA22410122;

public class list {
    public static class PassengerRequest {
        private String name;
        private String contactNumber;
        private String currentLocation;
        private String destinationLocation;

        public PassengerRequest() {
            // Default constructor required for Firebase
        }

        public PassengerRequest(String name, String contactNumber, String currentLocation, String destinationLocation) {
            this.name = name;
            this.contactNumber = contactNumber;
            this.currentLocation = currentLocation;
            this.destinationLocation = destinationLocation;
        }

        public String getName() {
            return name;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public String getDestinationLocation() {
            return destinationLocation;
        }
    }
}
