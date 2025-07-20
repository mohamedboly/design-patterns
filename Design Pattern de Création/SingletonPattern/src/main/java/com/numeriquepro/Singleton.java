package com.numeriquepro;

public class Singleton {
    private static Singleton instance;
    private String data;

    private Singleton() {
        data = "Données partagées";
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public String getData() {
        return data;
    }
}
