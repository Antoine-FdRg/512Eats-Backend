# Rendu Rapport D2 - Team K

# 512 Eats

Équipe :

- ALLAIN Emma : QA
- BACON Roxane  : OPS
- FADDA RODRIGUEZ Antoine : SA
- LEFÈVRE Clément : PO

## 1. Périmètre fonctionnel

### 1.1 Hypothèses de travail & limites identfiées

#### Hypothèses de travail

Pour mener à bien notre projet et répondre aux exigences, nous avons formulé plusieurs hypothèses de travail pour
clarifier certains points :

- #### H1. Localisation pré-enregistrée : Les localisations sont supposées être déjà enregistrées dans la base de données, sans création en temps réel.
- #### H2. Plat uniquement : Les restaurants ne proposent que des plats individuels (entrée, plat, dessert, boisson) sans formules, laissant aux clients le soin de composer leur repas.
- #### H3. 1 livreur <=> 1 commande : Chaque commande (subOrder) est prise en charge par un seul livreur, sans affectation multiple.
- #### H4. Horaires d’ouverture : Les restaurants sont ouverts tous les jours de la semaine, du lundi au vendredi, avec des horaires fixes.
- #### H5. Temps moyen de préparation d’une commande : Dans chaque restaurant, toutes les commandes ont un temps de préparation moyen identique prédéfini par le manager du restaurant.
- #### H6. Temps maximum de préparation : Le temps maximal pour préparer une commande est supposé être de 30 minutes.
- #### H7. Temps moyen de livraison : Le temps de livraison estimé pour une commande est de 20 minutes.

#### Limites identifiées

En fonction des hypothèses posées et des exigences initiales, certaines limites ont été identifiées :

- #### L1. Livraisons non optimisées : Le trafic n’est pas pris en compte, et chaque livreur est affecté à une seule commande, ce qui n’optimise pas les livraisons dans le cas de commandes individuelles non groupées.
- #### L2. Pas de formules : Chaque client doit composer son repas en sélectionnant les produits individuellement sur la carte.
- #### L3. Non responsabilité du paiement: Le processus de paiement n’étant pas à prendre en charge, malgré une couverture de cas d’erreur pour les objets envoyés au processus de paiement, nous ne pouvons pas garantir le bon déroulé d’un processus externe.
- #### L4. Préavis de commande de 50 min minimum : Pour assurer la préparation de la commande, celle-ci doit être faite au moins 50 minutes à l’avance car il faut prendre en compte le temps de livraison (20 minutes) et le temps de préparation maximum (30 minutes dans le créneau horaire du restaurant).

### 1.2 Points non implémentés relativement à la spécification et aux extensions requises

### 1.3 Points forts

### 1.4 Points faibles

## 2. Conception

### 2.1 Architecture

### 2.2 Justification de l'architecture

## 3. Qualité des codes

## 4. Gestion de projet

## 5.Restrospective

## 6. Autoévaluation