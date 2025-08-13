
## Présentation

Supponsons que nous avons une application de livraison. L’application permet de commander en ligne et, au final, de payer ce que vous avez acheté.

Supposons que nous ayons une classe PaymentService où le montant à payer nous est déjà fourni. Tout ce qu’il nous reste à faire est d’implémenter la méthode processOrder, qui va réellement exécuter le paiement et l’achat. D’accord, le paiement doit se faire par carte bancaire : la première étape consiste donc à récupérer les informations de carte du client, puis à valider ces informations, et enfin à procéder effectivement au paiement.

Cependant, des problèmes apparaissent si vous essayez d’ajouter à ce code — qui fonctionne déjà — d’autres moyens de paiement, comme PayPal. Dans ce cas, vous devrez entourer votre code actuel d’un if ou d’un switch et introduire le nouveau moyen de paiement avec toute sa logique propre. À court terme, cela peut sembler acceptable, mais à long terme ce sera difficile à maintenir, surtout si vous ajoutez encore d’autres moyens de paiement.

En fait, nous venons de créer un bloc de code « fermé à l’extension et ouvert à la modification », car chaque fois qu’un changement est requis, nous devrons rouvrir cette méthode et la modifier — ce qui va à l’encontre du principe Open/Closed que nous connaissons tous. De plus, cette classe gère plusieurs responsabilités : dans notre exemple, elle gère à la fois le paiement par carte et par PayPal, ce qui contrevient au principe de responsabilité unique (SRP).

Pour corriger cela, il nous faut une stratégie qui place chaque moyen de paiement dans sa propre classe, rendant chaque classe responsable d’un moyen donné. Et ces classes doivent être facilement interchangeables. Une manière d’y parvenir est d’appliquer le Strategy Pattern.

Le Strategy Pattern est un patron comportemental qui vous permet de définir une famille d’algorithmes, de mettre chacun d’eux dans une classe séparée et de rendre leurs objets interchangeables.

Appliquons-le à l’exemple du paiement. Comme indiqué, nous devons commencer par extraire la logique spécifique à chaque paiement dans sa propre classe. Dans notre exemple, nous aurons PaymentByCreditCard et PaymentByPayPal. Pour rendre ces classes interchangeables, vous devez créer une interface commune que toutes implémenteront. Vous pouvez aller plus loin en extrayant les comportements communs dans des méthodes séparées, au lieu d’entasser toute la logique dans une seule méthode.

Dans cet exemple, nous pouvons extraire les méthodes collectPaymentDetails, validatePaymentDetails et pay. Cela nous donnera plus de flexibilité par la suite, tout en dessinant le squelette de l’algorithme commun dont toutes les stratégies devraient se servir. Ces méthodes peuvent maintenant remplacer le morceau de code que nous avions écrit au début dans la classe PaymentService, à l’intérieur de laquelle une stratégie devra être définie. Ainsi, cette classe n’a plus de visibilité sur la manière dont le paiement est réalisé, et c’est très bien : elle utilise l’interface Strategy. De cette façon, nous pouvons ajouter de nouveaux algorithmes ou modifier ceux existants sans changer le code du service ni celui des autres stratégies.

Si un client veut utiliser le code que nous avons écrit, il lui suffit de définir la stratégie à employer et de la passer au service. Remarquez aussi que la stratégie peut maintenant être remplacée facilement à l’exécution.

Ce patron est souvent confondu avec le State Pattern.

La première chose à noter est la classe Context, qui correspondait à PaymentService dans notre exemple. Le contexte maintient une référence vers l’une des stratégies concrètes et communique avec cet objet uniquement via l’interface Strategy. Justement, l’interface Strategy est commune à toutes les stratégies concrètes : elle déclare une ou plusieurs méthodes utilisées par le contexte pour exécuter une stratégie. Les classes suivantes du diagramme sont les stratégies concrètes, représentées par PaymentByCreditCard et PaymentByPayPal. Ces classes fournissent leur propre implémentation des méthodes de la stratégie. Le contexte ne sait ni avec quel type de stratégie il travaille, ni comment l’algorithme est exécuté. Enfin, le client doit créer un objet stratégie spécifique et le passer au contexte, qui doit exposer un setter pour cette stratégie afin de permettre son remplacement à l’exécution.

