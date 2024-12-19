# Rendu Rapport D2 - Team K

![logo](ressources/logo.png)

# 512 Eats

Équipe :

- ALLAIN Emma : QA
- BACON Roxane  : OPS
- FADDA RODRIGUEZ Antoine : SA
- LEFÈVRE Clément : PO

# 1. Périmètre fonctionnel

## 1.1 Hypothèses de travail & limites identfiées

### Hypothèses de travail

Pour mener à bien notre projet et répondre aux exigences, nous avons formulé plusieurs hypothèses de travail pour
clarifier certains points :

- #### H1. Localisation pré-enregistrée : Les localisations sont supposées être déjà enregistrées dans la base de données, sans création en temps réel.
- #### H2. Plat uniquement : Les restaurants ne proposent que des plats individuels (entrée, plat, dessert, boisson) sans formules, laissant aux clients le soin de composer leur repas.
- #### H3. 1 livreur <=> 1 commande : Chaque commande (subOrder) est prise en charge par un seul livreur, sans affectation multiple.
- #### H4. Horaires d’ouverture : Les restaurants sont ouverts tous les jours de la semaine, du lundi au vendredi, avec des horaires fixes.
- #### H5. Temps moyen de préparation d’une commande : Dans chaque restaurant, toutes les commandes ont un temps de préparation moyen identique prédéfini par le manager du restaurant.
- #### H6. Temps maximum de préparation : Le temps maximal pour préparer une commande est supposé être de 30 minutes.
- #### H7. Temps moyen de livraison : Le temps de livraison estimé pour une commande est de 20 minutes.

### Limites identifiées

En fonction des hypothèses posées et des exigences initiales, certaines limites ont été identifiées :

- #### L1. Livraisons non optimisées : Le trafic n’est pas pris en compte, et chaque livreur est affecté à une seule commande, ce qui n’optimise pas les livraisons dans le cas de commandes individuelles non groupées.
- #### L2. Pas de formules : Chaque client doit composer son repas en sélectionnant les produits individuellement sur la carte.
- #### L3. Non responsabilité du paiement: Le processus de paiement n’étant pas à prendre en charge, malgré une couverture de cas d’erreur pour les objets envoyés au processus de paiement, nous ne pouvons pas garantir le bon déroulé d’un processus externe.
- #### L4. Préavis de commande de 50 min minimum : Pour assurer la préparation de la commande, celle-ci doit être faite au moins 50 minutes à l’avance car il faut prendre en compte le temps de livraison (20 minutes) et le temps de préparation maximum (30 minutes dans le créneau horaire du restaurant).

### Stratégie choisie et éléments spécifiques

Nous avons mis en place une stratégie bien particulière pour la gestion des commandes en fonction des temps de
préparation de celles-ci. Cette stratégie permet un compromis entre les utilisateurs et les restaurateurs, et donc
éviter de créer une frustration importante d’un côté comme de l’autre.
Nous sommes partis de l’exigence R2 qui fixe la durée d’un créneau (time slot) à 30 minutes.
Exemple de fonctionnement :
En ayant un temps moyen de préparation de 10 minutes par commande, avec une seule personne pour les préparer, nous
autorisons 3 commandes passée par TimeSlot (30 minutes/10 minutes * 1 préparateur(s) de commande). Si nous avons 2
préparateurs de commande, alors nous permettrons à 6 utilisateurs enregistrés de passer une commande, etc.
Remarque:
Si il y a trop de demandes pour un restaurant à un horaire précis, alors nous n'autorisons plus la création de commande
pour ce restaurant. Reprenons notre exemple avec 2 préparateurs de commandes, soit 6 commandes possibles. Si les 3
premières prennent l'entièreté du temps disponible, alors seulement 3 utilisateurs ayant déjà créé une commande seront
bloqués avant de pouvoir valider celle-ci. (voir schéma ci-dessous). A l’inverse, peut être que les 6 commandes seront
courtes et du temps pourrait être perdu, ou bien que le temps de préparation de la dernière commande acceptée peut être
plus long que le temps disponible, ce qui mettrait en retard le restaurant.

![schema explicatif](ressources/schemaP1.png)
![schema explicatif](ressources/schemaP2.png)

## 1.2 Points non implémentés relativement à la spécification et aux extensions requises

