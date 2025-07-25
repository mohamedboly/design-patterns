package com.numeriquepro;

// Client
public class Main {
    public static void main(String[] args) {
        Light salon = new Light();
        RemoteControl remote = new RemoteControl();

        Command turnOn = new TurnOnCommand(salon);
        Command turnOff = new TurnOffCommand(salon);

        remote.setCommand(turnOn);
        remote.pressButton(); // Lumière allumée

        remote.setCommand(turnOff);
        remote.pressButton(); // Lumière éteinte
    }
}