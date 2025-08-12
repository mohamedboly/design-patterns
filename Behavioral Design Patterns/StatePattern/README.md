## Présentation

Presque tous les smartphones existants sur le marché, ou encore en conception, possèdent au moins deux boutons principaux : le bouton d’accueil et le bouton d’alimentation. Les actions réalisées par ces deux boutons varient selon l’état dans lequel se trouve le téléphone. Par exemple, si le téléphone est verrouillé, appuyer sur le bouton d’alimentation l’allume ; s’il est déverrouillé, appuyer sur le même bouton le verrouille.

L’exemple que nous venons de donner peut être illustré par une machine à états finis. L’idée principale est qu’à tout instant, un programme ne peut être que dans un nombre fini d’états. Dans chaque état, le programme se comporte différemment et, en fonction de cet état, on peut choisir de basculer immédiatement vers un autre état… ou de ne pas changer d’état du tout. Ces règles de bascule, appelées transitions, sont elles aussi finies et prédéterminées.

Revenons à notre exemple du smartphone et supposons que le téléphone puisse être dans trois états différents : éteint, verrouillé et prêt à l’emploi. Lorsque nous prenons le téléphone pour la première fois, il est éteint. Pour l’allumer, on peut utiliser le bouton d’alimentation ou le bouton d’accueil, mais dans tous les cas, le téléphone restera verrouillé : on ne peut pas encore accéder aux applications. Pour y accéder, il faut appuyer une seconde fois sur le bouton d’accueil. On ne peut pas utiliser le bouton d’alimentation cette fois, car il nous ramènerait à l’état éteint. Enfin, pendant l’utilisation, on peut à tout moment appuyer sur le bouton d’accueil pour revenir à l’écran d’accueil, ou sur le bouton d’alimentation pour l’éteindre à nouveau. Voilà la machine à états correspondant à notre exemple.

Ce concept est étroitement lié au patron de conception appelé State (État). Le State Pattern est un patron comportemental qui permet à un objet de modifier son comportement lorsque son état interne change ; on peut avoir l’impression que l’objet change de classe.

Pour aller plus loin dans cette définition, implémentons ensemble l’exemple du téléphone avec le State Pattern. La première chose dont nous avons besoin, c’est d’un contexte : quel est l’objet qui change d’état ? Ici, c’est le téléphone, donc nous pouvons créer une classe Phone. Cette classe contiendra l’état du téléphone, ainsi que plusieurs méthodes correspondant aux fonctionnalités du téléphone. Pour l’exemple, ces méthodes renverront simplement une chaîne de caractères.

Ensuite, nous définissons une classe State (État) qui sera abstraite. Elle stockera une instance du téléphone sur lequel elle agit et déclarera deux méthodes, chacune représentant l’un des boutons : que se passe-t-il quand on appuie sur le bouton d’accueil, et que se passe-t-il quand on appuie sur le bouton d’alimentation ? Cette classe doit être abstraite, car les états réels du téléphone seront chacun représentés par une classe concrète qui l’étend : OffState, LockedState et ReadyState.

Chaque classe d’état devra surcharger ces deux méthodes abstraites. L’implémentation concrète pourra consister à sauter vers un autre état (comme dans le diagramme de la machine à états) et/ou à invoquer une ou plusieurs des fonctionnalités définies dans la classe Phone. Et c’est tout : nos différents états du téléphone sont implémentés avec le State Pattern. Pour simuler cela, on peut imaginer deux JButtons avec des listeners reliés à ces méthodes ; selon l’état du téléphone, quand l’utilisateur clique sur l’un des deux boutons, le comportement correspondant sera exécuté.

Beaucoup de gens confondent ce pattern avec Strategy. Les deux reposent sur la composition : ils modifient le comportement du contexte en déléguant du travail à des objets auxiliaires. Cependant, les stratégies sont indépendantes et ne se connaissent pas entre elles, tandis que les états peuvent être dépendants et passer facilement l’un à l’autre. Strategy concerne différentes implémentations d’un même objectif (ex. plusieurs tris) où le résultat reste identique ; State consiste à faire des choses différentes selon l’état, et le résultat peut varier.

Plutôt que de multiplier les `switch` ou les `if/else` liés aux états dans votre code, le State Pattern appelle automatiquement le bon comportement grâce à la composition et au polymorphisme.

Pour résumer, le State Pattern permet à un objet de modifier son comportement quand son état interne change. Cela vous aide à éviter les structures `if/else` basées sur l’état et à extraire la logique dans des classes séparées : le contexte délègue le comportement à la classe d’état correspondante. En suivant ce pattern, vous appliquez également les principes de responsabilité unique et d’ouverture/fermeture.

