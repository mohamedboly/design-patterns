package com.numeriquepro;

public class RestaurantOld {

    public Burger orderBurger(String request) {
        if ("BEEF".equals(request)) {
            BeefBurger burger = new BeefBurger();
            burger.prepare();
            return burger;
        } else if ("VEGGIE".equals(request)) {
            VeggieBurger burger = new VeggieBurger();
            burger.prepare();
            return burger;
        }
        return null;
    }
}
