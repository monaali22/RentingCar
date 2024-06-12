package com.example.rentingcar;

public class CarAdmin {
    private int id;
    private String model;
    private String make_date;
    private double price;
    private String image_url;
    private String description;
    private double rating;
    private int num_available;

    public CarAdmin(int id, String model, String make_date, double price, String image_url, String description, double rating, int num_available) {
        this.id = id;
        this.model = model;
        this.make_date = make_date;
        this.price = price;
        this.image_url = image_url;
        this.description = description;
        this.rating = rating;
        this.num_available = num_available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake_date() {
        return make_date;
    }

    public void setMake_date(String make_date) {
        this.make_date = make_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public int getNum_available() {
        return num_available;
    }

    public void setNum_available(int num_available) {
        this.num_available = num_available;
    }
// Getters and Setters
}
