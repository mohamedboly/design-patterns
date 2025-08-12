package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        // Étape 6 : abonnements
        store.getService().subscribe(new EmailMsgListener("alice@mail.com"));
        store.getService().subscribe(new EmailMsgListener("bob@mail.com"));

        // Étape 3 : un événement métier survient
        store.newItemPromotion();
    }
}