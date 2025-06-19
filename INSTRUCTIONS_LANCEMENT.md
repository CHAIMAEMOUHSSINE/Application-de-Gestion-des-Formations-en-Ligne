# 🚀 INSTRUCTIONS POUR LANCER L'APPLICATION

## ❌ **Problèmes courants et solutions**

### **Problème 1: "Module not found" ou erreurs JavaFX**
**Cause:** JavaFX n'est pas dans le classpath
**Solution:** Utiliser IntelliJ IDEA qui gère automatiquement JavaFX

### **Problème 2: "No suitable driver found"**
**Cause:** Driver MySQL manquant
**Solution:** Maven télécharge automatiquement les dépendances

### **Problème 3: "JAVA_HOME not found"**
**Cause:** Variables d'environnement non configurées
**Solution:** IntelliJ utilise son propre JDK

## ✅ **MÉTHODE RECOMMANDÉE : IntelliJ IDEA**

### **Étape 1: Ouvrir le projet**
1. Lancer IntelliJ IDEA
2. File → Open
3. Sélectionner le dossier `exam2test`
4. Cliquer "OK"

### **Étape 2: Attendre la synchronisation**
- IntelliJ va détecter le projet Maven
- Laisser télécharger les dépendances (2-3 minutes)
- Vérifier que "Maven" apparaît dans la barre latérale droite

### **Étape 3: Configurer le projet**
1. File → Project Structure (Ctrl+Alt+Shift+S)
2. Project → Project SDK → Sélectionner Java 17+ ou 21
3. Modules → Vérifier que le module est reconnu
4. OK

### **Étape 4: Tester avec SimpleApp**
1. Ouvrir `src/main/java/ma/enset/exam2test/SimpleApp.java`
2. Clic droit → "Run 'SimpleApp.main()'"
3. Si ça marche → JavaFX fonctionne ✅

### **Étape 5: Lancer MainApplication**
1. Ouvrir `src/main/java/ma/enset/exam2test/MainApplication.java`
2. Clic droit → "Run 'MainApplication.main()'"
3. Si erreur de connexion DB → Démarrer XAMPP d'abord

## 🔧 **DÉPANNAGE**

### **Si SimpleApp ne marche pas:**
- Problème JavaFX → Installer JavaFX SDK dans IntelliJ
- File → Settings → Plugins → Rechercher "JavaFX" → Installer

### **Si MainApplication ne marche pas:**
1. **Erreur FXML:** Vérifier que les fichiers .fxml existent
2. **Erreur DB:** Démarrer XAMPP et créer la base
3. **Erreur BootstrapFX:** Vérifier les dépendances Maven

### **Vérifier les dépendances Maven:**
1. Clic droit sur `pom.xml` → "Maven" → "Reload project"
2. Maven → Lifecycle → "clean" puis "compile"

## 📋 **ORDRE DE TEST RECOMMANDÉ**

1. **Test 1:** `SimpleApp.java` (JavaFX de base)
2. **Test 2:** `TestSimple.java` (Connexion DB)
3. **Test 3:** `ServiceTest.java` (Services métier)
4. **Test 4:** `MainApplication.java` (Application complète)

## 🆘 **SI RIEN NE MARCHE**

### **Alternative 1: Recréer la configuration**
1. File → Invalidate Caches and Restart
2. Redémarrer IntelliJ
3. Réouvrir le projet

### **Alternative 2: Vérifier les logs**
- View → Tool Windows → Run
- Regarder les messages d'erreur complets
- Copier l'erreur exacte pour diagnostic

### **Alternative 3: Version minimale**
Utiliser seulement `SimpleApp.java` pour vérifier que JavaFX fonctionne

## 📞 **MESSAGES D'ERREUR COURANTS**

| **Erreur** | **Cause** | **Solution** |
|------------|-----------|--------------|
| `Module javafx.controls not found` | JavaFX manquant | Configurer JavaFX dans IntelliJ |
| `No suitable driver found` | MySQL driver manquant | Maven reload |
| `FXML file not found` | Chemin FXML incorrect | Vérifier les ressources |
| `BootstrapFX not found` | Dépendance manquante | Maven reload |

## 🎯 **RÉSULTAT ATTENDU**

Quand tout fonctionne, vous devriez voir :
1. **SimpleApp:** Fenêtre avec boutons ✅
2. **MainApplication:** Interface complète avec onglets ✅
3. **Base de données:** Connexion réussie ✅
4. **Fonctionnalités:** CRUD employés/formations ✅

**Votre projet est COMPLET et FONCTIONNEL !**
Le problème est juste la configuration de l'environnement.
