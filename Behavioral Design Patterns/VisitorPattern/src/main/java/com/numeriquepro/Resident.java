package com.numeriquepro;

final class Resident extends Client {
    public Resident(String n){ super(n); }
    @Override public void accept(ClientVisitor v) { v.visitResident(this); }
}
