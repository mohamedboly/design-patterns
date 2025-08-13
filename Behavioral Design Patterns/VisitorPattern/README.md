# Pattern Visitor – Guide Complet

## 1. Introduction
Le **pattern Visitor** est un patron de conception comportemental qui permet de **séparer les algorithmes** (opérations) des objets sur lesquels ils opèrent. Il devient particulièrement pertinent quand la **hiérarchie d’objets est stable** mais que **les opérations à appliquer évoluent ou se multiplient**.

Dans cette fiche : 
- Les **premières solutions** courantes et leurs **limites** (POJO, Service, surcharge)
- Le **passage au Visitor** avec code
- Une **explication claire du double dispatch**
- Un **comparatif Visitor vs Service métier**
- Un **arbre de décision** pour choisir entre **POJO, Service, Visitor**
- Un **schéma** et les **étapes d’implémentation**


## 2. Cas de départ – Exemple assurance
On modélise des `Client` (par ex. `Resident`, `Bank`) pour une compagnie d’assurance.

### Modèle de base
```java
abstract class Client { 
    private final String name;
    protected Client(String name) { this.name = name; }
    public String name() { return name; }
}
final class Resident extends Client { public Resident(String n){ super(n);} }
final class Bank     extends Client { public Bank(String n){ super(n);} }
```

### Demande métier
Le manager vous demande d’implémenter une fonctionnalité de messagerie publicitaire : selon le type de client, un email spécifique est envoyé.

## 3. Premières solutions et limites

### 3.1 Tout dans le POJO (polymorphisme simple)
```java
abstract class Client {
    private final String name;
    protected Client(String name) { this.name = name; }
    public String name() { return name; }
    public abstract void sendMail();
}

final class Resident extends Client {
    public Resident(String n){ super(n); }
    @Override public void sendMail() { System.out.println("Email santé → " + name()); }
}
final class Bank extends Client {
    public Bank(String n){ super(n); }
    @Override public void sendMail() { System.out.println("Email vol → " + name()); }
}

List<Client> clients = List.of(new Resident("Alice"), new Bank("AcmeBank"));
clients.forEach(Client::sendMail);
```
**Limites** :
- **SRP** violé : les entités font de l’infrastructure (messagerie)
- **Open/Closed** : si on multiplie les opérations (PDF, audit, export…), on “salit” toute la hiérarchie

### 3.2 Service métier avec `instanceof`
```java
final class EmailCampaignService {
    public void envoyerEmail(Client client) {
        if (client instanceof Resident r) {
            System.out.println("Email santé → " + r.name());
        } else if (client instanceof Bank b) {
            System.out.println("Email vol → " + b.name());
        }
    }
}
```
**Limites** :
- Prolifération de `instanceof`
- Couplage fort à tous les types concrets

### 3.3 Surcharge naïve (ne marche pas)
```java
final class Messaging {
    public void send(Resident r) { /* … */ }
    public void send(Bank b)     { /* … */ }
}

Client c = new Resident("Alice");
new Messaging().send(c); // Erreur, car type statique = Client → pas de résolution de send(Resident)
```
**Pourquoi ?** La surcharge se résout **à la compilation** selon le **type statique** du paramètre. Avec une référence typée `Client`, le compilateur ne choisit pas `send(Resident)`.


## 4. Étapes pour implémenter le pattern Visitor
1. **Définir l’interface `Element`** (ou une classe abstraite) avec la méthode `accept(Visitor)`.
2. **Créer les classes concrètes** (`ElementA`, `ElementB`…) qui implémentent `accept` et **redirigent** l’appel vers la méthode `visitXxx` appropriée.
3. **Définir l’interface `Visitor`** avec une méthode `visit` **par type concret** d’élément.
4. **Implémenter un ou plusieurs visiteurs concrets** qui portent chaque opération (messagerie, audit, export…)
5. **Dans le code client**, itérer sur la collection d’éléments et appeler `element.accept(visitor)`.

Chaque objet va accepter un visiteur et lui dire quelle méthode de visite doit être exécutée.

## 5. Schéma du pattern Visitor

- **`<<interface>> Element`** : contrat que chaque élément concret doit respecter (`accept(Visitor)`).
- **`ElementA`, `ElementB`** : classes concrètes (ex. `Resident`, `Bank`).
- **`<<interface>> Visitor`** : définit les méthodes `visit` pour chaque type d’élément.
- **`ConcreteVisitor`** : implémentations concrètes des comportements (ex. `InsuranceMessagingVisitor`).
- **`Client`** : détient les éléments et applique un `Visitor` sur chacun.