On peut considérer State comme une extension de Strategy : les deux patrons reposent sur la composition et modifient le comportement du contexte en déléguant du travail à des objets auxiliaires. En revanche, les stratégies sont totalement indépendantes et ne se connaissent pas, tandis que les états peuvent être dépendants : on peut facilement passer d’un état à un autre, et un état peut dépendre d’un autre ; tous les états peuvent être conscients des autres. De plus, Strategy consiste à disposer de différentes implémentations qui accomplissent la même chose : par exemple, plusieurs algorithmes de tri — quel que soit celui que vous choisissez, le résultat (la liste triée) reste le même. Ou, dans notre cas, quel que soit le moyen de paiement choisi, le résultat est l’achat des articles. Le State Pattern, lui, consiste à faire des choses différentes selon l’état ; le résultat peut donc varier. Plutôt que d’avoir plusieurs switch ou if-else pour chaque état, le State Pattern appellera automatiquement le bon comportement grâce à la composition et au polymorphisme.

En résumé, le Strategy Pattern vous permet de définir une famille de comportements, de mettre chacun dans une classe séparée et de rendre leurs objets interchangeables. En plus de réduire la duplication de code, vos différents comportements — ou stratégies — seront facilement remplaçables et interchangeables par les clients à l’exécution. En suivant ce patron, vous appliquez aussi les principes de responsabilité unique et d’ouverture/fermeture : chaque stratégie est isolée dans sa propre classe, et vous pouvez introduire de nouvelles stratégies sans modifier ni les stratégies existantes, ni le contexte.

## Clarification : quand utiliser Strategy

Le pattern Strategy s’applique lorsqu’une **même finalité** peut être atteinte par **plusieurs algorithmes interchangeables**. Par exemple, « payer » est la finalité ; carte bancaire, PayPal, Apple Pay sont des stratégies différentes.

Principes clés

* Interface commune : chaque stratégie expose la même API et produit un résultat fonctionnellement équivalent.
* Interchangeable au runtime : on choisit la stratégie à l’exécution.
* SRP & OCP : chaque stratégie est isolée ; on en ajoute sans modifier le contexte.

Quand ce n’est pas Strategy

* Si le comportement dépend d’un état qui évolue dans le temps → **State**.
* Si les « stratégies » ne mènent pas à une finalité comparable → pas Strategy.

Exemples typiques

* Paiement (carte, PayPal, virement) → « payer »
* Compression (ZIP, GZIP) → « compresser »
* Tri (quick/merge/heap) → « trier »
* Itinéraire (voiture, vélo, marche) → « calculer un chemin »

## Étapes d’implémentation (checklist)

1. Définir l’interface Strategy commune (ex. `PaymentStrategy`) avec les opérations clés (`collectPaymentDetails()`, `validatePaymentDetails()`, `pay(amount)`).
2. Créer une classe par stratégie concrète (`PaymentByCreditCard`, `PaymentByPayPal`, …) qui implémente l’interface et encapsule sa propre logique.
3. Créer le **contexte** (ex. `PaymentService`) qui référence une `PaymentStrategy` et délègue l’exécution (ne connaît pas les détails).
4. Fournir un moyen d’**injection/sélection** de la stratégie (setter, constructeur, factory, configuration) et permettre son remplacement au runtime.
5. Respecter **SRP/OCP** : chaque stratégie ne fait qu’une chose ; ajouter une stratégie n’exige pas de modifier le contexte.
6. Tester chaque stratégie isolément (mocks/doubles de test), et tester le contexte avec plusieurs stratégies.
7. Optionnel : factoriser un squelette d’algorithme commun via **Template Method** si besoin, tout en gardant des stratégies interchangeables.

