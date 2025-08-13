package com.numeriquepro;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        MaCollection col = new MaCollection();
        col.add("A");
        col.add("B");
        col.add("C");

        System.out.println("Parcours par défaut :");
        for (String s : col) {
            System.out.println(s);
        }

        System.out.println("\nParcours inverse :");
        for (Iterator<String> it = col.reverseIterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }
}