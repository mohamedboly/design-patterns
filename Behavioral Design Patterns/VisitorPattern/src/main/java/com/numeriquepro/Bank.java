package com.numeriquepro;

final class Bank extends Client {
    public Bank(String n){ super(n); }
    @Override public void accept(ClientVisitor v) { v.visitBank(this); }
}
