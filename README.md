# Design Pattern : C'est quoi ?

## 🌍 Introduction

Pour bien comprendre les Design Patterns, prenons un exemple concret : **imaginons un steak**.

Vous avez peut-être imaginé un steak bien cuit, une autre personne un steak à point, et une autre encore un steak végétarien.
Au restaurant, chacun commande son steak selon ses préférences. Pourtant, le cuisinier utilise la même base (viande ou substitut) et adapte la cuisson selon la commande. Résultat : un plat personnalisé pour chacun à partir du même ingrédient.

Transposons cela à un programme informatique : imaginons une classe `Chef` avec une méthode `prepare()`.
Cette méthode peut recevoir un objet `WellDoneInstruction`. Le chef saura alors préparer un steak bien cuit.
Mais pour gérer d'autres cuissons, on devrait surcharger la méthode pour inclure `MediumDoneInstruction`, `VeganInstruction`, etc.
Cela devient vite ingérable si le chef apprend 5 nouvelles cuissons… Allons-nous créer 5 nouvelles méthodes ?

⭐ **Solution :** Créer une classe `SteakCookingInstruction` et faire hériter toutes les instructions de celle-ci.
Ainsi, une seule méthode `prepare()` dans la classe `Chef` suffit, et elle est capable de gérer toutes les instructions grâce au polymorphisme.

C'est ce qu’on appelle un bon design : notre classe est **fermée à la modification mais ouverte à l’extension** (*Open/Closed Principle*).
Plus besoin de modifier la classe `Chef` à chaque fois. On peut juste créer une nouvelle instruction qui hérite, et ça fonctionnera directement.

Imaginons maintenant que ce chef ouvre une nouvelle succursale. À chaque nouveau client, il prend le temps d’expliquer les détails d’ouverture de la nouvelle branche.
N'est-il pas plus logique qu’il publie un message sur les réseaux sociaux ou envoie une newsletter ? Il toucherait beaucoup plus de monde.

Ces deux scénarios illustrent des **Design Patterns** :
Ils ne résolvent pas directement les problèmes, mais proposent une manière efficace de penser et de structurer le code à travers des modèles éprouvés — des pratiques standardisées qui ont déjà permis à d'autres développeurs de résoudre les mêmes problèmes.

Les Design Patterns ont été popularisés par un livre culte écrit par **Erich Gamma, Richard Helm, Ralph Johnson et John Vlissides**, souvent appelés le **Gang of Four (GoF)**.

Le livre, publié dans les années 90, décrit **23 Design Patterns** classés en trois grandes catégories :

---

### 1. Les patrons de création (*Creational Patterns*)

Ils concernent la manière de créer des objets.
Plutôt que d’instancier les objets de manière rigide, ces patrons apportent de la flexibilité dans leur création.

Par exemple, si vous avez une classe avec 10 attributs, mais que vous ne voulez pas tous les définir à chaque fois, vous avez deux options peu élégantes :

* Créer plein de constructeurs surchargés
* Créer un constructeur avec tous les paramètres, mais devoir passer `null` pour ceux que vous n’utilisez pas

✅ Le **Builder Pattern** propose une alternative plus propre et maintenable. C’est une solution largement utilisée dans l’industrie.

---

### 2. Les patrons structurels (*Structural Patterns*)

Ils se concentrent sur la manière dont les classes et objets sont organisés pour former de plus grandes structures.

Exemple : vous gérez des milliers de livres dans une bibliothèque, et beaucoup ont les mêmes données (auteur, entrepôt, éditeur, etc.).
Créer un objet distinct pour chaque livre gaspille de la mémoire.

✅ Le **Flyweight Pattern** permet de partager les données communes et économiser des ressources.

---

### 3. Les patrons comportementaux (*Behavioral Patterns*)

Ils traitent de la communication entre les objets et la manière dont les responsabilités sont distribuées pendant l’exécution.

Les deux exemples vus au début illustrent bien :

* Le **Strategy Pattern** : choisir dynamiquement un comportement
* Le **Memento Pattern** : sauvegarder l’état d’un objet et pouvoir le restaurer

---

### 🔄 En résumé

Les Design Patterns :

* ⏱ Accélèrent le développement
* 🔧 Fournissent des solutions testées et réutilisables
* 📊 Facilitent la maintenance et l’évolution du code

Avec le temps et la pratique, vous les utiliserez naturellement.

---

## 🌟 Exemple simple : la cuisson d'un steak (Open/Closed en Java)

```java
public interface SteakCookingInstruction {
    void cook();
}

public class WellDoneSteak implements SteakCookingInstruction {
    public void cook() {
        System.out.println("Cuisson bien cuite : 10 minutes");
    }
}

public class MediumSteak implements SteakCookingInstruction {
    public void cook() {
        System.out.println("Cuisson à point : 5 minutes");
    }
}

public class VeganSteak implements SteakCookingInstruction {
    public void cook() {
        System.out.println("Cuisson vegan : 3 minutes");
    }
}

public class Chef {
    public void prepare(SteakCookingInstruction instruction) {
        instruction.cook();
    }
}

public class Main {
    public static void main(String[] args) {
        Chef chef = new Chef();
        chef.prepare(new WellDoneSteak());
        chef.prepare(new MediumSteak());
        chef.prepare(new VeganSteak());
    }
}
```

✅ Avantage : la classe `Chef` ne change jamais. On ajoute des comportements sans modifier le code existant.
