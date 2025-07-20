package com.numeriquepro;

public abstract class RestaurantFactoryMethod {
    public Burger orderBurger() {
        Burger burger = createBurger();
        burger.prepare();
        return burger;
    }

    protected abstract Burger createBurger(); // Factory Method
}
