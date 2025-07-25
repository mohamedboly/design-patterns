package com.numeriquepro;

public class CarBuilder {
    private String brand;
    private String model;
    private String color;
    private int doors;
    private String screen;
    private double weight;
    private double height;

    public CarBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }

    public CarBuilder model(String model) {
        this.model = model;
        return this;
    }

    public CarBuilder color(String color) {
        this.color = color;
        return this;
    }

    public CarBuilder doors(int doors) {
        this.doors = doors;
        return this;
    }

    public CarBuilder screen(String screen) {
        this.screen = screen;
        return this;
    }

    public CarBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }

    public CarBuilder height(double height) {
        this.height = height;
        return this;
    }

    public Car build() {
        return new Car(brand, model, color, doors, screen, weight, height);
    }
}