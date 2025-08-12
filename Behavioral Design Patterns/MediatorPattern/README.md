# Mediator Pattern – Explication et Guide d’Implémentation

## Idée clé

Quand un événement se produit dans un composant (clic, saisie, sélection, etc.) et que d’autres composants doivent réagir,
au lieu de mettre la logique directement dans le code du composant qui émet l’événement,
on déplace cette logique dans un objet central appelé **Médiateur**.

---

## Définition

Le **Mediator Pattern** est un patron de conception comportemental qui définit un objet central (le Médiateur) chargé de gérer la communication entre plusieurs composants.
Ainsi, les composants n’interagissent pas directement entre eux mais passent par le Médiateur.

---

## Notion de "composant"

Dans ce contexte, un composant désigne généralement une **instance d’une classe** participant à une interaction.

Exemple : une instance de `Button` ou `TextBox` dans une fenêtre.

Les composants sont des objets "vivants" dans le programme, qui émettent ou reçoivent des événements.

---

## Pourquoi utiliser un Médiateur

### Découplage

Les composants ne se connaissent pas directement, ce qui réduit les dépendances.

### Flexibilité

On peut modifier les règles d’interaction sans toucher aux composants eux-mêmes.

### Réutilisabilité

Les composants peuvent être utilisés dans d’autres contextes avec un autre Médiateur.

---

## Cas d’utilisation

Le Médiateur est utile dès que plusieurs objets doivent réagir les uns aux autres sans dépendre directement.

* **Interfaces utilisateur**
  Clic sur un bouton → met à jour d’autres composants (listes, champs, etc.).

* **Modules métier**
  Un événement métier (ex : virement bancaire) entraîne plusieurs actions coordonnées.

* **Systèmes de chat ou messagerie**
  Les clients passent par un serveur central pour s’échanger des messages.

* **Jeux vidéo**
  Actions d’un joueur entraînant des réactions multiples dans le jeu.

* **Contrôle de trafic**
  Tour de contrôle coordonnant les avions.

* **IoT (Internet des objets)**
  Un capteur déclenche plusieurs actions dans différents systèmes.

---

## Étapes pour implémenter le Mediator Pattern

1. **Définir une interface Mediator**
   Contient une méthode `notify(sender, event)` ou similaire.

2. **Créer des composants**
   Chaque composant garde une référence vers le Médiateur.
   Les composants notifient le Médiateur lorsqu’un événement survient.

3. **Créer une classe Mediator concrète**
   Connaît tous les composants à coordonner.
   Contient la logique des réactions aux événements.

4. **Assembler dans le code client**
   Instancier les composants et le Médiateur.
   Injecter le Médiateur dans les composants.
   Déclencher les événements.

---

## Exemple sans Mediator

```java
class ListBox {
    public void update() {
        System.out.println("ListBox mise à jour");
    }
}

class TextBox {
    public void clear() {
        System.out.println("TextBox vidé");
    }
}

class Button {
    private ListBox listBox;
    private TextBox textBox;

    public Button(ListBox listBox, TextBox textBox) {
        this.listBox = listBox;
        this.textBox = textBox;
    }

    public void click() {
        System.out.println("Bouton cliqué !");
        listBox.update();
        textBox.clear();
    }
}

public class MainSansMediator {
    public static void main(String[] args) {
        ListBox listBox = new ListBox();
        TextBox textBox = new TextBox();
        Button button = new Button(listBox, textBox);

        button.click();
    }
}
```

**Problème :**
Le `Button` dépend directement de `ListBox` et `TextBox`.
Changer la logique ou remplacer un composant nécessite de modifier `Button`.

---

## Exemple avec Mediator

### Interface et composants

```java
interface Mediator {
    void notify(Component sender, String event);
}

abstract class Component {
    protected Mediator mediator;
    public Component(Mediator mediator) {
        this.mediator = mediator;
    }
}

class Button extends Component {
    public Button(Mediator mediator) {
        super(mediator);
    }

    public void click() {
        System.out.println("Bouton cliqué !");
        mediator.notify(this, "click");
    }
}

class TextBox extends Component {
    public TextBox(Mediator mediator) {
        super(mediator);
    }

    public void clear() {
        System.out.println("TextBox vidé");
    }
}

class ListBox extends Component {
    public ListBox(Mediator mediator) {
        super(mediator);
    }

    public void update() {
        System.out.println("ListBox mise à jour");
    }
}
```

### Médiateur concret

```java
class DialogMediator implements Mediator {
    private Button button;
    private TextBox textBox;
    private ListBox listBox;

    public DialogMediator(Button button, TextBox textBox, ListBox listBox) {
        this.button = button;
        this.textBox = textBox;
        this.listBox = listBox;
    }

    @Override
    public void notify(Component sender, String event) {
        if (sender == button && "click".equals(event)) {
            listBox.update();
            textBox.clear();
        }
    }
}
```

### Utilisation dans `main`

```java
public class MainAvecMediator {
    public static void main(String[] args) {
        // Création provisoire des composants avec un mediator null
        Button button = new Button(null);
        TextBox textBox = new TextBox(null);
        ListBox listBox = new ListBox(null);

        // Création du mediator avec les composants
        DialogMediator mediator = new DialogMediator(button, textBox, listBox);

        // Injection du mediator dans les composants
        button.mediator = mediator;
        textBox.mediator = mediator;
        listBox.mediator = mediator;

        // Simulation : clic sur le bouton
        button.click();
    }
}
```

**Sortie console :**

```
Bouton cliqué !
ListBox mise à jour
TextBox vidé
```

---

## Comparaison

| Sans Mediator                                                     | Avec Mediator                         |
| ----------------------------------------------------------------- | ------------------------------------- |
| Couplage fort entre composants                                    | Couplage faible grâce au médiateur    |
| Modifications dans un composant peuvent impacter plusieurs autres | Les composants restent indépendants   |
| Logique de coordination dispersée                                 | Logique centralisée dans le médiateur |

---

## Limites

* Un Médiateur peut devenir trop complexe s’il gère trop de règles.
* Risque de créer un "God Object" si toute la logique est centralisée sans organisation.
