package com.numeriquepro;

import java.util.ArrayList;
import java.util.List;

// Étape 2 : Publisher simple (un seul type d'événement)
class NotificationService {
    private final List<EmailMsgListener> customers; // collection d'abonnés

    public NotificationService() {
        customers = new ArrayList<>();
    }

    // Étape 6 : permettre l'abonnement
    public void subscribe(EmailMsgListener listener) {
        customers.add(listener);
    }

    // Étape 6 : permettre la désinscription
    public void unsubscribe(EmailMsgListener listener) {
        customers.remove(listener);
    }

    // Étape 2 : notification des abonnés
    public void notifySubscribers() {
        customers.forEach(EmailMsgListener::update);
    }
}
