Feature: Charger les plats disponibles d'un restaurant

  Scenario:
    Given Un restaurant "512EatRestaurant" existe dans la liste des restaurants avec un plat "fraise" et un plat "banane"
    When L'utilisateur demande à accéder aux plats du restaurant "512EatRestaurant"
    Then L'utilisateur reçoit les dishes "fraise" et "banane" enregistrés dans le restaurant préselectionné
