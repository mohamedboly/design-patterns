package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        RestaurantFactoryMethod beefRestaurant = new BeefBurgerRestaurantFactoryMethod();
        beefRestaurant.orderBurger();

        RestaurantFactoryMethod veggieRestaurant = new VeggieBurgerRestaurantFactoryMethod();
        veggieRestaurant.orderBurger();
    }
}