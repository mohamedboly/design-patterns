package com.numeriquepro;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Phone phone = new Phone();
        JButton home = new JButton("Home");
        home.addActionListener(e -> phone.pressHome());
        JButton onOff = new JButton("On/Off");
        onOff.addActionListener(e -> phone.pressPower());
    }
}