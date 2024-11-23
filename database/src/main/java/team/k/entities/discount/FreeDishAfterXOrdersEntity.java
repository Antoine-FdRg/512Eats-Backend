package team.k.entities.discount;

import io.ebean.annotation.Identity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import team.k.entities.order.SubOrderEntity;
import team.k.entities.restaurant.RestaurantEntity;

/**
 * Represents a discount strategy where a free dish is given after a certain number of orders in the restaurant.
 */
@NoArgsConstructor
@Entity
@Table(name = "free_dish_after_x_orders")
@Identity
public class FreeDishAfterXOrdersEntity extends DiscountStrategyEntity {
    /**
     * The number of orders required in the restaurant to get a free dish
     */
    private int nbOrdersRequired;

    public FreeDishAfterXOrdersEntity(RestaurantEntity restaurant, int nbOrdersRequired) {
        super(restaurant);
        this.nbOrdersRequired = nbOrdersRequired;
    }


    /**
     * Get the number of orders of the user in the restaurant
     *
     * @param order the order to check
     * @return the number of orders of the user in the restaurant
     */
    public int getNbOrderInRestaurant(SubOrderEntity order) {
        return (int) order.getUser().getOrders().stream().filter(o -> o.getRestaurant().equals(restaurant)).count();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FreeDishAfterXOrdersEntity discountStrategy)) {
            return false;
        }
        return id == discountStrategy.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
