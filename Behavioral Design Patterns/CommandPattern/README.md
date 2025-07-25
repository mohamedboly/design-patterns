## Définition du Command Pattern

Le Command Pattern est un **pattern comportemental** (behavioral) qui transforme une **requête ou un comportement** en un **objet autonome**. Cet objet encapsule **toutes les informations nécessaires pour effectuer une action ou déclencher un événement**, ce qui permet de :

* déclencher des actions plus tard (ex. scheduler)
* annuler ou rejouer des actions (undo/redo)
* stocker des actions (fichier, base de données, historique)
* découpler complètement l’émetteur de la commande de sa logique
* enchaîner dynamiquement des actions

Le Command Pattern encapsule **une méthode (l'action à exécuter), l'objet sur lequel cette action doit être effectuée (par exemple : une lampe), ainsi que les éventuels paramètres que cette méthode peut nécessiter** dans un **objet autonome** appelé "commande".

Par exemple, dans le cas d'une maison connectée :

* l’action à exécuter est "allumer",
* l’objet concerné est une instance de `Light`,
* et cette méthode (`turnOn()`) pourrait potentiellement accepter des paramètres (comme une intensité lumineuse, une durée, etc.).

---

## Étapes pour implémenter le Command Pattern

1. **Créer une interface `Command`** avec une méthode unique `execute()`.
2. **Créer une classe concrète** qui implémente `Command` et contient les données nécessaires à l’exécution de l’action (appelée ConcreteCommand).
3. **Créer un Receiver** : l'objet métier qui contient la logique réelle (ex. `Light`, `UserService`).
4. **Créer un Invoker** : la classe qui exécute la commande (par exemple une télécommande, un contrôleur REST).
5. **Créer un Client** qui configure la commande (en injectant le receiver et les paramètres nécessaires).

---

## Problème que ce pattern résout

On a une opération à exécuter (ex : allumer une lumière, envoyer un email...). Mais parfois :

* ✅ On veux la déclencher plus tard
* ✅ On veux l'annuler ou la rejouer
* ✅ On veux la stocker
* ✅ On veux que celui qui demande l'action ne connaisse pas comment elle est faite
* ✅ On veux enchaîner plusieurs actions dynamiquement

Le Command Pattern encapsule la **méthode + son contexte (objet cible + paramètres)** dans un **objet autonome**.

## Sans Command Pattern

```java
public class RemoteControl {
    private Light light;

    public RemoteControl(Light light) {
        this.light = light;
    }

    public void pressButton() {
        light.turnOn(); // étroitement lié à la classe Light
    }
}
```

→ Très couplé, difficile à changer, tester, différer, annuler, etc.

## Avec Command Pattern

```java
// 1. Commande générique
public interface Command {
    void execute();
}

// 2. Implémentation concrète
public class TurnOnLightCommand implements Command {
    private Light light;

    public TurnOnLightCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

// 3. Invoker
public class RemoteControl {
    private Command command;

    public void setCommand(Command cmd) {
        this.command = cmd;
    }

    public void pressButton() {
        command.execute(); // Remote ne connaît ni Light ni la logique
    }
}
```

### Avantages obtenus

* Remplacer la commande : `TurnOffLightCommand`, `PlayMusicCommand`, etc.
* Enregistrer des commandes dans une liste pour les exécuter plus tard
* Ajouter `undo()` facilement
* Découpler totalement la logique métier de l’invocation

## En résumé

| Sans Command            | Avec Command            |
| ----------------------- | ----------------------- |
| Code couplé             | Code découplé           |
| Pas de stockage différé | Facile à enregistrer    |
| Pas d'annulation        | Undo/Redo possible      |
| If/Else partout         | Ajout = nouvelle classe |

---

## Exemple complet : Maison Connectée

### 1. Sans Command Pattern

```java
public class Light {
    private boolean isOn;

    public void turnOn() {
        isOn = true;
        System.out.println("Lumière allumée");
    }

    public void turnOff() {
        isOn = false;
        System.out.println("Lumière éteinte");
    }

    public boolean isOn() {
        return isOn;
    }
}

public class Room {
    private Light light = new Light();

    public void toggleLight() {
        if (light.isOn()) light.turnOff();
        else light.turnOn();
    }
}
```

→ Impossible de planifier, d’annuler, ou de journaliser une action. Code rigide.

### 2. Avec Command Pattern (Solution améliorée)

```java
// Command
public interface Command {
    void execute();
}

// Receiver
public class Light {
    private boolean isOn;

    public void turnOn() {
        isOn = true;
        System.out.println("Lumière allumée");
    }

    public void turnOff() {
        isOn = false;
        System.out.println("Lumière éteinte");
    }

    public boolean isOn() {
        return isOn;
    }
}

// Concrete Commands
public class TurnOnCommand implements Command {
    private Light light;

    public TurnOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

public class TurnOffCommand implements Command {
    private Light light;

    public TurnOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }
}

// Invoker
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}

// Client
public class Main {
    public static void main(String[] args) {
        Light salon = new Light();
        RemoteControl remote = new RemoteControl();

        Command turnOn = new TurnOnCommand(salon);
        Command turnOff = new TurnOffCommand(salon);

        remote.setCommand(turnOn);
        remote.pressButton(); // Lumière allumée

        remote.setCommand(turnOff);
        remote.pressButton(); // Lumière éteinte
    }
}
```

→ Grâce au Command Pattern, on peux maintenant stocker, annuler ou même exécuter en différé les commandes sur nos objets.

---

## Exemple Spring Boot : Création d’un utilisateur

### Sans Command Pattern (classique)

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserDTO dto) {
        userService.createUser(dto.getName(), dto.getEmail());
        return ResponseEntity.ok("Utilisateur créé");
    }
}

@Service
public class UserService {
    public void createUser(String name, String email) {
        User user = new User(name, email);
        userRepository.save(user);
    }
}
```

### Avec Command Pattern

#### 1. Interface Command

```java
public interface Command {
    void execute();
}
```

#### 2. Commande concrète

```java
public class CreateUserCommand implements Command {
    private final UserService userService;
    private final String name;
    private final String email;

    public CreateUserCommand(UserService userService, String name, String email) {
        this.userService = userService;
        this.name = name;
        this.email = email;
    }

    @Override
    public void execute() {
        userService.createUser(name, email);
    }
}
```

#### 3. Invoker

```java
@Component
public class CommandExecutor {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void run() {
        command.execute();
    }
}
```

#### 4. Controller

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private CommandExecutor executor;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserDTO dto) {
        Command cmd = new CreateUserCommand(userService, dto.getName(), dto.getEmail());
        executor.setCommand(cmd);
        executor.run();
        return ResponseEntity.ok("Utilisateur créé");
    }
}
```

On peux maintenant logguer les commandes, les stocker, différer leur exécution ou même ajouter `undo()` !
