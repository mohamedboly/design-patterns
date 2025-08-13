package com.numeriquepro;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Client> clients = List.of(new Resident("Alice"), new Bank("AcmeBank"));
        ClientVisitor visitor = new InsuranceMessagingVisitor();
        for (Client c : clients) {
            c.accept(visitor); // double dispatch propre, sans instanceof
        }
    }
}