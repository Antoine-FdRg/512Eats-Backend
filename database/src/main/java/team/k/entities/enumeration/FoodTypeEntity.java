package team.k.entities.enumeration;

import io.ebean.annotation.DbEnumType;
import io.ebean.annotation.DbEnumValue;
import lombok.Getter;


@Getter
public enum FoodTypeEntity {
    FAST_FOOD("Fast food"), ASIAN_FOOD("Asian food"), PIZZA("Pizza"), SUSHI("Sushi"), VEGAN("Vegan"), POKEBOWL("Pokebowl"), BURGER("Burger"), SANDWICH("Sandwich"), TACOS("Tacos");
    final String type;

    FoodTypeEntity(String type) {
        this.type = type;
    }

    @DbEnumValue(storage = DbEnumType.VARCHAR)
    public String getType() {
        return type;
    }
}
