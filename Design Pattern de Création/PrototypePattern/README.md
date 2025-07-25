# Prototype Pattern - Comprendre et Implémenter le Clonage d’Objets en Java

## Objectif

Le but du **Prototype Pattern** est de **créer des copies d’objets** sans dépendre de leur classe concrète. C’est particulièrement utile quand :

* on ne connaît pas la classe exacte de l’objet,
* l’instanciation d’un objet est coûteuse,
* ou l’on souhaite éviter les constructeurs longs et répétitifs.

Ce pattern délègue le clonage **à l’objet lui-même**, via une méthode `clone()`.

---

## Pourquoi ce pattern ?

Prenons un exemple simple :

```java
Car carA = new Car();
carA.brand = "Bugatti";
carA.model = "Chiron";
carA.color = "Blue";
carA.topSpeed = 261;

Car carB = new Car();
carB.brand = carA.brand;
carB.model = carA.model;
carB.color = carA.color;
carB.topSpeed = carA.topSpeed;
```

On duplique manuellement les champs. Cela devient vite lourd, surtout si les attributs sont privés ou si on ne connaît pas la classe exacte (par exemple on manipule un `Vehicle`, sans savoir si c’est une `Car` ou un `Bus`).

La solution : demander à l’objet de **se cloner lui-même**.

---

## Étapes d’implémentation du Prototype Pattern

### Étape 1 : Définir une interface `Prototype`

```java
public interface Prototype {
    Prototype clone();
}
```

### Étape 2 : Implémenter cette interface dans vos classes (ex : `Car`)

```java
public class Car implements Prototype {
    private String brand;
    private String model;
    private String color;
    private int topSpeed;

    public Car(String brand, String model, String color, int topSpeed) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.topSpeed = topSpeed;
    }
    // Constructeur par copie
    public Car(Car car) {
        this.brand = car.brand;
        this.model = car.model;
        this.color = car.color;
        this.topSpeed = car.topSpeed;
    }

    @Override
    public Prototype clone() {
        return new Car(this);
    }
}
```

### Étape 3 : Utilisation

```java
Car carA = new Car("Bugatti", "Chiron", "Blue", 261);
Car carB = (Car) carA.clone();
```

---

## Shallow Copy vs Deep Copy

### Shallow Copy (copie superficielle)

Cela signifie que les objets imbriqués **ne sont pas clonés**, mais référencés :

```java
this.gps = car.gps; // même objet partagé
```

➡️ Toute modification sur `gps` impactera **toutes les copies**.

### Deep Copy (copie profonde)

Chaque objet imbriqué est **cloné indépendamment** :

```java
this.gps = car.gps.clone();
```

Chaque copie est **totalement indépendante** de l’original.

### 💡 Remarque importante

Lorsque tu veux un **deep copy**, tous les objets **imbriqués** doivent aussi **implémenter l’interface `Prototype`** ou fournir une méthode `clone()` dédiée. Cela permet de cloner chaque sous-objet proprement.

---

## Variante avec superclasse `Vehicle`

Si plusieurs classes partagent une même structure (par ex. `Car` et `Bus`), on peut mettre le comportement commun de clonage dans une superclasse :

### Superclasse

```java
public abstract class Vehicle implements Prototype {
    protected String brand;
    protected String model;
    protected String color;

    public Vehicle(Vehicle vehicle) {
        this.brand = vehicle.brand;
        this.model = vehicle.model;
        this.color = vehicle.color;
    }
}
```

### Sous-classe

```java
public class Car extends Vehicle {
    private int topSpeed;

    public Car(Car car) {
        super(car);
        this.topSpeed = car.topSpeed;
    }

    @Override
    public Prototype clone() {
        return new Car(this);
    }
}
```

### Avantage ?

Réutilisation du constructeur par copie pour les champs communs.

---

## Variante sans superclasse

Si chaque classe est indépendante, elles peuvent directement implémenter l’interface `Prototype`.

```java
public class Bus implements Prototype {
    private int wheels;

    public Bus(Bus bus) {
        this.wheels = bus.wheels;
    }

    @Override
    public Prototype clone() {
        return new Bus(this);
    }
}
```

---

## Prototype Registry

Il est souvent utile de créer un **catalogue d’objets prototypes prêts à être clonés** :

```java
public class VehicleRegistry {
    private Map<String, Prototype> prototypes = new HashMap<>();

    public VehicleRegistry() {
        prototypes.put("sportsCar", new Car("Bugatti", "Chiron", "Blue", 261));
        prototypes.put("cityBus", new Bus(6));
    }

    public Prototype get(String key) {
        return prototypes.get(key).clone();
    }
}
```

Tu appelles `registry.get("sportsCar")` pour obtenir un clone prêt à l’emploi !

---

## En résumé

| Avantage                               | Détail                                             |
| -------------------------------------- | -------------------------------------------------- |
| Pas de dépendance à la classe concrète | On clone sans savoir s’il s’agit de `Car` ou `Bus` |
| Réutilisable                           | On évite la duplication de logique de création     |
| Accès aux champs privés                | Le clone est interne à la classe                   |
| Performant                             | Utile si instanciation coûteuse                    |
| Deep Copy possible                     | Les objets imbriqués peuvent aussi être clonés     |

