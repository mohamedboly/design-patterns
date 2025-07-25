package com.numeriquepro;

public class Car implements Prototype {
    private String brand;
    private String model;
    private String color;
    private int topSpeed;

    public Car(String brand, String model, String color, int topSpeed) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.topSpeed = topSpeed;
    }

    // Constructeur par copie
    public Car(Car car) {
        this.brand = car.brand;
        this.model = car.model;
        this.color = car.color;
        this.topSpeed = car.topSpeed;
    }

    @Override
    public Prototype clone() {
        return new Car(this);
    }
}
