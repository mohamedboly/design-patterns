# Chain of Responsibility Pattern - Gérez les requêtes par une chaîne dynamique

## Définition

Le **Chain of Responsibility Pattern** est un pattern **comportemental (behavioral)** qui permet de transformer des **comportements spécifiques** en objets autonomes appelés **handlers**.

Lorsqu’une requête est envoyée, elle circule dans une **chaîne de handlers**. Chaque handler peut :

* soit **traiter la requête**,
* soit **la passer au suivant** dans la chaîne.

Ce modèle favorise la **découplage entre l’émetteur et le récepteur** d’une requête.

---

## Contexte introductif

Imagine que tu es tranquillement en train de regarder Netflix, quand soudain tu reçois un SMS indiquant que ta carte bancaire a été suspendue.

Tu appelles la banque. Un robot vocal te demande si tu préfères l’anglais. Tu attends. Un opérateur décroche, ne comprend rien. Il te transfère vers un autre. Encore un autre. Jusqu’à tomber sur la bonne personne.

Ce que tu viens de vivre, c’est un **enchaînement de traitements successifs** : chacun vérifie s’il peut résoudre ton problème. Sinon, il passe la requête au suivant.

C’est exactement ce que modélise le **Chain of Responsibility Pattern**.

---

## Quand utiliser ce pattern ?

Quand tu as plusieurs vérifications ou traitements à appliquer **dans un ordre déterminé**, mais que :

* tu ne veux pas écrire un gros `if/else` ou `switch`,
* l’ordre ou le contenu des traitements peut changer dynamiquement,
* les responsabilités doivent être **séparées** pour plus de clarté et de flexibilité.

---

## Étapes d'implémentation sans BaseHandler (exemple authentification)

### 1. Créer la classe `Handler` (interface ou classe abstraite)

```java
public abstract class Handler {
    protected Handler next;

    public void setNext(Handler next) {
        this.next = next;
    }

    public abstract boolean handle(String username, String password);
}
```

### 2. Implémenter des handlers concrets :

#### `UserExistsHandler`

```java
public class UserExistsHandler extends Handler {
    private Database database;

    public UserExistsHandler(Database db) {
        this.database = db;
    }

    @Override
    public boolean handle(String username, String password) {
        if (!database.userExists(username)) {
            System.out.println("Utilisateur non trouvé");
            return false;
        }
        return next != null && next.handle(username, password);
    }
}
```

#### `PasswordCheckHandler`

```java
public class PasswordCheckHandler extends Handler {
    private Database database;

    public PasswordCheckHandler(Database db) {
        this.database = db;
    }

    @Override
    public boolean handle(String username, String password) {
        if (!database.passwordIsValid(username, password)) {
            System.out.println("Mot de passe incorrect");
            return false;
        }
        return next != null && next.handle(username, password);
    }
}
```

#### `RoleCheckHandler`

```java
public class RoleCheckHandler extends Handler {
    @Override
    public boolean handle(String username, String password) {
        if (username.equals("admin")) {
            System.out.println("Admin connecté");
        }
        return next != null && next.handle(username, password);
    }
}
```

### 3. Créer la classe `AuthenticationService`

```java
public class AuthenticationService {
    private Handler handler;

    public AuthenticationService(Handler handler) {
        this.handler = handler;
    }

    public boolean login(String username, String password) {
        return handler.handle(username, password);
    }
}
```

### 4. Chaîner les handlers dans le `main` ou le `client`

```java
Database db = new Database();
Handler userCheck = new UserExistsHandler(db);
Handler passCheck = new PasswordCheckHandler(db);
Handler roleCheck = new RoleCheckHandler();

userCheck.setNext(passCheck);
passCheck.setNext(roleCheck);

AuthenticationService auth = new AuthenticationService(userCheck);
auth.login("admin", "1234");
```
### 5. Simulation d'une base de donnee avec `Database`

```java
package com.numeriquepro;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, String> users = new HashMap<>();

    public Database() {
        users.put("admin", "1234");
        users.put("user", "pass");
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public boolean passwordIsValid(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

}

```
---

## Version avec `BaseHandler` (optionnel mais utile)

### Pourquoi ?

Évite de dupliquer la logique de passage au `next` dans chaque handler. On centralise ça dans un **comportement par défaut**.

### Exemple `BaseHandler`

```java
public abstract class BaseHandler {
    protected BaseHandler next;

    public BaseHandler setNext(BaseHandler next) {
        this.next = next;
        return next;
    }

    public boolean handle(String username, String password) {
        if (next != null) return next.handle(username, password);
        return true;
    }
}
```

### `UserExistsHandler` (hérite de `BaseHandler`)

```java
public class UserExistsHandler extends BaseHandler {
    private Database database;

    public UserExistsHandler(Database db) {
        this.database = db;
    }

    @Override
    public boolean handle(String username, String password) {
        if (!database.userExists(username)) {
            System.out.println("Utilisateur non trouvé");
            return false;
        }
        return super.handle(username, password);
    }
}
```

Autres handlers restent similaires mais appellent `super.handle()` au lieu de `next.handle()`.

---

## Avantages du pattern

* Permet d'**organiser dynamiquement** les traitements.
* **Facile à étendre** (ajouter ou retirer des handlers).
* **Responsabilités bien séparées** : chaque handler gère un seul aspect.
* Plus de gros blocs de `if/else` ou de `switch` imbriqués.

---

## Cas d’usage fréquents

* Authentification ou validation d’un formulaire complexe
* Traitement d’événements utilisateur
* Middleware HTTP
* Traitement d’erreurs ou logs conditionnels

---

## Récapitulatif

* Crée des classes pour chaque traitement
* Chaîne-les dynamiquement
* Chaque handler traite ou transmet
* Le client déclenche la chaîne

> C’est un modèle simple, efficace, testable et ultra flexible !
