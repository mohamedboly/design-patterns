package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        // Création provisoire des composants avec un mediator null
        Button button = new Button(null);
        TextBox textBox = new TextBox(null);
        ListBox listBox = new ListBox(null);

        // Création du mediator avec les composants
        DialogMediator mediator = new DialogMediator(button, textBox, listBox);

        // Injection du mediator dans les composants
        button.mediator = mediator;
        textBox.mediator = mediator;
        listBox.mediator = mediator;

        // Simulation : clic sur le bouton
        button.click();
    }
}