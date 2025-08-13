# Pattern Iterator en Java – Guide et Exemple

## 1. Introduction

Le **pattern Iterator** est un patron de conception comportemental qui permet de parcourir les éléments d'une collection **sans exposer sa structure interne**. Il centralise la logique de parcours dans un objet dédié (l’itérateur) et fournit au client une interface simple (`hasNext()` / `next()`).

En Java :

* `Iterator<T>` définit les méthodes `hasNext()` et `next()`.
* `Iterable<T>` définit `iterator()`, qui renvoie un `Iterator<T>` et permet l’utilisation dans un `for-each`.

 L’Iterator Design Pattern est un patron de conception comportemental dont l’idée principale est d’extraire le comportement de parcours d’une collection dans un objet séparé appelé itérateur.

## 2. Principes essentiels

* Tous les itérateurs implémentent `Iterator<T>`.
* `next()` ne retourne jamais `null` : en fin de parcours, il lance `NoSuchElementException`.
* Une collection doit implémenter `Iterable<T>` pour être utilisable avec `for-each`.
* Toujours indiquer le type générique `<T>` pour un typage fort.
* Une même collection peut fournir plusieurs itérateurs (avant, arrière, filtré…).

Cela permet à plusieurs itérateurs de parcourir la même collection indépendamment l’un de l’autre.

## 3. Exemple : Collection de chaînes avec deux itérateurs

```java
import java.util.*;

public class MaCollection implements Iterable<String> {
    private final List<String> elements = new ArrayList<>();

    public void add(String e) {
        elements.add(e);
    }

    @Override
    public Iterator<String> iterator() {
        return new ForwardIterator();
    }

    public Iterator<String> reverseIterator() {
        return new ReverseIterator();
    }

    private class ForwardIterator implements Iterator<String> {
        private int index = 0;
        public boolean hasNext() { return index < elements.size(); }
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(index++);
        }
    }

    private class ReverseIterator implements Iterator<String> {
        private int index = elements.size() - 1;
        public boolean hasNext() { return index >= 0; }
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(index--);
        }
    }
}
```

### Utilisation

```java
public class Test {
    public static void main(String[] args) {
        MaCollection col = new MaCollection();
        col.add("A");
        col.add("B");
        col.add("C");

        System.out.println("Parcours par défaut :");
        for (String s : col) {
            System.out.println(s);
        }

        System.out.println("\nParcours inverse :");
        for (Iterator<String> it = col.reverseIterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }
}
```

## 4. Points clés à retenir

* Le `for-each` utilise toujours l’itérateur renvoyé par `iterator()`.
* Pour changer le mode de parcours, exposez d’autres méthodes qui retournent des `Iterator<T>`.
* Les classes internes pour les itérateurs ne sont pas obligatoires, mais elles sont adaptées ici car ces itérateurs ne servent qu’à cette collection.
* Ce pattern évite la duplication de code de parcours et respecte les principes **Single Responsibility** et **Open/Closed**.

En suivant le pattern Iterator, vous appliquez le principe de responsabilité unique et le principe ouvert/fermé, car chaque algorithme de parcours est extrait dans une classe séparée.

## 5. Gestion de la concurrence et bonnes pratiques avancées

Lorsqu’une collection est modifiée pendant qu’un itérateur est en cours d’utilisation, cela peut provoquer des comportements imprévisibles (lecture de données incohérentes, erreurs…).

En Java, les collections standards implémentent un mécanisme **fail-fast** :

* La collection maintient un compteur de modifications (`modCount`).
* Chaque itérateur mémorise la valeur de ce compteur au moment de sa création (`expectedModCount`).
* Avant chaque accès (`hasNext()` / `next()`), l’itérateur compare les deux valeurs. Si elles diffèrent, il lance `ConcurrentModificationException`.

### Exemple d’amélioration avec fail-fast

```java
private int modCount = 0;

public void add(String e) {
    elements.add(e);
    modCount++;
}

private class ForwardIterator implements Iterator<String> {
    private int index = 0;
    private final int expectedModCount = modCount;

    public boolean hasNext() {
        checkForComodification();
        return index < elements.size();
    }
    public String next() {
        checkForComodification();
        if (!hasNext()) throw new NoSuchElementException();
        return elements.get(index++);
    }
    private void checkForComodification() {
        if (expectedModCount != modCount) {
            throw new ConcurrentModificationException();
        }
    }
}
```

### Autres approches selon les besoins

* **Collections synchronisées** (`Collections.synchronizedList`) pour un accès multi-thread sûr.
* **CopyOnWriteArrayList** pour des scénarios où la lecture est majoritaire par rapport aux modifications.
* **Itérateurs immuables** : travailler sur un snapshot (copie de la collection au moment de l’itération).

Ces techniques permettent de rendre le parcours plus sûr en environnement concurrent tout en conservant la clarté du pattern Iterator.
