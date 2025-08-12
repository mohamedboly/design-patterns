package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        TextArea textArea = new TextArea();
        Editor editor = new Editor(textArea);

        editor.write("Bonjour");
        editor.write("Bonjour le monde");
        editor.write("Bonjour le monde entier");

        System.out.println(textArea.getText()); // Bonjour le monde entier

        editor.undo();
        System.out.println(textArea.getText()); // Bonjour le monde

        editor.undo();
        System.out.println(textArea.getText()); // Bonjour
    }
}