Pattern Memento – Explication et Implémentation

Le Memento Pattern est un patron de conception comportemental qui permet de sauvegarder et restaurer l’état d’un objet sans exposer ses détails internes.
Il est particulièrement utile pour implémenter des fonctionnalités d’annulation (Undo/Redo) ou pour revenir à un état précédent dans un processus, tout en respectant l’encapsulation.

Problématique

Lorsqu’on souhaite sauvegarder l’état d’un objet, on pourrait être tenté de copier ses champs à partir de l’extérieur. Cela pose deux problèmes :

* Briser l’encapsulation : exposition de champs privés ou ajout de getters inutiles.
* Couplage fort : toute modification interne de l’objet peut casser le code qui le manipule.

Objectif

Fournir un mécanisme permettant de :

* Sauvegarder l’état complet d’un objet, y compris ses champs privés.
* Restaurer cet état plus tard.
* Garantir qu’aucune autre classe n’accède directement aux détails internes.

Cas d’utilisation

* Fonctionnalité Annuler/Rétablir dans un éditeur de texte, d’image ou vidéo
* Sauvegarde temporaire avant une opération risquée
* Points de restauration dans un logiciel ou un jeu vidéo

Exemple sans Memento

Dans cet exemple, les champs sont publics et l’historique est géré en dehors de l’objet, violant l’encapsulation.

```java
class TextArea {
    public String text;

    public void setText(String text) {
        this.text = text;
    }
}

class Editor {
    private List<String> history = new ArrayList<>();
    private TextArea textArea;

    public Editor(TextArea textArea) {
        this.textArea = textArea;
    }

    public void write(String text) {
        history.add(textArea.text);
        textArea.setText(text);
    }

    public void undo() {
        if (!history.isEmpty()) {
            String lastState = history.remove(history.size() - 1);
            textArea.setText(lastState);
        }
    }
}
```

Limites de cette approche :

* Champs exposés publiquement
* Modification interne de TextArea casse Editor
* Perte de contrôle sur les données

Question fréquente : peut-on utiliser des getters à la place ?
Il est possible d’utiliser des getters, mais cela déplace le problème sans le résoudre.

* Les getters exposent tout de même l’état interne
* Le code externe doit connaître les détails de la structure interne
* Le couplage reste fort et l’encapsulation n’est pas garantie

Exemple avec getters :

```java
class TextArea {
    private String text;
    private String font;
    
    public String getText() { return text; }
    public String getFont() { return font; }
}

class Editor {
    private List<String[]> history = new ArrayList<>();
    private TextArea textArea;

    public void save() {
        history.add(new String[]{textArea.getText(), textArea.getFont()});
    }
}
```

Le Memento Pattern corrige ce problème en déléguant la sauvegarde et la restauration à l’objet lui-même.

Exemple avec Memento

Rôles :

* Originator : l’objet dont on sauvegarde l’état
* Memento : l’objet représentant un snapshot de l’état, généralement immuable
* Caretaker : le gestionnaire d’historique, qui décide quand sauvegarder ou restaurer

Étapes d’implémentation :

1. Identifier l’Originator : l’objet dont l’état doit être sauvegardé/restauré
2. Créer le Memento : classe interne ou dédiée, immuable, contenant uniquement l’état nécessaire
3. Ajouter une méthode takeSnapshot() dans l’Originator, qui retourne un Memento
4. Ajouter une méthode restore(Memento m) dans l’Originator, qui restaure l’état
5. Créer un Caretaker (pile ou liste de Mementos) qui gère l’historique
6. Avant toute modification annulable, appeler takeSnapshot() et stocker dans l’historique
7. Pour annuler, récupérer le dernier Memento et appeler restore()
8. Facultatif : implémenter Redo avec deux piles (undoStack, redoStack)
9. Facultatif : limiter l’historique pour maîtriser la mémoire

Code complet :

```java
class TextArea {
    private String text;
    private String font;

    public class Memento {
        private final String text;
        private final String font;
        private Memento(String text, String font) {
            this.text = text;
            this.font = font;
        }
    }

    public void setText(String text) { this.text = text; }
    public String getText() { return text; }

    public Memento takeSnapshot() {
        return new Memento(text, font);
    }

    public void restore(Memento memento) {
        this.text = memento.text;
        this.font = memento.font;
    }
}

class Editor {
    private final Stack<TextArea.Memento> history = new Stack<>();
    private final TextArea textArea;

    public Editor(TextArea textArea) {
        this.textArea = textArea;
    }

    public void write(String text) {
        history.push(textArea.takeSnapshot());
        textArea.setText(text);
    }

    public void undo() {
        if (!history.isEmpty()) {
            textArea.restore(history.pop());
        }
    }
}
```

Utilisation :

```java
public class Main {
    public static void main(String[] args) {
        TextArea textArea = new TextArea();
        Editor editor = new Editor(textArea);

        editor.write("Bonjour");
        editor.write("Bonjour le monde");
        editor.write("Bonjour le monde entier");

        System.out.println(textArea.getText()); // Bonjour le monde entier

        editor.undo();
        System.out.println(textArea.getText()); // Bonjour le monde

        editor.undo();
        System.out.println(textArea.getText()); // Bonjour
    }
}
```

Structure UML :

```
+----------------+         +----------------+         +----------------+
|   Originator   | <-----> |    Memento     |         |   Caretaker    |
| - state        |         | - state        |         | - history      |
| + saveState()  |         | (immutable)    |         | + undo()/redo()|
| + restore()    |         |                |         |                |
+----------------+         +----------------+         +----------------+
```

Résumé :

* Sauvegarde et restauration d’état sans briser l’encapsulation
* L’objet est responsable de créer et restaurer son Memento
* Historique propre et sécurisé
* Attention à la consommation mémoire si les états sont volumineux
