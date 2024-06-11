package com.example.rentingcar;

public class Car {
    private int id;
    private String model;

    private double price;
    private int make_date;

    private double totalPrice; // New field for total price

    public Car(int id, String model , double price , int make_date) {
        this.price = price;
        this.id = id;
        this.model = model;
        this.make_date = make_date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }
    public double getprice() {
        return price;
    }


    @Override
    public String toString() {

        return "Car Model: " + model + "\n\nCare Make Date : " + make_date+"\n\n"+"Price for your booking : "+totalPrice+"\n\n";
    }

    public String getModel() {
        return model;
    }
}
