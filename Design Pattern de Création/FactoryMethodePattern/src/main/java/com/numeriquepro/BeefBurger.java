package com.numeriquepro;

public class BeefBurger extends Burger{
    private String angus;
    @Override
    public void prepare() {
        System.out.println("Préparation du BeefBurger...");
    }
}
