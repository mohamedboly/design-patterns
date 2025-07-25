package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        Car car = new CarBuilder()
                .brand("Bugatti")
                .model("Veyron")
                .color("Red")
                .doors(2)
                .screen("LCD")
                .weight(1200.5)
                .height(1.2)
                .build();
        // Exemple avec seulement quelques attributs
        Car carSimple = new CarBuilder()
                .brand("Tesla")
                .model("Model S")
                .build();

        System.out.println(carSimple);

        System.out.println(car);
    }
}