Nous n'avons pas implémenté l'extension du login sur l'interface. IL n'y a pas de gestion de token ni de mot de passe.

## 1.3 Points forts

## 1.4 Points faibles

# 2. Architecture

## 2.1 Présentation générale

Notre projet est architecturé de la manière suivante :
![schema d'architectute](ressources/architecture.png)

### 2.1.1 Client web

Le [client web](#26-le-client-web) est un projet à part de ce dépôt, il communique en REST avec le backend via l'[API
gateway](#212-lapi-gateway)).
Il permet une utilisation optimisée et ergonomique de notre application.

### 2.1.2 Backend

Tout le code du backend est disponible dans ce dépôt. C'est un [projet maven
multimodule](https://www.baeldung.com/maven-multi-module).

#### 2.1.2 L'API gateway

L'API gateway (disponible dans [ce module](../api)) est l'interface de notre backend. Le client web lui envoie
des requêtes HTTP qu'elle "redirige" vers le [service métier](#213-les-services-métiers) adéquat. En exposant
plusieurs controllers REST, l'API gateway peut interroger simplement le service métier concerné par la requête et
ainsi envoyer la réponse reçue vers le [client web](#211-client-web). Pour simplifier fonctionnement et alléger son
code, les
réponses reçues par l'API gateway ne sont pas désérialisées, mais renvoyées telles quelles.
Sa documentation OpenAPI est disponible [ici](../api/openapi.json).

#### 2.1.3 Les services métiers

Les services métiers sont les services qui gèrent les différentes
entités (disponible dans [ce module](../common-library/src/main/java/commonlibrary/model)) de notre application.
Ils sont consommés par l'[API gateway](#212-lapi-gateway).
Ils sont responsables de la logique métier et utilisent les entités qu'ils récupèrent grâce à
la [couche DAO](#214-la-couche-dao).

#### 2.1.4 La couche DAO

La couche DAO (Data Access Object) est composée de repositories [JPA](#24-les-entités) (disponible dans
[ce package](../common-library/src/main/java/commonlibrary/repository) du module [common-library](../common-library))
qui
permettent de communiquer avec la [base de données](#215-la-base-de-données).

#### 2.1.5 La base de données

La base de données est une base de données relationnelle PostgreSQL. Elle est composée de plusieurs tables qui
représentent les différentes [entités](#24-les-entités) de notre application. Elle
est accessible par les[repositories JPA](#214-la-couche-dao).
Elle est exécutée dans un conteneur Docker lancé via [dokcer-compose](../docker-compose.yml)

## 2.2 Les services métiers

Les services métiers sont les services qui gèrent les différentes fonctionnalités de notre application. Ils sont
découpés en plusieurs services pour plus de clarté, de lisibilité et de maintenabilité. Ils sont tous dans le module
[service](../services) et sont répartis en 4 packages :

- [OrderService](../services/src/main/java/team/k/orderservice) : Gère les commandes
- [RestaurantService](../services/src/main/java/team/k/restaurantservice) : Gère les restaurants et plus
  particulièrement la partie utile pour les clients
- [GroupOrderService](../services/src/main/java/team/k/grouporderservice) : Gère les groupes de commandes
- [ManagementService](../services/src/main/java/team/k/managementservice) : Administre les restaurants

Chacun de ses packages contient :

- Une classe exécutable qui permet de lancer le service et son serveur REST
- Un controller qui expose les endpoints REST qui appellent les méthodes du service
- Un service qui contient la logique métier
- Une classe de configuration qui permet de configurer le service et notamment de définir les composants Spring à
  scanner

## 2.3 Utliisation de Spring Data JPA

Spring Data JPA est une bibliothèque qui permet de simplifier l'accès aux données en utilisant JPA. Elle permet de
créer des repositories JPA en utilisant des interfaces. Ces repositories permettent de communiquer avec la base de
données sans avoir à écrire de requêtes SQL, en effet, Spring Data JPA fournit des méthodes pour effectuer des
opérations CRUD. Ces repositories étant des Beans Spring, ils sont à injecter dans les services métiers. C'est pour
cette raison que les services métiers sont annotés avec `@Service`, ils sont eux, à injecter dans les controller. Il en
va donc de même pour les controllers qui sont annotés avec `@Component`.

## 2.4 Les entités

Les entités sont les classes qui représentent les tables de la base de données, ce sont les modèles manipulés par les
services métiers. Elles sont toutes dans le package [model](../common-library/src/main/java/commonlibrary/model) du
module [common-library](../common-library). Elles sont toutes annotées avec `@Entity` pour indiquer à JPA qu'elles
représentent une table de la base de données. Elles sont aussi annotées avec `@Data` de Lombok pour générer notamment
les getters et les setters.

## 2.5 Les DTOs

Les DTOs (Data Transfer Object) sont des classes qui permettent de transférer des données entre le backend et le
frontend. Ils sont utilisés pour éviter de transférer des données inutiles et pour éviter de modifier les entités. Ils
sont tous dans le package [dto](../common-library/src/main/java/commonlibrary/dto) du
module [common-library](../common-library).
Certaines classes ne sont pas utilisées actuellement, elles sont conservées en vue d'une potentielle utilisation.

## 2.6 Le client web

<img alt="UI.png" height="500" src="ressources/UI.png"/>

## 2.7 Exemples de requêtes

### 2.7.1 Récupération des restaurants

![diagrame de séquence](ressources/diagrame%20de%20séquence%20ajout%20plat%20commande.png)
<img alt="getRestaurantRoute.png" height="300" src="ressources/getRestaurantRoute.png"/>

### 2.7.2 Ajout d'un plat dans une commande

![diagrame de séquence](ressources/diagrame%20de%20séquence%20ajout%20plat%20commande.png)

# 3. Qualité des codes

En ce qui concerne l'auto-évaluation de notre code, voici nos observations :
Code de bonne qualité :

- Le code est lisible avec des noms de méthodes et de classes transparentes.
- Les scénarios sont clairs et englobent la totalité des specs attendues notamment le scénario avec les commandes d'un
  group order.
- La gestion des erreurs est faite de sorte à envoyer des messages clairs et corrects concernant les problèmes ou les
  succès rencontrés afin d'avertir l'utilisateur du succès ou de l'échec de ses actions. Il est aussi pour nous plus
  pratique de débugger et de contrôler la maintenance de notre code.
- Sur le point de vue du partage des responsabilités, nous avons bien séparé les classes et les méthodes pour ne pas
  avoir trop de responsabilités dans une classe et rien dans les autres.
- Nous avons également fait tester notre interface utilisateurs à plusieurs personnes et avons eu des retours positifs
  sur la facilité d'utilisation de notre application.

Code à améliorer :

- Nous avons encore certaines fonctionnalités à implémenter pour que notre projet soit complet et fonctionnel. Il
  manque :
    - La gestion du profil de l'internet user ( notamment la navigation libre de tous les restaurants et leur carte qui
      n'est pas possible à ce jour)
    - La gestion des localisations n'est pas totalement implémentée, à l'heure actuelle nous ne pouvons pas rentrer la
      localisation d'un group order à la fin de celle-ci elle doit être décidée dès le début.
- En ce qui concerne les endpoints que nous avons mis pour la communication REST. Ils ne sont pas conventionnels. En
  effet, dans nos controllers, nous pouvons voir des endpoints avec des verbes dans le chemin, ou alors des "path
  variable" qui sont dans des request param. La cause de ce problème est notre implémentation du framework Rest qui
  traite les endpoints et les paramètres avec un système de Régex.

# 4. Gestion de projet

Pour la gestion de projet, nous avons utilisé la méthode Scrum. Nous avons donc mis en place les éléments suivants :

- un backlog des issues à effecteur
- des sprints de 1 semaine
- des points réguliers pour faire le point sur l'avancement du projet

Nous avons divisé l'équipe en deux binômes pour travailler sur les différentes parties du projet. Un des binômes
avançait le back-end, la séparation en micro-services et la gestion de la base de données. L'autre binôme avançait sur
le front-end et la gestion de l'interface utilisateur.

En ce qui concerne le front-end, une fois l'interface globale effectuée sur la base des user stories et des specs, le
binôme est venu travailler sur le back-end pour la création de l'API gateway et des micro-services

# 5.Restrospective

# 6. Autoévaluation

- Concernant l'attribution des points, ayant tous travaillés équitablement, nous avons décider d’attribuer les points de
  manière homogène:
  Emma: 100pts / Roxane: 100pts / Antoine: 100pts / Clément: 100pts