## Code — Paiements CB/PayPal (Java)

```java
// 1) Interface commune (Strategy)
interface PaymentStrategy {
    void collectPaymentDetails();
    boolean validatePaymentDetails();
    boolean pay(int amountInCents);
}

// 2) Stratégie concrète : Carte bancaire
class PaymentByCreditCard implements PaymentStrategy {
    private String cardNumber;
    private String cvv;
    private String expiry;

    @Override
    public void collectPaymentDetails() {
        // Ici : lire depuis un formulaire/UI. Démo : valeurs factices
        this.cardNumber = "4111 1111 1111 1111";
        this.cvv = "123";
        this.expiry = "12/27";
        System.out.println("[CB] Saisie des infos carte");
    }

    @Override
    public boolean validatePaymentDetails() {
        boolean ok = cardNumber != null && cvv != null && expiry != null;
        System.out.println("[CB] Validation: " + ok);
        return ok;
    }

    @Override
    public boolean pay(int amountInCents) {
        System.out.println("[CB] Paiement de " + amountInCents + " centimes");
        return true; // Simulation
    }
}

// 2) Stratégie concrète : PayPal
class PaymentByPayPal implements PaymentStrategy {
    private String email;
    private String password;

    @Override
    public void collectPaymentDetails() {
        // Démo : valeurs factices
        this.email = "user@example.com";
        this.password = "secret";
        System.out.println("[PayPal] Saisie identifiants");
    }

    @Override
    public boolean validatePaymentDetails() {
        boolean ok = email != null && password != null;
        System.out.println("[PayPal] Validation: " + ok);
        return ok;
    }

    @Override
    public boolean pay(int amountInCents) {
        System.out.println("[PayPal] Paiement de " + amountInCents + " centimes");
        return true; // Simulation
    }
}

// 3) Contexte : délègue à la stratégie active
class PaymentService {
    private PaymentStrategy strategy;

    public PaymentService(PaymentStrategy initial) {
        this.strategy = initial;
    }

    public void setStrategy(PaymentStrategy strategy) { // 4) Injection/remplacement
        this.strategy = strategy;
    }

    public boolean processOrder(int amountInCents) {
        strategy.collectPaymentDetails();
        if (!strategy.validatePaymentDetails()) {
            System.out.println("Détails de paiement invalides");
            return false;
        }
        return strategy.pay(amountInCents);
    }
}

// 4) Client : choisit/remplace la stratégie au runtime
class DemoStrategy {
    public static void main(String[] args) {
        PaymentService service = new PaymentService(new PaymentByCreditCard());
        boolean paid = service.processOrder(2499);
        System.out.println("Paiement CB: " + paid);

        service.setStrategy(new PaymentByPayPal());
        boolean paid2 = service.processOrder(3499);
        System.out.println("Paiement PayPal: " + paid2);
    }
}
```

## Diagramme de classes (ASCII)

```
+---------------- Context (PaymentService) ---------------+
| - strategy : PaymentStrategy                            |
| + setStrategy(s)                                        |
| + processOrder(amount)                                  |
+--------------------------+------------------------------+
                           |
                           | délègue
                           v
+-----------------<<interface>> PaymentStrategy-----------+
| + collectPaymentDetails()                               |
| + validatePaymentDetails()                              |
| + pay(amount)                                           |
+--------------------------+------------------------------+
            ^                                  ^
            |                                  |
            | implémente                        | implémente
            |                                  |
+-------------------- PaymentByCreditCard --------------+   +------------------- PaymentByPayPal ----------------+
| (détails carte, validations, paiement CB)             |   | (identifiants, validations, paiement PayPal)       |
+-------------------------------------------------------+   +-----------------------------------------------------+

Client ---> Context (choisit/remplace la stratégie)
Context ---> Strategy (interface commune, polymorphisme)
```

