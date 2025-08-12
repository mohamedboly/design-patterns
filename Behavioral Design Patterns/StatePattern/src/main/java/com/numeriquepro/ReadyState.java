package com.numeriquepro;

public class ReadyState extends State {
    public ReadyState(Phone phone) {
        super(phone);
    }

    @Override
    public String onHome() {
        // reste en ReadyState (retour home)
        return null;
    }

    @Override
    public String onOffOn() {
        phone.setState(new OffState(phone));
        return null;
    }
}