package team.k;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum FoodType {
    FAST_FOOD("Fast food"), ASIAN_FOOD("Asian food"), PIZZA("Pizza"), SUSHI("Sushi"), VEGAN("Vegan"), POKEBOWL("Pokebowl"), BURGER("Burger"), SANDWICH("Sandwich"), TACOS("Tacos");
    String name;
}
