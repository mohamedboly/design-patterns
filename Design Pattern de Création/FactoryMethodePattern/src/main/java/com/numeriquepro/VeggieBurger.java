package com.numeriquepro;

public class VeggieBurger extends Burger{
    private String combo;
    @Override
    public void prepare() {
        System.out.println("Préparation du VeggieBurger...");
    }
}
