# Pattern Observer – Explication et Implémentation

## Introduction

Imaginons que vous possédez un magasin où vous vendez de tout. Un client vous demande un article précis que vous n’avez pas en stock pour l’instant, mais qui arrivera plus tard. Deux options s’offrent à vous :

* Le client passe tous les jours vérifier s’il est arrivé, la plupart du temps pour rien.
* Vous envoyez un e‑mail à tous vos clients à chaque nouvel arrivage, y compris à ceux qui ne sont pas intéressés.

## Problème

Il faut un mécanisme d’abonnement où chaque client choisit s’il veut être notifié, quand, et pour quel type d’événement. C’est exactement ce que fournit le pattern Observer.

## Définition

Le pattern Observer est un patron comportemental qui notifie automatiquement plusieurs objets abonnés lorsqu’un événement se produit dans un autre objet observé.

## Étapes d’implémentation (checklist)

1. **Définir le contrat d’abonné** : créer une interface (ex. `EventListener`) avec la méthode `update(...)`.
2. **Concevoir le publisher** : créer un service (ex. `NotificationService`) qui maintient les abonnés et expose `subscribe(...)`, `unsubscribe(...)`, `notify(...)`.
3. **Intégrer dans la logique métier** : la classe domaine (ex. `Store`) déclenche `notify(...)` quand un événement survient.
4. **Supporter plusieurs événements** : introduire `enum Event { ... }` et stocker une `Map<Event, List<EventListener>>` ; adapter `subscribe/unsubscribe/notify`.
5. **Rendre extensible** : ajouter de nouveaux canaux via de nouveaux `ConcreteListener` sans modifier le publisher (Open/Closed).
6. **Prévoir la désinscription** : toujours fournir `unsubscribe(...)`.
7. **Assurer la robustesse** : ne pas laisser l’échec d’un listener bloquer les autres notifications.

## Exemple 1 : un seul événement (implémentation minimale)

```java
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

// Démonstration
class DemoSingleEvent {
    public static void main(String[] args) {
        Store store = new Store();
        // Étape 6 : abonnements
        store.getService().subscribe(new EmailMsgListener("alice@mail.com"));
        store.getService().subscribe(new EmailMsgListener("bob@mail.com"));

        // Étape 3 : un événement métier survient
        store.newItemPromotion();
    }
}
```

## Exemple 2 : plusieurs événements (abonnements différenciés)

```java
import java.util.*;

// Étape 4 : définir les événements supportés
enum Event {
    NEW_ITEM,
    SALE
}

// Étape 1 : contrat d'abonné
interface EventListener {
    void update(Event event, String message);
}

// Étape 5 : abonné e-mail (un canal)
class EmailMsgListener implements EventListener {
    private final String email;

    public EmailMsgListener(String email) {
        this.email = email;
    }

    @Override
    public void update(Event event, String message) {
        System.out.println("[EMAIL][" + event + "] -> " + email + " : " + message);
    }
}

// Étape 5 : abonné mobile (autre canal)
class MobileAppListener implements EventListener {
    private final String username;

    public MobileAppListener(String username) {
        this.username = username;
    }

    @Override
    public void update(Event event, String message) {
        System.out.println("[PUSH][" + event + "] -> " + username + " : " + message);
    }
}

// Étape 2 et 4 : Publisher multi-événements
class NotificationService {
    private final Map<Event, List<EventListener>> listeners = new EnumMap<>(Event.class);

    public NotificationService() {
        for (Event e : Event.values()) {
            listeners.put(e, new ArrayList<>()); // liste d'abonnés par événement
        }
    }

    // Étape 6 : abonnement à un événement précis
    public void subscribe(Event event, EventListener listener) {
        listeners.get(event).add(listener);
    }

    // Étape 6 : désinscription d'un événement précis
    public void unsubscribe(Event event, EventListener listener) {
        listeners.get(event).remove(listener);
    }

    // Étape 2 : notifier uniquement les abonnés de l'événement concerné
    public void notify(Event event, String message) {
        for (EventListener l : listeners.get(event)) {
            try {
                l.update(event, message);
            } catch (Exception ex) {
                // Étape 7 : robustesse — ne pas bloquer la boucle
                System.err.println("Erreur de notification: " + ex.getMessage());
            }
        }
    }
}

// Étape 3 : intégration domaine (Store émet des événements)
class Store {
    private final NotificationService notifications = new NotificationService();

    public NotificationService getNotifications() { return notifications; }

    public void newItemArrived(String itemName) {
        notifications.notify(Event.NEW_ITEM, "Nouveau produit : " + itemName);
    }

    public void startSale(String details) {
        notifications.notify(Event.SALE, "Promotion : " + details);
    }
}

// Démonstration : abonnés à un seul, à l'autre, et à plusieurs événements
class DemoMultiEvents {
    public static void main(String[] args) {
        Store store = new Store();

        // Inscrits seulement à NEW_ITEM
        store.getNotifications().subscribe(Event.NEW_ITEM, new EmailMsgListener("alice@mail.com"));
        store.getNotifications().subscribe(Event.NEW_ITEM, new MobileAppListener("alice_app"));

        // Inscrits seulement à SALE
        store.getNotifications().subscribe(Event.SALE, new EmailMsgListener("bob@mail.com"));
        store.getNotifications().subscribe(Event.SALE, new MobileAppListener("bob_app"));

        // Inscrits aux deux événements
        EventListener carolEmail = new EmailMsgListener("carol@mail.com");
        EventListener carolPush  = new MobileAppListener("carol_app");
        store.getNotifications().subscribe(Event.NEW_ITEM, carolEmail);
        store.getNotifications().subscribe(Event.SALE,     carolEmail);
        store.getNotifications().subscribe(Event.NEW_ITEM, carolPush);
        store.getNotifications().subscribe(Event.SALE,     carolPush);

        // Étape 3 : événements déclenchés
        store.newItemArrived("Console X Pro");
        store.startSale("-30% sur accessoires");
    }
}
```

## Différence avec le pattern Mediator

**Intention**

* Observer : publication/abonnement à des événements. Quand X change, prévenir les abonnés de X.
* Mediator : orchestration centrale des interactions entre composants pour réduire les dépendances directes.

**Structure**

* Observer : un publisher, N subscribers reliés via une interface commune. Abonnements dynamiques.
* Mediator : un médiateur avec plusieurs collègues ; la logique de coordination est centralisée.

**Usages typiques**

* Observer : notifications, flux d’événements, temps réel.
* Mediator : coordination de composants UI/formulaires, flux multi-modules.

## Résumé

Observer permet d’exécuter des actions sur un ensemble d’objets dès qu’un autre change d’état, avec une forte extensibilité (Open/Closed) et un couplage réduit entre émetteur et abonnés. Mediator centralise la logique d’échanges entre plusieurs composants pour éviter le couplage direct et concentrer les règles d’orchestration en un endroit unique.
