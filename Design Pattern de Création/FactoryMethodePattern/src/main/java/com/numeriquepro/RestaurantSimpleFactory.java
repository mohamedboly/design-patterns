package com.numeriquepro;

public class RestaurantSimpleFactory {
    private final SimpleBurgerFactory factory;

    public RestaurantSimpleFactory(SimpleBurgerFactory factory) {
        this.factory = factory;
    }

    public Burger orderBurger(String type) {
        Burger burger = factory.createBurger(type);
        burger.prepare();
        return burger;
    }
}