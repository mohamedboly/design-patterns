package com.numeriquepro;

abstract class Client {
    private final String name;
    protected Client(String name) { this.name = name; }
    public String name() { return name; }
    public abstract void accept(ClientVisitor visitor);
}
