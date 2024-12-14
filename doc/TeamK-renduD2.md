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
Un client web est un projet à part de ce dépôt, il communique en REST avec le backend via l'[API gateway](#212-lapi-gateway)).
Il permet une utilisation optimisée et ergonomique de notre application.

### 2.1.2 L'API gateway
L'API gateway (disponible dans [ce package](../api) est l'interface de notre backend end. Le client web lui envoie 
des requêtes HTTP qu'il "redirige" vers le [service métier](#213-les-services-métiers) adéquat. En exposant 
plusieurs controllers REST, l'API gateway peut interroger simplement le service métier concerné par la requête et 
ainsi envoyer la réponse reçue vers le [client web](#211-frontend). Pour simplifier fonctionnement et alléger son code, les 
réponses 
reçues par l'API gateway ne sont pas désérialisées, mais renvoyées telles quelles.

### 2.1.3 Les services métiers
Les services métiers sont les services qui gèrent les différentes 
entités (disponible dans [ce package](../common-library/src/main/java/commonlibrary/model)) de notre application.
Ils sont consommés par l'[API gateway](#212-lapi-gateway). 
Ils sont responsables de la logique métier et utilisent les entités qu'ils récupèrent grâce à la [couche DAO](#214-la-couche-dao).

### 2.1.4 La couche DAO
La couche DAO (Data Access Object) est composée de repositories JPA (disponible dans
[ce package](../common-library/src/main/java/commonlibrary/repository)) qui permettent de communiquer avec la base de 
données.

### 2.1.5 La base de données
La base de données est une base de données relationnelle PostgreSQL. Elle est composée de plusieurs tables qui
représentent les différentes entités de notre application. Elle est accessible par les [repositories JPA](#214-la-couche-dao).
Elle est exécutée dans un conteneur Docker lancé via  [dokcer-compose](../docker-compose.yml)


# 3. Qualité des codes

En ce qui concerne l'auto-évaluation de notre code, voici nos observations : 
Code de bonne qualité : 
- Le code est lisible avec des noms de méthodes et de classes transparentes. 
- Les scénarios sont clairs et englobent la totalité des specs attendues notamment le scénario avec les commandes d'un groupe Order. 
- La gestion des erreurs est faite de sorte à envoyer des messages clairs et corrects concernant les problèmes ou les succès rencontrés afin d'avertir l'utilisateur du succès ou de l'échec de ses actions. Il est aussi pour nous plus pratique de débugger et de contrôler la maintenance de notre code. 
- Sur le point de vue du partage des responsabilités, nous avons bien séparé les classes et les méthodes pour ne pas avoir trop de responsabilités dans une classe et rien dans les autres. 
- Nous avons également fait tester notre interface utilisateurs à plusieurs personnes et avons eu des retours positifs sur la facilité d'utilisation de notre application.

Code à améliorer : 
- Nous avons encore certaines fonctionnalités à implémenter pour que notre projet soit complet et fonctionnel. Il manque :
  - La gestion du profil de l'internet user ( notamment la navigation libre de tous les restaurants et leur carte qui n'est pas possible à ce jour)
  - La gestion des localisations n'est pas totalement implémentée, à l'heure actuelle nous ne pouvons pas rentrer la localisation d'une groupe order à la fin de celle-ci elle doit être décidée dès le début. 
- En ce qui concerne les endpoint que nous avons mis pour la communication REST. Ils ne sont pas conventionnels. En effet, dans nos controller, nous pouvons voir des endpoints avec des verbes dans le chemin, ou alors des path variable qui sont dans des request param. La cause de ce problème est notre implémentation du framework Rest qui traite les endpoints et les paramètres avec un système de Régex. 

# 4. Gestion de projet

# 5.Restrospective

# 6. Autoévaluation

- Concernant l'attribution des points, ayant tous travaillés équitablement, nous avons décider d’attribuer les points de
  manière homogène:
  Emma: 100pts / Roxane: 100pts / Antoine: 100pts / Clément: 100pts
