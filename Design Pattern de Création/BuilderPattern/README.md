# Builder Pattern - Explication Simple + Exemple Complet en Java

##  Objectif

Comprendre pourquoi et comment utiliser le **Builder Pattern** pour créer des objets complexes en Java, en évitant les constructeurs monstrueux et en améliorant la lisibilité du code.

---

##  Définition

Le **Builder Pattern** est un patron de conception créationnel qui permet de construire des objets complexes **pas à pas**. Il permet aussi de créer **différentes représentations d'un même objet** avec le même processus de construction.

---

## 🛡 Sécuriser la création avec un constructeur privé

Pour forcer les développeurs à utiliser le `Builder` et éviter la création directe de l’objet avec `new`, il est recommandé de **rendre le constructeur de ********`Car`******** privé ou package-private**.

```java
// Exemple : constructeur non public pour éviter l’instanciation directe
class Car {
    private String brand;
    private String model;
    private String color;
    private int doors;
    private String screen;
    private double weight;
    private double height;

    //  Constructeur package-private (accessible uniquement dans le même package)
    Car(String brand, String model, String color, int doors, String screen, double weight, double height) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.doors = doors;
        this.screen = screen;
        this.weight = weight;
        this.height = height;
    }
}
```

Ainsi, seul le `Builder` peut instancier l'objet, garantissant un usage correct.

---

## Problème

Prenons une classe `Car` qui contient beaucoup de champs :

```java
public class Car {
    private String brand;
    private String model;
    private String color;
    private int doors;
    private String screen;
    private double weight;
    private double height;

    // constructeur complexe
    public Car(String brand, String model, String color, int doors, String screen, double weight, double height) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.doors = doors;
        this.screen = screen;
        this.weight = weight;
        this.height = height;
    }

    // getters (et possiblement toString)
}
```

Appeler ce constructeur est difficile à lire et à maintenir, surtout si on ne veut fournir que certains champs.

---

## Solution : Le Builder Pattern

### 1. Le Builder

```java
public class CarBuilder {
    private String brand;
    private String model;
    private String color;
    private int doors;
    private String screen;
    private double weight;
    private double height;

    public CarBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }

    public CarBuilder model(String model) {
        this.model = model;
        return this;
    }

    public CarBuilder color(String color) {
        this.color = color;
        return this;
    }

    public CarBuilder doors(int doors) {
        this.doors = doors;
        return this;
    }

    public CarBuilder screen(String screen) {
        this.screen = screen;
        return this;
    }

    public CarBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }

    public CarBuilder height(double height) {
        this.height = height;
        return this;
    }

    public Car build() {
        return new Car(brand, model, color, doors, screen, weight, height);
    }
}
```

### 2. Utilisation

```java


// Exemple complet

public class Main {
    public static void main(String[] args) {
        Car car = new CarBuilder()
                .brand("Bugatti")
                .model("Veyron")
                .color("Red")
                .doors(2)
                .screen("LCD")
                .weight(1200.5)
                .height(1.2)
                .build();
      // Exemple avec seulement quelques attributs
Car carSimple = new CarBuilder()
        .brand("Tesla")
        .model("Model S")
        .build();

System.out.println(carSimple);

        System.out.println(car);
    }
}
```

---

## Et si plusieurs configurations sont récurrentes ?

Si tu veux créer **plusieurs objets `Car` avec des attributs récurrents**, le Builder peut être combiné avec un **Director**.

> Le **Director** est une classe qui centralise la logique de construction répétée. Elle est utile quand tu veux créer des objets avec des configurations pré-définies (ex. toutes les Bugatti ou Tesla ont certains attributs communs).

Exemple : plutôt que de réécrire la configuration d’une Bugatti dans chaque classe, le `Director` garde ce code à un seul endroit.

On peut utiliser un **Director** qui contient la logique de construction d'un objet de façon réutilisable.

```java
public class CarDirector {
    public void constructBugatti(CarBuilder builder) {
        builder.brand("Bugatti")
               .model("Veyron")
               .color("Black")
               .doors(2)
               .screen("Touch")
               .weight(1300.0)
               .height(1.2);
    }

    public void constructLamborghini(CarBuilder builder) {
        builder.brand("Lamborghini")
               .model("Aventador")
               .color("Yellow")
               .doors(2)
               .screen("LED")
               .weight(1350.0)
               .height(1.15);
    }
}
```

### Exemple avec Director

```java
public class Main {
    public static void main(String[] args) {
        CarBuilder builder = new CarBuilder();
        CarDirector director = new CarDirector();

        director.constructBugatti(builder);
        Car bugatti = builder.build();

        director.constructLamborghini(builder);
        Car lambo = builder.build();

        System.out.println(bugatti);
        System.out.println(lambo);
    }
}
```

---

## Avantages

* Plus lisible que les constructeurs avec 8+ arguments.
* Flexible : tu peux construire partiellement.
* Réutilisable via le `Director`.
* Sépare la création de la logique métier.

---

## Sans Builder

```java
Car car = new Car("Bugatti", "Veyron", "Red", 2, "LCD", 1200.5, 1.2); // difficile à lire !
```

---

## En résumé

| Critère               | Avec Builder | Sans Builder                       |
| --------------------- | ------------ | ---------------------------------- |
| Lisibilité du code    | Haute        | Basse                              |
| Maintenance           | Facile       | Difficile (surtout avec surcharge) |
| Construction flexible | Oui          | Non                                |
| Respect SOLID (SRP)   | Oui          | Non                                |

