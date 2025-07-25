# Abstract Factory Pattern - Simplified Guide with Code Examples

## ✨ Objectif

Ce fichier README explique **simplement et progressivement** pourquoi et quand passer du **Factory Method Pattern** au **Abstract Factory Pattern**, en  montrant leurs relations, limites, et comment ils s'enchaînent logiquement.

---

## 🧵 1. Démarrage : Factory Method

### Contexte

Supposons que notre entreprise fabrique des composants d'ordinateur, en particulier **des cartes graphiques (GPU)** pour deux marques : **MSI** et **ASUS**.

### Interface commune

```java
public interface Gpu {
    void assemble();
}
```

### Implémentations concrètes

```java
public class MsiGpu implements Gpu {
    @Override
    public void assemble() {
        // business logic relevant to MSI
    }
}

public class AsusGpu implements Gpu {
    @Override
    public void assemble() {
        // business logic relevant to ASUS
    }
}
```

### Factory Method Pattern

```java
public abstract class Company {
    public Gpu assembleGpu() {
        Gpu gpu = createGpu();
        gpu.assemble();
        return gpu;
    }

    public abstract Gpu createGpu();
}

public class MsiManufacturer extends Company {
    @Override
    public Gpu createGpu() {
        return new MsiGpu();
    }
}

public class AsusManufacturer extends Company {
    @Override
    public Gpu createGpu() {
        return new AsusGpu();
    }
}
```

### ✅ Résultat

Le **Factory Method** fonctionne bien pour **un seul type de produit**.

---

## ⚠️ 2. Limite du Factory Method

L'entreprise commence à produire un **deuxième produit** : des **moniteurs (screens)** pour les mêmes marques MSI et ASUS.

Tu peux être tenté de généraliser `Gpu` en `Component` :

```java
public interface Component {
    void assemble();
}
```

Et ensuite :

```java
public class MsiGpu implements Component {
    @Override
    public void assemble() {
        // logic relevant to MSI GPUs
    }
}

public class AsusGpu implements Component {
    @Override
    public void assemble() {
        // logic relevant to ASUS GPUs
    }
}

public class MsiMonitor implements Component {
    @Override
    public void assemble() {
        // logic relevant to MSI monitors
    }
}

public class AsusMonitor implements Component {
    @Override
    public void assemble() {
        // logic relevant to ASUS monitors
    }
}
```

Et dans la factory :

```java

public abstract class Company {
    public abstract Component createComponent(String type);
}

public class MsiManufacturer extends Company {
    @Override
    public Component createComponent(String type) {
        if ("GPU".equals(type)) {
            return new MsiGpu();
        } else {
            return new MsiMonitor();
        }
    }
}

public class AsusManufacturer extends Company {
    @Override
    public Component createComponent(String type) {
        if ("GPU".equals(type)) {
            return new AsusGpu();
        } else {
            return new AsusMonitor();
        }
    }
}
```

### ❌ Problème : Violation du principe Open/Close

Si on veut ajouter un **nouveau produit** demain (ex: clavier), on doit **modifier** la factory existante : ce qui va **contre** le principe d'ouverture/fermeture.

---

## 🚀 3. Solution : Abstract Factory Pattern

On reconnaît qu'on a besoin de créer **des familles d'objets liés**. Ici, la **famille**, c'est une marque (MSI ou ASUS). Et les objets liés sont : GPU + Monitor de la même marque.

### Interfaces séparées pour chaque type de produit

```java
public interface Gpu {
    void assemble();
}

public interface Monitor {
    void assemble();
}
```

### Implémentations concrètes

```java
public class MsiGpu implements Gpu {
    @Override
    public void assemble() {
        // MSI GPU logic
    }
}

public class AsusGpu implements Gpu {
    @Override
    public void assemble() {
        // ASUS GPU logic
    }
}

public class MsiMonitor implements Monitor {
    @Override
    public void assemble() {
        // MSI Monitor logic
    }
}

public class AsusMonitor implements Monitor {
    @Override
    public void assemble() {
        // ASUS Monitor logic
    }
}
```

### Abstract Factory (Company)

```java
public abstract class Company {
    public abstract Gpu createGpu();
    public abstract Monitor createMonitor();
}
```

### Factories concrètes (par marque)

```java
public class MsiManufacturer extends Company {
    @Override
    public Gpu createGpu() {
        return new MsiGpu();
    }

    @Override
    public Monitor createMonitor() {
        return new MsiMonitor();
    }
}

public class AsusManufacturer extends Company {
    @Override
    public Gpu createGpu() {
        return new AsusGpu();
    }

    @Override
    public Monitor createMonitor() {
        return new AsusMonitor();
    }
}
```

---

## 💻 Utilisation côté client

```java
public static void main(String[] args) {
    Company msi = new MsiManufacturer();
    Gpu msiGpu = msi.createGpu();
    Monitor msiMonitor = msi.createMonitor();

    Company asus = new AsusManufacturer();
    Gpu asusGpu = asus.createGpu();
    Monitor asusMonitor = asus.createMonitor();
}
```

---

## ✨ En résumé

| Critère   | Factory Method                      | Abstract Factory                                  |
| --------- | ----------------------------------- | ------------------------------------------------- |
| Produit   | 1 seul type de produit              | Plusieurs produits liés (famille)                 |
| Évolution | Violable si plusieurs types ajoutés | Respecte Open/Close                               |
| Objectif  | Déléguer la création                | Fournir une famille complète d'objets compatibles |

---

## ❓ Quand passer de Factory Method → Abstract Factory ?

* Quand tu dois **créer plusieurs objets** qui **appartiennent à une même famille** (ex: marque, thème, plateforme).
* Quand tu veux **garder le code ouvert à l'extension mais fermé à la modification** (Open/Close Principle).
* Quand tu veux garantir que des objets **travaillent bien ensemble** (compatibilité).

-