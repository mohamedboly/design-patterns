package com.numeriquepro;

public class OffState extends State {
    public OffState(Phone phone) {
        super(phone);
    }

    @Override
    public String onHome() {
        phone.setState(new LockedState(phone));
        return null; // dans la vidéo, on enchaîne surtout les transitions
    }

    @Override
    public String onOffOn() {
        phone.setState(new LockedState(phone));
        return null;
    }
}
