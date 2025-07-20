package com.numeriquepro;

public class BeefBurgerRestaurantFactoryMethod extends RestaurantFactoryMethod{
    @Override
    protected Burger createBurger() {
        return new BeefBurger();
    }
}
