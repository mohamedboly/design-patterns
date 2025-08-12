package com.numeriquepro;

// (Variante minimale sans interface) Abonné e-mail avec une méthode update
class EmailMsgListener {
    private final String email;

    public EmailMsgListener(String email) {
        this.email = email;
    }

    public void update() {
        // Étape 7 : ici on enverrait réellement l'e-mail
        System.out.println("Email envoyé à " + email + " : Nouveau produit disponible.");
    }
}
