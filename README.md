# 512 Eats

![logo](doc/ressources/logo.png)

# TEAM

Ce projet a été concu par 4 personnes.\
En plus du role de Software Developer (SD), chacun possède des spécificités complémentaires:

- [Emma ALLAIN](https://github.com/emmaallain): Quality Assurance Engineer (QA)
- [Roxane BACON](https://github.com/RoxaneBacon): Continuous Integration and Repository Manager (Ops)
- [Antoine FADDA-RODRIGUEZ](https://github.com/Antoine-FdRg): Software Architect (SA)
- [Clément LEFEVRE](https://github.com/Firelods): Business Analyst and Product Owner (PO)

# Documentation

### [Voir la documentation](doc/README.md)

## User story :  A registered user place an individual order

- [Lien user story #57](https://github.com/PNS-Conception/STE-24-25--teamk/issues/57)

### As a Registered user, I want to place my order so that the restaurant can prepare the order

### [Lien feature](services/src/test/resources/features/team/k/RegisteredUserPlacesAnOrder.feature)

# Lancement & intallation du projet

## Prérequis

- Java Development Kit (JDK) 21
- Maven

## Étapes d'installation

1. **Construire le projet avec Maven** :
   ```bash
   mvn clean install
   ```

   Cette commande télécharge les dépendances nécessaires et compile le projet.

## Lancer le projet

1. **Exécuter l'application** :
   _Pour le moment, l'application ne contient pas de classe exécutable. Vous pouvez cependant lancer les tests unitaires
   avec la commande ci-après :_

# Tests

Pour exécuter les tests unitaires et fonctionnels :

```bash
mvn test
```

# Structuration globale

## 1. Dossier `.github`

-  ### [Workflows/maven.yml](.github/workflows/maven.yml) :

Contient un fichier de configuration pour les actions GitHub. Ce fichier définit un workflow qui se déclenche
automatiquement à chaque push de code sur le dépôt. Actuellement, il exécute uniquement des tests avec JUnit 5 pour
vérifier que le projet fonctionne correctement.

- ### [ISSUE_TEMPLATE](.github/ISSUE_TEMPLATE) :

Regroupe les modèles prédéfinis pour créer des issues, notamment pour les user stories et les bugs. Ces modèles
facilitent la création de tickets pour le suivi des tâches ou des problèmes rencontrés.

## 2. Module `api`

- ### [src](api/src) :
  Contient la passerelle API (API Gateway), servant d’interface entre le client web et les différents
  services métiers. La documentation OpenAPI générée automatiquement se trouve dans [openapi.json]
  (api/openapi.
  json).

## 3. Module `common-library`

- ### [src/main/java/commonlibrary](common-library/src/main/java/commonlibrary) :
  Ce module regroupe les classes communes, telles que :
    - **DTO** : Définit les objets de transfert de données utilisés entre le frontend et le backend.
    - **Enumerations** : Contient les énumérations utilisées dans l’ensemble du projet (ex. : `FoodType`,
      `OrderStatus`).
    - **Model** : Définit les entités métiers (ex. : `Dish`, `Location`).
    - **Repository** : Contient les interfaces JPA pour l’accès aux données (ex. : `SubOrderRepository`,
      `RestaurantRepository`).

- ### [src/test](common-library/src/test) :
  Comprend des tests unitaires validant les fonctionnalités des composants communs.

## 4. Dossier `services`

- ### [src/main/java/team/k](services/src/main/java/team/k) :
  Les services métiers sont organisés dans quatre modules principaux :
    - **GroupOrderService** : Gère les commandes groupées.
    - **ManagementService** : Administre les restaurants.
    - **OrderService** : Gère les commandes individuelles.
    - **RestaurantService** : Gère les informations liées aux restaurants (ex. : disponibilité des plats).

  Chaque module contient :
  - **Un controller** : Expose des endpoints REST.
  - **Une classe service** : Contient la logique métier.
  - **Une classe de configuration** : Définit les composants nécessaires au fonctionnement des services.

- ### [src/test/java/team/k](services/src/test/java/team/k) :
  Implémente des tests fonctionnels avec **Cucumber** pour valider les user stories. Les scénarios sont définis dans le
  dossier [features](services/src/test/resources/features/team/k).

## 5. Dossier `ssdb-rest-framework`

- ### [src/main/java/ssdbrestframework](ssdb-rest-framework/src/main/java/ssdbrestframework) :
  Ce module contient un framework REST personnalisé pour gérer les annotations et le traitement des requêtes HTTP. Les
  principales classes incluent :
    - **SSDBHttpServer** : Serveur HTTP personnalisable.
    - **SSDBHandler** : Gestionnaire des requêtes.
    - **Annotations** : Définissent les annotations personnalisées pour configurer les endpoints REST.

- ### [docker-compose.yml](docker-compose.yml) :
  Démarre la base de données PostgreSQL.

## 6. Dossier `doc`

- #### [README](doc/README.md)
  Ce fichier regroupe les liens vers les documents illustrant le projet
- #### [classDiagram](doc/classDiagram.md)
  Illustration de notre diagramme de classe
- #### [sequenceDiagram](doc/sequenceDiagram.md)
  Représentation de notre diagramme de séquence
- #### [SlideO1](doc/slidesO1.pdf)
  Slides de l'oral O1
- #### [Rapport D1](doc/TeamK-renduD1.pdf)
  Rapport rédigé concernant la modélisation et l'organisation du projet pour le premier rendu
- #### [SlideO2](doc/slidesO2.pdf)
  Slides de l'oral O2
- #### [Rapport D2](doc/TeamK-renduD2.md)
  Rapport rédigé concernant la réalisation et l'organisation du projet pour le deuxième rendu


- ### [ressources](doc/ressources) :
  Contient les ressources liées à la documentation du projet :
    - Diagrammes (ex. : [classDiagram.md](doc/classDiagram.md), [sequenceDiagram.md](doc/sequenceDiagram.md)).
    - Présentations et rapports (
      ex. : [TeamK-renduD1.pdf](doc/TeamK-renduD1.pdf), [TeamK-renduD2.md](doc/TeamK-renduD2.md)).

## 7. Fichier `pom.xml`

- Gère les dépendances Maven nécessaires au projet, y compris :
    - JDK 21.
    - Bibliothèques Spring Data JPA et Cucumber pour les tests.

## 4. [pom.xml](pom.xml)

- Ce fichier de configuration Maven gère les dépendances et les propriétés du projet telles que:
    - L'intégration de Cucumber 7 et JUnit 5 pour les tests.
    - La version du JDK (JDK 21).  
