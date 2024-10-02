#language: en
Feature: : Emprunter un livre

  Background: :
    Given une bibliothèque avec un etudiant de nom "Marcel" et de noEtudiant 123456
    And un etudiant de nom "Walid" et de noEtudiant 123457
    And un livre de titre "UML pour les nuls"
    And un livre de titre "Design Patterns for dummies" en deux exemplaires

  Scenario: emprunt d'un livre
    When "Marcel" emprunte le livre "UML pour les nuls"
    Then Il y a 1 dans son nombre d'emprunts
      And Il y a le livre "UML pour les nuls" dans un emprunt de la liste d'emprunts
      And Le livre "UML pour les nuls" est indisponible

  Scenario: emprunt d'un exemplaire d'un livre
    When "Marcel" emprunte le livre "Design Patterns for dummies"
    Then Il y a 1 dans son nombre d'emprunts
    And Il y a le livre "Design Patterns for dummies" dans un emprunt de la liste d'emprunts
    And Le livre "Design Patterns for dummies" est disponible

  Scenario: rendu d'un livre
    Given que "Marcel" a emprunté le livre "UML pour les nuls"
    When "Marcel" rend le livre "UML pour les nuls"
    Then Il y a 0 dans son nombre d'emprunts
    And Le livre "UML pour les nuls" est disponible

  Scenario Outline: : emprunt d'un livre non disponible
    When <nomEtudiant> emprunte le livre <titreLivre>
    And <nomEtudiant> emprunte le livre <titreLivre>
    Then Il y a <nombredEmprunts> dans son nombre d'emprunts
    And Il y a le livre <titreLivre> dans un emprunt de la liste d'emprunts
    And Le livre <titreLivre> est indisponible
    Examples:
      | nomEtudiant | titreLivre          | nombredEmprunts |
      | "Marcel"    | "UML pour les nuls" | 1               |
      | "Walid"     | "Design Patterns for dummies" | 2     |