## Étapes d’implémentation (checklist)

1. Définir l’interface (ou classe abstraite) d’état `State` avec les opérations dépendantes de l’état (ici `onHome()`, `onOffOn()`).
2. Créer une classe par état concret (`OffState`, `LockedState`, `ReadyState`) qui implémente `State` et contient les règles de transition (appels à `phone.setState(...)`).
3. Créer le **contexte** `Phone` : il conserve l’état courant (`State state`), l’initialise dans le constructeur et fournit un `setState(...)`.
4. Déléguer le comportement aux états : le contexte expose des méthodes côté UI/API (ex. `pressHome()`, `pressPower()`) qui appelleront les méthodes de l’état courant.
5. Centraliser les données/méthodes communes dans le contexte (ex. `turnOn()`, `lock()`, `unlock()`), et laisser les états décider **quand** les invoquer.
6. Éviter l’accès direct au champ d’état depuis l’extérieur : ne pas appeler `phone.state...` depuis le client ; utiliser les méthodes du contexte.
7. Tester les **transitions** : vérifier pour chaque séquence que l’état attendu est atteint et que les actions sont déclenchées.
8. (Optionnel) Optimiser : partager les instances d’états (pattern Singleton) si elles sont stateless, ou tracer les transitions (logs).


### Phone et State abstrait

```java
public class Phone {
    private State state;

    public Phone() {
        state = new OffState(this);
    }

    public void setState(State state) {
        this.state = state;
    }

    public String lock() {
        return "Locking phone and turning off the screen";
    }

    public String home() {
        return "Going to home-screen";
    }

    public String unlock() {
        return "Unlocking the phone to home";
    }

    public String turnOn() {
        return "Turning screen on, device still locked";
    }
}

public abstract class State {
    protected Phone phone;

    public State(Phone phone) {
        this.phone = phone;
    }

    public abstract String onHome();
    public abstract String onOffOn();
}
```

### États concrets

```java
public class OffState extends State {
    public OffState(Phone phone) {
        super(phone);
    }

    @Override
    public String onHome() {
        phone.setState(new LockedState(phone));
        return null; // dans la vidéo, on enchaîne surtout les transitions
    }

    @Override
    public String onOffOn() {
        phone.setState(new LockedState(phone));
        return null;
    }
}

public class LockedState extends State {
    public LockedState(Phone phone) {
        super(phone);
    }

    @Override
    public String onHome() {
        phone.setState(new ReadyState(phone));
        return null;
    }

    @Override
    public String onOffOn() {
        phone.setState(new OffState(phone));
        return null;
    }
}

public class ReadyState extends State {
    public ReadyState(Phone phone) {
        super(phone);
    }

    @Override
    public String onHome() {
        // reste en ReadyState (retour home)
        return null;
    }

    @Override
    public String onOffOn() {
        phone.setState(new OffState(phone));
        return null;
    }
}
```

### Démonstration (listeners Swing)

```java
public static void main(String[] args) {
    Phone phone = new Phone();
    JButton home = new JButton("Home");
    home.addActionListener(e -> phone.state.onHome());
    JButton onOff = new JButton("On/Off");
    onOff.addActionListener(e -> phone.state.onOffOn());
}
```

> Remarque : pour un code « production », encapsuler `state` dans `Phone` (ex. `pressHome()`, `pressPower()`) et effectuer de vraies actions (`phone.turnOn()`, `phone.lock()`).

## Diagramme de classes 
Représentation textuelle du diagramme State vu dans la vidéo :

```
+-------------------- Context --------------------+
| - state : State                                  |
| + changeState(State)                             |
| + doThis()                                       |
| + doThat()                                       |
+--------------------------------------------------+
                 |  délègue vers
                 v
+------------- <<interface>> State ---------------+
| + doSomething()                                  |
| + doMoreStuff()                                  |
+--------------------------------------------------+
                 ^             implémente
                 |-------------------------------+
                 |                               |
+-------------------- ConcreteState -------------+   (plusieurs variantes)
| - context : Context                             |
| + setContext(Context)                           |
| + doSomething()                                 |
| + doMoreStuff()                                 |
+-------------------------------------------------+

Client --> Context (le client invoque l'API publique du contexte)
Context --> State (le contexte délègue au State courant)
ConcreteState --> Context (accède au contexte pour déclencher transitions)
```
