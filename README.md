# 512 Eats

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

### [Lien feature](src/test/resources/features/team/k/RegisteredUserPlacesAnOrder.feature)

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

## 1. Dossier .github

-  ### [Workflows/maven.yml](.github/workflows/maven.yml) :

Contient un fichier de configuration pour les actions GitHub. Ce fichier définit un workflow qui se déclenche
automatiquement à chaque push de code sur le dépôt. Actuellement, il exécute uniquement des tests avec JUnit 5 pour
vérifier que le projet fonctionne correctement.

- ### [ISSUE_TEMPLATE](.github/ISSUE_TEMPLATE) :

Regroupe les modèles prédéfinis pour créer des issues, notamment pour les user stories et les bugs. Ces modèles
facilitent la création de tickets pour le suivi des tâches ou des problèmes rencontrés.

## 2. Dossier src

- ### Dossier [main](src/main) :

Contient le code applicatif du projet. Au sein du package [team.k](src/main/java/team/k), nous retrouvons:

### [`team.k.common`](src/main/java/team/k/common)

Ce package contient les classes communes utilisées dans tout le projet, telles que `Dish` et `Location`.

### [`team.k.enumerations`](src/main/java/team/k/enumerations)

Ce package contient les énumérations utilisées dans le projet, telles que `FoodType`, `OrderStatus`, et `Role`.

### [`team.k.enumerations`](src/main/java/team/k/external)

Ce package est composé de la relation avec l'acteur externe (ie le paiement)

### [`team.k.order`](src/main/java/team/k/order)

Ce package gère les commandes, y compris les sous-commandes (`SubOrder`), les commandes
individuelles (`IndividualOrder`), les commandes de groupe (`GroupOrder`), et les paiements (`Payment`).

### [`team.k.repository`](src/main/java/team/k/repository)

Ce package contient les classes de dépôt pour accéder et manipuler les données des différentes entités, telles
que `SubOrderRepository`, `RestaurantRepository`, `LocationRepository`, et `RegisteredUserRepository`.

### [`team.k.restaurant`](src/main/java/team/k/restaurant)

Ce package gère les restaurants, y compris les classes `Restaurant` et `TimeSlot`.

### [`team.k.restaurant.discount`](src/main/java/team/k/restaurant/discount)

Ce package contient les stratégies de réduction appliquées aux commandes de restaurant, telles
que `DiscountStrategy`, `FreeDishAfterXOrders`, `RoleDiscount`, et `UnconditionalDiscount`.

### [`team.k.service`](src/main/java/team/k/service)

Ce package contient les services pour gérer les opérations sur les restaurants et les commandes,
comme `ManageRestaurantService` et `RestaurantService`.

- ### Dossier [test](src/test) :

Regroupe les tests unitaires et fonctionnels

## 3. Dossier doc

- #### [README](doc/README.md) : Ce fichier regroupe les liens vers les documents illustrant le projet
- #### [classDiagram](doc/classDiagram.md) : Illustration de notre diagramme de classe
- #### [sequenceDiagram](doc/sequenceDiagram.md): Représentation de notre diagramme de séquence
- #### [SlideO1](doc/512Eats O1.pdf): Slide de l'oral O1
- #### [Rapport](doc/TeamK-renduD1.pdf): Rapport rédigé concernant la modélisation et  l'organisation du projet

## 4. [pom.xml](pom.xml)

- Ce fichier de configuration Maven gère les dépendances et les propriétés du projet telles que:
    - L'intégration de Cucumber 7 et JUnit 5 pour les tests.
    - La version du JDK (JDK 21).  
