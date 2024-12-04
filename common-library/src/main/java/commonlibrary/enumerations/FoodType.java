package commonlibrary.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum FoodType {
    FAST_FOOD("FAST_FOOD"),
    ASIAN_FOOD("ASIAN_FOOD"),
    PIZZA("PIZZA"),
    SUSHI("SUSHI"),
    VEGAN("VEGAN"),
    POKEBOWL("POKEBOWL"),
    BURGER("BURGER"),
    SANDWICH("SANDWICH"),
    TACOS("TACOS");
    final String name;
}
