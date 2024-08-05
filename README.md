Description de l'Application
c'est une application desktop de gestion de contact développé en Java et utilisant JavaFX pour l'interface graphique. 
Elle permet aux utilisateurs de gérer facilement leurs contacts personnels et professionnels. 
L'application est organisée en plusieurs composants, chacun ayant une responsabilité spécifique, ce qui facilite la maintenance et l'extension des fonctionnalités.

Fonctionnalités de l'Application
1. Gestion des Contacts Personnels et Professionnels:
   - Contacts Personnels : Inclut les informations comme le nom, le numéro de téléphone, l'email, la date d'anniversaire, et l'adresse.
   - Contacts Professionnels : Inclut le nom de l'entreprise.
2.Ajout, Modification et Suppression de Contacts:
   - Ajout : L'utilisateur peuvent ajouter de nouveaux contacts en remplissant un formulaire.
   - Modification : L' utilisateur peut modifier les informations des contacts existants.
   - Suppression : L'utilisateur peut supprimer des contacts de la liste.
3. Recherche :
   - Recherche par Nom : Permet de rechercher des contacts par leur nom.
   - Recherche par ID : Permet de rechercher des contacts par leur ID.
4. Connexion à une Base de Données :
   - Utilisation d'une base de données pour stocker les informations des contacts, assurant la persistance des données entre les sessions.
   - DatabaseConnection : Une classe dédiée pour gérer les connexions à la base de données.

5. Gestion des Exceptions :
   - ContactException : Une classe pour gérer les erreurs spécifiques à la gestion des contacts.

6. Interface Graphique Intuitive :
   - Utilisation de JavaFX pour offrir une interface utilisateur intuitive et réactive.
   - Le fichier FXML (`hello.fxml`) définit la structure et la disposition de l'interface utilisateur.

  Les instructions pour compiler et exécuter le projet

1. avant tout :
   - avoir un environnement de developpement.
   - installation du JDK (Java Development Kit) 17 ou supérieur.
   - Mettez à jour votre `PATH` pour inclure le `javac` et le `java`.

3. Configurer le Projet:
   - Si vous utilisez Maven, assurez-vous d'avoir le fichier `pom.xml` configuré avec les dépendances JavaFX.
   - Si vous n'utilisez pas Maven, téléchargez JavaFX SDK et ajoutez-le à votre classpath.

4. Compiler le Projet :
   - Ouvrez un terminal et allez dans le répertoire racine de votre projet.
   - Utilisez la commande suivante pour compiler votre projet:
   
     javac --module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml -d out src/helloapplication/*.java src/contact/*.java
     

5. Exécuter le Projet :
   - Après la compilation, utilisez la commande suivante pour exécuter votre application:
     java --module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml -cp out helloapplication.HelloApplication

Remarques:

Chemin de classe: Assurez-vous que le chemin de classe inclut les répertoires contenant les fichiers compilés de votre projet et les bibliothèques nécessaires (JavaFX, JDBC pour la base de données, etc.).
Configuration de la base de données: Configurez les paramètres de connexion à votre base de données dans la classe DatabaseConnection.

