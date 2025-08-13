# Pattern Adapter en Java – Guide Complet

## 1. Introduction

Le **pattern Adapter** est un **patron de conception structurel** qui permet à deux objets ayant des interfaces incompatibles de collaborer.

Le pattern Adapter est similaire à l’adaptateur entre une prise américaine et une prise européenne : il permet de connecter deux systèmes incompatibles en créant une couche intermédiaire qui traduit les appels.

Dans notre exemple, nous avons un service qui rend des menus en **XML**, et nous souhaitons utiliser une bibliothèque tierce (plus performante) qui ne comprend que le **JSON**.


## 2. Pertinence du pattern Adapter

Sans le pattern Adapter, on pourrait être tenté de :

* Modifier notre ancien code pour gérer le JSON → risque de casser du code existant.
* Modifier la librairie tierce pour comprendre le XML → souvent impossible (code fermé) ou risqué.

**Le signal que vous avez besoin d’un Adapter** :

* Vous devez utiliser une nouvelle API / bibliothèque, mais son interface ne correspond pas à celle de votre code existant.
* Vous devez éviter de réécrire ou de casser du code stable.
* Vous voulez isoler la logique de transformation dans une seule classe réutilisable.

**Pour éviter l’over-engineering** :

* Utilisez un Adapter uniquement quand il y a incompatibilité d’interfaces.
* Si vous pouvez adapter en modifiant légèrement une seule méthode sans casser de contrats, ne créez pas un Adapter.


## 3. Étapes d’implémentation

1. **Identifier le client** (code existant) et son interface.
2. **Identifier le service tiers** (API externe ou legacy) que vous souhaitez utiliser.
3. **Créer un Adapter** qui :

    * Implémente l’interface du client.
    * Contient une référence vers le service tiers.
    * Traduit les appels du client vers le service tiers.
4. **Utiliser l’Adapter** là où vous utilisiez l’ancienne implémentation.


## 4. Exemple complet

### Interface de rendu (client)

```java
public interface MenuRenderer {
    void render(String xmlMenu);
}
```

### Ancienne implémentation (XML)

```java
public class XmlMenuRenderer implements MenuRenderer {
    @Override
    public void render(String xmlMenu) {
        System.out.println("Rendering XML menu: " + xmlMenu);
    }
}
```

### Service tiers (JSON uniquement)

```java
public class FancyUIService {
    public void renderJson(String jsonMenu) {
        System.out.println("Rendering JSON menu with fancy UI: " + jsonMenu);
    }
}
```

### Wrapper autour du service tiers

```java
public class JsonMenuRenderer {
    private final FancyUIService fancy;

    public JsonMenuRenderer(FancyUIService fancy) {
        this.fancy = fancy;
    }

    public void render(String jsonMenu) {
        fancy.renderJson(jsonMenu);
    }
}
```

**Pourquoi ce wrapper ?**

* Il **encapsule** le service tiers (`FancyUIService`) pour contrôler son usage.
* Il permet d’ajouter une couche d’abstraction (facile à tester, à remplacer).
* Il clarifie l’intention : ici, ce service rend des menus JSON.
* Il applique le principe de **Single Responsibility** : `JsonMenuRenderer` ne fait qu’une chose, appeler `FancyUIService` avec du JSON.

### Adapter : XML → JSON

```java
import org.json.JSONObject;
import org.json.XML;

public class FancyUIServiceAdapter implements MenuRenderer {
    private final JsonMenuRenderer jsonRenderer;

    public FancyUIServiceAdapter(JsonMenuRenderer jsonRenderer) {
        this.jsonRenderer = jsonRenderer;
    }

    @Override
    public void render(String xmlMenu) {
        JSONObject json = XML.toJSONObject(xmlMenu);
        jsonRenderer.render(json.toString());
    }
}
```

### Code client

```java
public class Main {
    public static void main(String[] args) {
        String xmlData = "<menu><item>Pizza</item></menu>";

        // Ancienne version
        MenuRenderer xmlRenderer = new XmlMenuRenderer();
        xmlRenderer.render(xmlData);

        // Nouvelle version avec Adapter
        FancyUIService fancyUIService = new FancyUIService();
        JsonMenuRenderer jsonRenderer = new JsonMenuRenderer(fancyUIService);
        MenuRenderer adapter = new FancyUIServiceAdapter(jsonRenderer);
        adapter.render(xmlData);
    }
}
```


## 5. Avantages

* Réutilisation du code existant sans le modifier.
* Respect du principe **Open/Closed** (ouvert à l’extension, fermé à la modification).
* Isolation des dépendances externes.

## 6. Inconvénients

* Ajoute une couche supplémentaire (peut complexifier si mal utilisé).
* Peut masquer un besoin de refactoriser l’interface d’origine.


## 7. Diagramme UML

```
+----------------+        +-----------------------+
| MenuRenderer   |        | FancyUIService        |
| <<interface>>  |        |-----------------------|
| +render(xml)   |        | +renderJson(json)     |
+----------------+        +-----------------------+
        ^                           ^
        |                           |
+----------------+        +-----------------------+
| XmlMenuRenderer|        | JsonMenuRenderer      |
|----------------|        |-----------------------|
| +render(xml)   |        | +render(json)         |
+----------------+        +-----------------------+
        ^
        |
+--------------------------+
| FancyUIServiceAdapter    |
|--------------------------|
| +render(xml)             |
+--------------------------+
```


## 8. Conclusion

Le pattern Adapter agit comme un **traducteur** entre deux mondes. Dans notre cas :

* Le **client** parle XML.
* Le **service tiers** parle JSON.
* L’**adapter** traduit XML → JSON.

En appliquant ce pattern, on **réduit les risques**, on **isole les dépendances** et on rend le système plus **flexible**.


Conseil :
Utilise sans wrapper si la librairie est stable, maîtrisée, et peu probable à changer.
Utilise avec wrapper si la librairie est tierce, instable, lourde, ou si tu veux garder ton code testable et découplé.