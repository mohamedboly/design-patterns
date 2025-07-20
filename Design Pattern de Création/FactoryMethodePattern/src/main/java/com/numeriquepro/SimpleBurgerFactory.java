package com.numeriquepro;

public class SimpleBurgerFactory {
    public Burger createBurger(String type) {
        if ("BEEF".equals(type)) {
            return new BeefBurger();
        } else if ("VEGGIE".equals(type)) {
            return new VeggieBurger();
        }
        return null;
    }
}