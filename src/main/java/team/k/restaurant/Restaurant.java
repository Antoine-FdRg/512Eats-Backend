package team.k.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Dish;
import team.k.enumerations.FoodType;
import team.k.order.SubOrder;
import team.k.restaurant.discount.DiscountStrategy;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {
    private String name;
    private int id;
    private String open;
    private String close;
    private List<TimeSlot> timeSlots;
    private List<Dish> dishes;
    private List<FoodType> foodTypes;
    private List<SubOrder> orders;
    private DiscountStrategy discountStrategy;
    public boolean isAvailable() {
        return true; // TODO: missing this implementation
    }

    public void updateRestaurantInfos(String name, String open, String close) {
        this.name = name;
        this.open = open;
        this.close = close;
    }
}
