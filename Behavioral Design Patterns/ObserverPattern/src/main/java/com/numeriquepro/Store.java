package com.numeriquepro;

import java.util.ArrayList;
import java.util.List;

// Étape 3 : Intégration métier (Store déclenche la notification)
public class Store {
    private final NotificationService notificationService; // Étape 2 : publisher

    public Store() {
        notificationService = new NotificationService();
    }

    public void newItemPromotion() {
        // Un événement survient : on notifie tous les abonnés
        notificationService.notifySubscribers();
    }

    public NotificationService getService() {
        return notificationService;
    }
}
