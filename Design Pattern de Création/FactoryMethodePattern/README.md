# 🏭 Factory Method Design Pattern en Java

## 🎯 Objectif

Le pattern **Factory Method** est un *design pattern de création* qui permet de déléguer la création d’un objet à une sous-classe, tout en masquant son type exact au code client. Il améliore la flexibilité, respecte les principes SOLID et permet une meilleure extensibilité.

---

## 🍔 Cas de départ : `Restaurant` qui crée des burgers

```java
public class Restaurant {
    public Burger orderBurger(String request) {
        if ("BEEF".equals(request)) {
            BeefBurger burger = new BeefBurger();
            burger.prepare();
            return burger;
        } else if ("VEGGIE".equals(request)) {
            VeggieBurger burger = new VeggieBurger();
            burger.prepare();
            return burger;
        }
        return null;
    }
}

class BeefBurger {
    int productId;
    String angus;
    String addOns;

    void prepare() {
        System.out.println("Préparation du BeefBurger...");
    }
}

class VeggieBurger {
    int productId;
    String combo;
    String addOns;

    void prepare() {
        System.out.println("Préparation du VeggieBurger...");
    }
}
```

🔻 **Problèmes** :

* Violation du **principe Open/Closed** : chaque ajout de burger → modification de `Restaurant`
* Violation du **principe Single Responsibility** : `orderBurger` fait deux choses → créer + préparer

---

## 🛠️ Refactoring avec une `SimpleBurgerFactory`

```java
public class SimpleBurgerFactory {
    public Burger createBurger(String type) {
        if ("BEEF".equals(type)) {
            return new BeefBurger();
        } else if ("VEGGIE".equals(type)) {
            return new VeggieBurger();
        }
        return null;
    }
}
```

Utilisation dans `Restaurant` :

```java
public class Restaurant {
    private final SimpleBurgerFactory factory;

    public Restaurant(SimpleBurgerFactory factory) {
        this.factory = factory;
    }

    public Burger orderBurger(String type) {
        Burger burger = factory.createBurger(type);
        burger.prepare();
        return burger;
    }
}
```

✅ Avantages : séparation de responsabilités
❌ Inconvénients : `SimpleBurgerFactory` viole toujours **Open/Closed** → nouvelle condition à chaque nouveau type

---

## 🚀 Transition vers le **Factory Method Pattern**

### Étape 1 : abstraction de `Burger`

```java
public interface Burger {
    void prepare();
}
```

### Étape 2 : Implémentations concrètes

```java
public class BeefBurger implements Burger {
    public void prepare() {
        System.out.println("Préparation du Beef Burger");
    }
}

public class VeggieBurger implements Burger {
    public void prepare() {
        System.out.println("Préparation du Veggie Burger");
    }
}
```

### Étape 3 : Classe `Restaurant` abstraite avec Factory Method

```java
public abstract class Restaurant {
    public Burger orderBurger() {
        Burger burger = createBurger();
        burger.prepare();
        return burger;
    }

    protected abstract Burger createBurger(); // Factory Method
}
```

### Étape 4 : Sous-classes concrètes qui créent les bons burgers

```java
public class BeefBurgerRestaurant extends Restaurant {
    @Override
    protected Burger createBurger() {
        return new BeefBurger();
    }
}

public class VeggieBurgerRestaurant extends Restaurant {
    @Override
    protected Burger createBurger() {
        return new VeggieBurger();
    }
}
```

### Utilisation

```java
public class Main {
    public static void main(String[] args) {
        Restaurant beefRestaurant = new BeefBurgerRestaurant();
        beefRestaurant.orderBurger();

        Restaurant veggieRestaurant = new VeggieBurgerRestaurant();
        veggieRestaurant.orderBurger();
    }
}
```

---

## 🧠 Quand utiliser Factory Method ?

Utilise ce pattern lorsque :

* Tu ne connais pas à l’avance le type exact d’objet à créer
* Tu veux respecter **Open/Closed** et permettre des extensions faciles
* Tu veux déléguer la création d’objet sans modifier le code client

---

## ⚠️ Limites

| ❗ Limite                               | Détail                                |
| -------------------------------------- | ------------------------------------- |
| Multiplication des sous-classes        | Une sous-classe par type de produit   |
| Couplage fort à l’héritage             | Moins souple que la composition       |
| Difficile avec des produits dynamiques | Préfère Abstract Factory ou stratégie |

---

## 🔄 Résumé comparaison : Simple Factory vs Factory Method

| Critère               | Simple Factory      | Factory Method                     |
| --------------------- | ------------------- | ---------------------------------- |
| Type                  | Idiome              | Design Pattern GoF                 |
| Couplage              | Centralisé, fort    | Faible via héritage et abstraction |
| OCP respecté ?        | ❌                   | ✅                                  |
| Ajout de nouveau type | Modifier la factory | Ajouter une nouvelle sous-classe   |

---

Ce pattern prépare aussi le terrain pour des patterns plus évolués comme **Abstract Factory**, **Builder** ou **Dependency Injection**.

