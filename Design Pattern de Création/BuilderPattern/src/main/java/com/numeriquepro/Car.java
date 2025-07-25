package com.numeriquepro;

class Car {
    private String brand;
    private String model;
    private String color;
    private int doors;
    private String screen;
    private double weight;
    private double height;

    // Constructeur package-private (accessible uniquement dans le même package)
    Car(String brand, String model, String color, int doors, String screen, double weight, double height) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.doors = doors;
        this.screen = screen;
        this.weight = weight;
        this.height = height;
    }
}