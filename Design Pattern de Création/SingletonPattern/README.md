# 🧠 Le Singleton Pattern en Java

## 🎯 Objectif

Le **Singleton** est un *design pattern de création* qui garantit qu'une **seule instance** d'une classe existe dans toute l'application, et fournit un **point d'accès global** à cette instance.

---

## 📌 Exemple concret

Un pays a **un seul président** ou **un seul gouvernement** en fonction. Peu importe qui est en poste, il n'y en a qu’un. Ce poste est un **point d’accès unique** à une entité globale. Le Singleton suit la même logique en programmation.

---

## 🛠️ Étapes de création d’un Singleton

1. **Créer un attribut `private static`** pour stocker l’instance unique.
2. **Définir un constructeur `private`** pour empêcher les autres classes de faire `new`.
3. **Ajouter une méthode `public static getInstance()`** pour retourner l’instance unique.
4. (Optionnel mais recommandé) **Rendre l’instance `volatile`** et utiliser une **double vérification synchronisée** si plusieurs threads peuvent accéder au Singleton.

---

## 💡 Implémentation de base

```java
public class Singleton {
    private static Singleton instance;
    private String data;

    private Singleton() {
        data = "Données partagées";
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public String getData() {
        return data;
    }
}
```

---

## ⚠️ Problème avec les threads

En environnement multi-thread, deux threads peuvent créer **deux instances** en même temps. ❌

---

## ✅ Solution : synchronized

```java
public static synchronized Singleton getInstance() {
    if (instance == null) {
        instance = new Singleton();
    }
    return instance;
}
```

**Inconvénient :** ralentit même les lectures après création.

---

## 🚀 Solution avancée : double-checked locking avec `volatile`

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### 🔒 Pourquoi `volatile` ?

`volatile` empêche qu’un thread voie une **instance partiellement construite**.

---

## 🧪 Optimisation finale : éviter deux lectures mémoire

```java
public static Singleton getInstance() {
    Singleton result = instance;
    if (result == null) {
        synchronized (Singleton.class) {
            result = instanc
```
