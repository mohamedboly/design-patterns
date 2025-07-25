package com.numeriquepro;
// Receiver
public class Light {
    private boolean isOn;

    public void turnOn() {
        isOn = true;
        System.out.println("Lumière allumée");
    }

    public void turnOff() {
        isOn = false;
        System.out.println("Lumière éteinte");
    }

    public boolean isOn() {
        return isOn;
    }
}
