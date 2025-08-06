package com.example.slpt;

public class Hotel {
    private String id;
    private String name;
    private String location;
    private String description;
    private double rating;
    private String imageUrl;
    private double price;

    public Hotel() {
        // Required empty constructor for Firebase
    }

    public Hotel(String id, String name, String location, String description, double rating, String imageUrl, double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}