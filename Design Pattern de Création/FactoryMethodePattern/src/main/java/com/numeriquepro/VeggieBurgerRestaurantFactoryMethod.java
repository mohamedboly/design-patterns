package com.numeriquepro;

public class VeggieBurgerRestaurantFactoryMethod extends RestaurantFactoryMethod{
    @Override
    protected Burger createBurger() {
        return new VeggieBurger();
    }
}
