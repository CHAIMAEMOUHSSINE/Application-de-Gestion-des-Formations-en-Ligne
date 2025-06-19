# Guide d'Installation - Application Gestion des Formations

## Prérequis

1. **XAMPP** installé et démarré
2. **MySQL** en cours d'exécution dans XAMPP
3. **Java JDK 21** ou plus récent
4. **Maven** (optionnel, le wrapper mvnw est inclus)

## Étapes d'installation

### 1. Configuration de la base de données

#### Option A: Via phpMyAdmin (Recommandé)
1. Ouvrez phpMyAdmin dans votre navigateur: `http://localhost/phpmyadmin`
2. Cliquez sur "Importer" dans le menu principal
3. Sélectionnez le fichier `setup_database.sql` 
4. Cliquez sur "Exécuter"

#### Option B: Via ligne de commande MySQL
```bash
mysql -u root -p < setup_database.sql
```

### 2. Vérification de la base de données
Après l'exécution du script, vous devriez voir:
- Base de données `DB_ENTREPRISE` créée
- 3 tables: `employes`, `formations`, `employe_formations`
- Données de test insérées (8 employés, 8 formations, 16 inscriptions)

### 3. Test de l'application

#### Compilation et test
```bash
# Si Maven est installé
mvn compile exec:java -Dexec.mainClass="ma.enset.exam2test.Services.ServiceTest"

# Ou avec le wrapper Maven (si JAVA_HOME est configuré)
./mvnw compile exec:java -Dexec.mainClass="ma.enset.exam2test.Services.ServiceTest"
```

#### Test de connexion simple
Vous pouvez aussi exécuter directement `ServiceTest.java` depuis votre IDE.

### 4. Lancement de l'application JavaFX
```bash
mvn javafx:run
# ou
./mvnw javafx:run
```

## Structure du projet

```
src/main/java/ma/enset/exam2test/
├── entities/           # Classes métier (employe, formation, EmployeFormation)
├── DAO/               # Accès aux données (interfaces et implémentations)
├── Services/          # Logique métier (interfaces et implémentations)
├── Controllers/       # Contrôleurs JavaFX
└── MainApplication.java # Classe principale

src/main/resources/ma/enset/exam2test/
├── main-view.fxml     # Interface principale
├── employe.fxml       # Interface gestion employés
└── formation.fxml     # Interface gestion formations
```

## Fonctionnalités implémentées

### ✅ Couche DAO (Data Access Object)
- `employeDAO` et `employeDAOImp` - CRUD complet pour les employés
- `formationDAO` et `formationDAOImp` - CRUD complet pour les formations + gestion inscriptions
- `DBConnection` - Connexion MySQL avec pool de connexions

### ✅ Couche Service (Logique métier)
- `IemployeService` et `employeServiceImp` - Validation, export CSV asynchrone
- `IformationService` et `formationServiceImp` - Gestion formations et inscriptions
- Export CSV avec threads et progress bar

### ✅ Entités
- `employe` - Employé avec validation email
- `formation` - Formation avec durée
- `EmployeFormation` - Relation employé-formation avec statut

### 🚧 En cours de développement
- Interface JavaFX complète
- Contrôleurs pour la gestion des employés et formations
- Fonctionnalités d'export avec progress bar

## Dépannage

### Erreur de connexion MySQL
- Vérifiez que XAMPP est démarré
- Vérifiez que MySQL fonctionne (port 3306)
- Vérifiez les paramètres de connexion dans `DBConnection.java`

### Erreur JAVA_HOME
- Configurez la variable d'environnement JAVA_HOME
- Ou utilisez directement `java` et `javac` si disponibles dans PATH

### Tables n'existent pas
- Exécutez le script `setup_database.sql` dans phpMyAdmin
- Ou utilisez la classe `DatabaseSetup.java` une fois compilée

## Tests

Le fichier `ServiceTest.java` permet de tester:
- Connexion à la base de données
- Fonctionnement des DAO
- Présence des tables et données

Résultat attendu:
```
=== Test de connexion à la base de données ===
✅ Connexion à la base de données réussie !
URL: jdbc:mysql://localhost:3306/DB_ENTREPRISE
Utilisateur: root@localhost
Driver: MySQL Connector/J

=== Test EmployeDAO ===
✅ Nombre d'employés trouvés: 8
- Ahmed Alami (ahmed.alami@entreprise.ma) - Développeur
- Fatima Benali (fatima.benali@entreprise.ma) - Chef de projet
...

=== Test FormationDAO ===
✅ Nombre de formations trouvées: 8
- Java Avancé (40h)
- Gestion de projet (24h)
...
```
