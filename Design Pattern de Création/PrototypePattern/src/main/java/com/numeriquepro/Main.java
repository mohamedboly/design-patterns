package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        Car carA = new Car("Bugatti", "Chiron", "Blue", 261);
        Car carB = (Car) carA.clone();
    }
}