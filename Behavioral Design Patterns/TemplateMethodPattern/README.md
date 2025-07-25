# Template Method Pattern - README

## Définition

Le **Template Method Pattern** est un *design pattern comportemental* qui permet de **définir le squelette d’un algorithme dans une classe abstraite**, tout en laissant les sous-classes **redéfinir certaines étapes** de cet algorithme **sans changer sa structure globale**.

> “Lorsqu’une opération globale comporte plusieurs étapes fixes, mais que certaines étapes peuvent varier d’une implémentation à une autre, ce pattern est idéal.”

---

## Pourquoi ce pattern ?

Dans les jeux vidéos, notamment les jeux AAA, l'écran de chargement effectue une série d'opérations :

* Charger les données locales (images, code, sons...)
* Créer des objets internes (personnages, éléments de jeu)
* Télécharger des ressources en ligne (traductions, sons...)
* Supprimer des fichiers temporaires
* Charger le profil du joueur

> Ces étapes sont communes à plusieurs jeux, mais l’implémentation de certaines étapes peut varier d’un jeu à l’autre.

On veut donc **factoriser le squelette de l’algorithme commun** tout en laissant les étapes personnalisables.

---

## Explication

Imaginez que vous êtes développeur de jeux vidéo et que vous travaillez sur l'écran de chargement d’un jeu AAA.

Vous constatez que toutes les opérations de chargement peuvent être divisées en plusieurs étapes :

1. Charger les données locales
2. Créer des objets du jeu
3. Télécharger des fichiers additionnels
4. Nettoyer les fichiers temporaires
5. Initialiser le profil du joueur

### Scénario concret :

* Pour World of Warcraft et Diablo, les étapes sont les mêmes, mais certaines diffèrent dans l’implémentation.
* On ne veut pas dupliquer le même algorithme de chargement.

La solution ? Créer une **classe abstraite BaseGameLoader** avec une méthode `load()` contenant l’ordre d’exécution.

Chaque sous-classe (ex : `DiabloLoader`, `WorldOfWarcraftLoader`) redéfinit les étapes qui la concernent.

---

## Mise en œuvre - Etapes

1. Créer une **classe abstraite** avec une méthode `template` (ici : `load()`)
2. Diviser le process global en **étapes/méthodes distinctes**
3. Certaines méthodes peuvent avoir une **implémentation par défaut**
4. Les **sous-classes redéfinissent** les étapes nécessaires

---

## Exemple de code

### 1. Classe abstraite : BaseGameLoader

```java
public abstract class BaseGameLoader {
    public final void load() {
        loadLocalData();
        createObjects();
        downloadAdditionalFiles();
        cleanTempFiles();
        initializeProfiles();
    }

    protected abstract void loadLocalData();
    protected abstract void createObjects();
    protected void downloadAdditionalFiles() {} // Hook (facultatif)
    protected void cleanTempFiles() {
        System.out.println("Cleaning temp files...");
    }
    protected abstract void initializeProfiles();
}
```

### 2. Implémentation concrète : DiabloLoader

```java
public class DiabloLoader extends BaseGameLoader {
    protected void loadLocalData() {
        System.out.println("Chargement des données Diablo...");
    }

    protected void createObjects() {
        System.out.println("Création des objets Diablo...");
    }

    protected void initializeProfiles() {
        System.out.println("Initialisation profil Diablo...");
    }
}
```

### 3. Utilisation

```java
public class Main {
    public static void main(String[] args) {
        BaseGameLoader loader = new DiabloLoader();
        loader.load();
    }
}
```

---

## 🚀 Quelques remarques 

### "Et si un jeu n’a pas besoin de toutes les étapes ?"

On peut :

* Fournir une **implémentation vide** par défaut pour les étapes optionnelles (hook method)
* Créer des **méthodes protégées** que la sous-classe n’est pas obligée de redéfinir

### "Et si l’ordre d’exécution doit changer ?"

→ Ce pattern **impose un ordre fixe**. Si l’ordre varie, le **Strategy Pattern** serait plus adapté.

### "L’algo doit être divisé en étapes claires ?"

Oui, c’est **obligatoire**. Chaque étape doit :

* Être autonome
* Participer à une tâche globale
* Être complémentaire avec les autres

---

## Avantages

* ✅ Permet de factoriser du code répété (comme `cleanTempFiles()`)
* ✅ Maintient un **ordre d’exécution fixe**
* ✅ Favorise l’utilisation des **hooks** (méthodes optionnelles)
* ✅ Évite la duplication de code entre sous-classes

## Inconvénients

* Ordre **figé** dans la classe parent
* Peut être **rigide** si les besoins varient trop

---

## En résumé

| Ce que fait ce pattern                                 |
| ------------------------------------------------------ |
| Définit le squelette d’un algo dans une classe parente |
| Laisse les sous-classes redéfinir les étapes           |
| Garde l’ordre et la structure fixe                     |
| Réduit la duplication                                  |

> Utilise-le quand :
>
> * Un process a toujours la même structure
> * Mais certaines étapes changent selon le contexte