## 6. Implémentation du pattern Visitor

L’interface Visitor déclare un ensemble de méthodes de visite qui peuvent prendre comme arguments des éléments concrets de la structure d’objets.

### Interfaces et éléments concrets
```java
interface ClientVisitor {
    void visitResident(Resident resident);
    void visitBank(Bank bank);
}

abstract class Client {
    private final String name;
    protected Client(String name) { this.name = name; }
    public String name() { return name; }
    public abstract void accept(ClientVisitor visitor);
}

final class Resident extends Client {
    public Resident(String n){ super(n); }
    @Override public void accept(ClientVisitor v) { v.visitResident(this); }
}
final class Bank extends Client {
    public Bank(String n){ super(n); }
    @Override public void accept(ClientVisitor v) { v.visitBank(this); }
}
```

### Un visiteur concret (messagerie)
```java
final class InsuranceMessagingVisitor implements ClientVisitor {
    @Override public void visitResident(Resident r) {
        System.out.println("Email santé → " + r.name());
    }
    @Override public void visitBank(Bank b) {
        System.out.println("Email vol → " + b.name());
    }
}
```

### Utilisation
```java
List<Client> clients = List.of(new Resident("Alice"), new Bank("AcmeBank"));
ClientVisitor visitor = new InsuranceMessagingVisitor();
for (Client c : clients) {
    c.accept(visitor); // double dispatch propre, sans instanceof
}
```

**Avantages** :
- Pas de `instanceof`
- **Double dispatch** : le bon `visitXxx` est choisi dynamiquement
- **Open/Closed (pour les opérations)** : ajouter une **nouvelle opération** = créer **un nouveau visiteur**

**Inconvénients** :
- Ajouter un **nouveau type** concret impose d’ajouter une méthode dans **tous** les visiteurs


## 7. Double dispatch expliqué simplement

- **Single dispatch** (classique en Java) : le choix dynamique se fait sur le **receveur** de la méthode.
- **Double dispatch** (Visitor) :
    1. `c.accept(visitor)` → choix selon le **type réel** de `c` (ex. `Resident.accept`)
    2. `visitor.visitResident(this)` → choix de la surcharge **correspondant au type concret** passé

Déléguer le choix de la méthode appropriée à l’objet lui‑même, au lieu de laisser le client choisir une méthode

## 8. Visitor vs Service métier

| Critère | Visitor | Service métier |
|---------|---------|----------------|
| **Extension des opérations** | Très facile : nouveau visiteur | Nouvelle méthode dans le service |
| **Extension des types** | Coûteux : modifier tous les visiteurs | Plus simple |
| **Dispatch** | Double dispatch, sans `instanceof` | Simple dispatch, souvent `instanceof` |
| **Couplage** | Couplé à la hiérarchie (`accept`) | Découplé des types (paramètres) |
| **Idéal quand** | Hiérarchie **stable**, opérations **nombreuses/évolutives** | Opérations **stables**, hiérarchie qui **évolue** |


## 9. Décider : POJO, Service ou Visitor ?

**Arbre de décision rapide** :
1. **Dépendance à l’infra** (SMTP/HTTP/PDF/DB) ? → **Service métier**
2. **Hiérarchie stable** + **nouvelles opérations fréquentes** ? → **Visitor**
3. **Règle métier pure, locale aux données** ? → **POJO** (entité/VO)
4. **Besoin de double dispatch** sans `instanceof` ? → **Visitor**

**Smells** :
- Trop de `instanceof` → penser Visitor
- Trop d’infra dans les entités → extraire (Service/Visitor)
- Ajout fréquent de **types** mais peu d’opés → éviter Visitor, rester Service/POJO

En suivant le pattern Visitor on applique la responsabilité unique et le principe ouvert/fermé, car on extrait chaque algorithme dans une classe séparée et pouvez introduire de nouveaux visiteurs sans modifier les existants.


## 10. Points clés
- **Visitor** excelle quand la **famille de types** est stable et que **les opérations** changent souvent.
- Le **double dispatch** est le cœur technique : `accept` → `visitXxx`.
- **Service métier** pour l’**infrastructure** et les orchestrations multi-agrégats.
- **POJO/Value Object** pour les règles **pures et locales** au modèle.
